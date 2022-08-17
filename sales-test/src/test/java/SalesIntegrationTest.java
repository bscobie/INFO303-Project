import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import domain.Customer;
import domain.Sale;
import domain.SaleItem;
import api.SalesApi;
import api.SalesForCustomerApi;
import domain.Summary;
import domain.Totals;
import java.io.IOException;
import java.util.List;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.IsCloseTo.closeTo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

public class SalesIntegrationTest {

	Retrofit retrofit = new Retrofit.Builder()
		.baseUrl("http://localhost:8083/api/")
		.addConverterFactory(GsonConverterFactory.create())
		.build();

	SalesApi sales = retrofit.create(SalesApi.class);
	SalesForCustomerApi customers = retrofit.create(SalesForCustomerApi.class);

	Customer cust1;
	Customer cust2;
	Sale sale1;
	Sale sale2;
	Sale sale3;
	Sale sale4;

	@BeforeEach
	public void setUp() throws IOException {
		cust1 = new Customer()
			.id("cust1_id")
			.firstName("Borris")
			.lastName("McNorris")
			.customerCode("654321")
			.email("cust1@example.net")
			.customerGroupId("regular");

		cust2 = new Customer()
			.id("cust2_id")
			.firstName("Doris")
			.lastName("Delores")
			.customerCode("123456")
			.email("cust2@example.net")
			.customerGroupId("regular");

		sale1 = new Sale()
			.id("1")
			.saleDate("today")
			.customer(cust1)
			.addRegisterSaleProductsItem(new SaleItem().price(100.00).productId("12345").quantity(2.0))
			.totals(new Totals().totalPayment(200.00).totalPrice(200.00).totalTax(0.0));

		sale2 = new Sale()
			.id("2")
			.saleDate("today")
			.customer(cust1)
			.addRegisterSaleProductsItem(new SaleItem().price(5000.00).productId("54321").quantity(2.0))
			.totals(new Totals().totalPayment(10000.00).totalPrice(10000.00).totalTax(0.0));

		sale3 = new Sale()
			.id("3")
			.saleDate("today")
			.customer(cust2)
			.addRegisterSaleProductsItem(new SaleItem().price(100.00).productId("12345").quantity(2.0))
			.totals(new Totals().totalPayment(100.00).totalPrice(100.00).totalTax(0.0));

		sale4 = new Sale()
			.id("4")
			.saleDate("today")
			.customer(cust1)
			.addRegisterSaleProductsItem(new SaleItem().price(100.00).productId("12345").quantity(2.0))
			.totals(new Totals().totalPayment(100.00).totalPrice(100.00).totalTax(0.0));

		sales.addSale(sale1).execute();
		sales.addSale(sale2).execute();
		sales.addSale(sale3).execute();
		// intentionally not adding sale4
	}

	@AfterEach
	public void cleanUp() throws IOException {
		sales.deleteSale(sale1.getId()).execute();
		sales.deleteSale(sale2.getId()).execute();
		sales.deleteSale(sale3.getId()).execute();
		sales.deleteSale(sale4.getId()).execute();
	}

	@Test
	public void testCreateSale() throws IOException {
		Response<Void> addRsp = sales.addSale(sale4).execute();
		assertThat("add operation succeeded", addRsp.isSuccessful(), is(true));
		assertThat("response code should be 201", addRsp.code(), is(201));

		Response<List<Sale>> salesRsp = customers.getSalesForCustomer(cust1.getId()).execute();
		assertThat("get operation succeeded", salesRsp.isSuccessful(), is(true));
		assertThat("new sale should exists at service", salesRsp.body(), hasItem(sale4));

		Response<Void> duplicateRsp = sales.addSale(sale4).execute();
		assertThat("creating a duplicate sale should fail", duplicateRsp.isSuccessful(), is(not(true)));
		assertThat("creating a duplicate sale should cause a 422", duplicateRsp.code(), is(422));
	}

	@Test
	public void testDeleteSale() throws IOException {
		Response<List<Sale>> salesRsp = customers.getSalesForCustomer(cust1.getId()).execute();
		assertThat("get operation should succeed", salesRsp.isSuccessful(), is(true));
		assertThat("sale we are about to delete should currently exist", salesRsp.body(), hasItem(sale1));

		Response<Void> response = sales.deleteSale(sale1.getId()).execute();
		assertThat("delete sale succeeded", response.isSuccessful(), is(true));

		salesRsp = customers.getSalesForCustomer(cust1.getId()).execute();
		assertThat("deleted sale should no longer exist at service", salesRsp.body(), not(hasItem(sale1)));

		Response<Void> notFoundResponse = sales.deleteSale("BAD ID").execute();
		assertThat("deleting with a bad ID should fail", notFoundResponse.isSuccessful(), is(not(true)));
		assertThat("response code should be 404", notFoundResponse.code(), is(404));
	}

	@Test
	public void testGetSales() throws IOException {
		Response<List<Sale>> salesRsp = customers.getSalesForCustomer(cust1.getId()).execute();
		assertThat("get operation succeeded", salesRsp.isSuccessful(), is(true));
		assertThat("sale1 and sale2 should exist", salesRsp.body(), hasItems(sale1, sale2));
		assertThat("sale3 sould not exist", salesRsp.body(), not(hasItem(sale3)));
	}

	@Test
	@SuppressWarnings("null")
	public void testGetSummary() throws IOException {
		Response<Summary> summaryRsp = customers.getSummary(cust1.getId()).execute();
		assertThat(summaryRsp.isSuccessful(), is(true));
		assertThat(summaryRsp.body().getNumberOfSales(), is(2));
		assertThat(summaryRsp.body().getTotalPayment(), is(closeTo(10200.0, 0.0000001)));
		assertThat(summaryRsp.body().getGroup(), is("VIP Customers"));
	}

}
