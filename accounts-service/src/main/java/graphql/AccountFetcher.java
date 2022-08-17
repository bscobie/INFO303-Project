package graphql;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.AccountsDAO;
import domain.Account;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import java.util.Map;

public class AccountFetcher {

	private final AccountsDAO dao;

	public AccountFetcher(AccountsDAO dao) {
		this.dao = dao;
	}

	public DataFetcher getAddAccountFetcher() {
		return (DataFetchingEnvironment dfe) -> {
			Map map = dfe.getArgument("account");
			ObjectMapper mapper = new ObjectMapper();
			Account newAccount = mapper.convertValue(map, Account.class);
			return dao.addAccount(newAccount);
		};
	}

	public DataFetcher getAllFetcher() {
		return (DataFetchingEnvironment dfe) -> {
			return dao.getAll();
		};
	}

	public DataFetcher getAccountByIdFetcher() {
		return (DataFetchingEnvironment dfe) -> {
			String id = dfe.getArgument("id");
			return dao.getAccountById(id);
		};
	}

	public DataFetcher getChangeGroupFetcher() {
		return (DataFetchingEnvironment dfe) -> {
			String id = dfe.getArgument("id");
			String group = dfe.getArgument("newGroup");
			return dao.changeGroup(id, group);
		};
	}

	public DataFetcher getDeleteAccountFetcher() {
		return (DataFetchingEnvironment dfe) -> {
			String id = dfe.getArgument("id");
			return dao.deleteAccountById(id);
		};
	}

}
