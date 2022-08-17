package domain;

import java.io.Serializable;

public class Summary implements Serializable {

	private Integer numberOfSales;
	private Double totalPayment;
	private String group;

	public Summary() {
	}

	public Integer getNumberOfSales() {
		return numberOfSales;
	}

	public void setNumberOfSales(Integer numberOfSales) {
		this.numberOfSales = numberOfSales;
	}

	public Double getTotalPayment() {
		return totalPayment;
	}

	public void setTotalPayment(Double totalPayment) {
		this.totalPayment = totalPayment;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return "Summary{" + "numberOfSales=" + numberOfSales + ", totalPayment=" + totalPayment + ", group=" + group + '}';
	}

}
