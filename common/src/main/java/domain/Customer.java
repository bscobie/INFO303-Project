package domain;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.Objects;

public class Customer implements Serializable {

	private String id;

	@SerializedName("customer_group_id")
	private String group;

	private String email;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("customer_code")
	private String customerCode;

	public Customer() {
	}

        public Customer(String id, String group, String email, String firstName, String lastName, String customerCode) {
            this.id = id;
            this.group = group;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.group = group;
        }
        
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	@Override
	public String toString() {
		return "Customer{" + "id=" + id + ", group=" + group + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + ", customerCode=" + customerCode + '}';
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 29 * hash + Objects.hashCode(this.id);
		hash = 29 * hash + Objects.hashCode(this.group);
		hash = 29 * hash + Objects.hashCode(this.email);
		hash = 29 * hash + Objects.hashCode(this.firstName);
		hash = 29 * hash + Objects.hashCode(this.lastName);
		hash = 29 * hash + Objects.hashCode(this.customerCode);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Customer other = (Customer) obj;
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		if (!Objects.equals(this.group, other.group)) {
			return false;
		}
		if (!Objects.equals(this.email, other.email)) {
			return false;
		}
		if (!Objects.equals(this.firstName, other.firstName)) {
			return false;
		}
		if (!Objects.equals(this.lastName, other.lastName)) {
			return false;
		}
		if (!Objects.equals(this.customerCode, other.customerCode)) {
			return false;
		}
		return true;
	}

}
