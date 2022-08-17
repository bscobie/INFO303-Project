package domain;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class SaleItem implements Serializable {

	@SerializedName("product_id")
	private String productId;

	private Double quantity;

	private Double price;

	public SaleItem() {
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "SaleItem{" + "productId=" + productId + ", quantity=" + quantity + ", price=" + price + '}';
	}

}
