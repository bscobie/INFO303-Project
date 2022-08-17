package dao;

import domain.Account;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AccountsDAO {

	private static final Map<String, Account> accounts = new HashMap<>();

	public Collection<Account> getAll() {
		return new ArrayList<>(accounts.values());
	}

	public Account addAccount(Account account) {
		accounts.put(account.getId(), account);
		return account;
	}

	public Account getAccountById(String id) {
		return accounts.get(id);
	}

	public Account deleteAccountById(String id) {
		Account account = accounts.remove(id);
		return account;
	}

	public Account changeGroup(String id, String newGroup) {
		Account acc = accounts.get(id);
		acc.setGroup(newGroup);
		return acc;
	}

	public boolean exists(String id) {
		return accounts.containsKey(id);
	}

}
