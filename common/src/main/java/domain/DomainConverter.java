package domain;

/**
 *
 * @author benscobie
 */
public class DomainConverter {
    public Customer accountToCustomer(Account account) {
        Customer customer = new Customer();
        customer.setCustomerCode(account.getUsername());
        customer.setGroup("0afa8de1-147c-11e8-edec-2b197906d816");
        customer.setEmail(account.getEmail());
        customer.setFirstName(account.getFirstName());
        customer.setLastName(account.getLastName());
        return customer;
  }

}
