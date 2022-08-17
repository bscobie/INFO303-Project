package domain;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Sale implements Serializable {

	private String id;

	@SerializedName("sale_date")
	private String saleDate;

	private Customer customer;

	@SerializedName("register_sale_products")
	private List<SaleItem> items = new ArrayList<>();

	@SerializedName("totals")
	private Totals totals;

	public Sale() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(String saleDate) {
		this.saleDate = saleDate;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<SaleItem> getItems() {
		return items;
	}

	public void setItems(List<SaleItem> items) {
		this.items = items;
	}

	public Totals getTotals() {
		return totals;
	}

	public void setTotals(Totals totals) {
		this.totals = totals;
	}

	@Override
	public String toString() {
		return "Sale{" + "id=" + id + ", saleDate=" + saleDate + ", customer=" + customer + ", items=" + items + ", totals=" + totals + '}';
	}

}
