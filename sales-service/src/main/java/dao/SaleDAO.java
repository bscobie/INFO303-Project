package dao;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import domain.Sale;
import domain.Summary;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SaleDAO {

	private static final Multimap<String, Sale> salesByCustomer = ArrayListMultimap.create();
	private static final Map<String, Sale> salesBySaleId = new HashMap<>();

	private static final Double THRESHHOLD = 5000.0;

	public void save(Sale sale) {
		salesByCustomer.put(sale.getCustomer().getId(), sale);
		salesBySaleId.put(sale.getId(), sale);
	}

	public void remove(String saleId) {
		Sale sale = salesBySaleId.get(saleId);
		salesByCustomer.remove(sale.getCustomer().getId(), sale);
		salesBySaleId.remove(sale.getId());
	}

	public Collection<Sale> getAllSales() {
		return salesBySaleId.values();
	}

	public Sale getSaleById(String saleId) {
		return salesBySaleId.get(saleId);
	}

	public Collection<Sale> getSalesByCustomerId(String customerId) {
		return salesByCustomer.get(customerId);
	}

	public Boolean doesSaleExist(String saleId) {
		return salesBySaleId.containsKey(saleId);
	}

	public Boolean doesCustomerExist(String customerId) {
		return salesByCustomer.containsKey(customerId);
	}

	public Summary getSummary(String customerId) {
		Collection<Sale> custSales = getSalesByCustomerId(customerId);

		Summary summary = new Summary();
		summary.setNumberOfSales(custSales.size());
		Double totalPayment = custSales.stream().mapToDouble(sale -> sale.getTotals().getTotalPayment()).sum();
		summary.setTotalPayment(totalPayment);
		summary.setGroup(totalPayment <= THRESHHOLD ? "Regular Customers" : "VIP Customers");

		return summary;
	}

}
