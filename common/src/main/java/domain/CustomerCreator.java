package domain;

/**
 *
 * @author benscobie
 */
public class CustomerCreator {
    public Customer createCustomer(String id, String group, String email, String firstName, String lastName, String customerCode) {
        return new Customer(id, group, email, firstName, lastName, customerCode);
    }
}
