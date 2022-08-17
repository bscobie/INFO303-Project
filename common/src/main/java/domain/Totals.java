package domain;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Totals implements Serializable {

	@SerializedName("total_price")
	private Double totalPrice;

	@SerializedName("total_tax")
	private Double totalTax;

	@SerializedName("total_payment")
	private Double totalPayment;

	public Totals() {
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Double getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(Double totalTax) {
		this.totalTax = totalTax;
	}

	public Double getTotalPayment() {
		return totalPayment;
	}

	public void setTotalPayment(Double totalPayment) {
		this.totalPayment = totalPayment;
	}

	@Override
	public String toString() {
		return "Totals{" + "totalPrice=" + totalPrice + ", totalTax=" + totalTax + ", totalPayment=" + totalPayment + '}';
	}

}
