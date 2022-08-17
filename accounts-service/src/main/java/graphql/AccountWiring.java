package graphql;

import dao.AccountsDAO;
import graphql.schema.idl.RuntimeWiring;
import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

public class AccountWiring {

	private final AccountsDAO dao;

	public AccountWiring(AccountsDAO dao) {
		this.dao = dao;
	}

	public RuntimeWiring getWiring() {
		return RuntimeWiring
			.newRuntimeWiring()
			.type(
				newTypeWiring("AccountQuery")
					.dataFetcher("accounts", new AccountFetcher(dao).getAllFetcher())
					.dataFetcher("accountById", new AccountFetcher(dao).getAccountByIdFetcher())
			).type(
				newTypeWiring("AccountMutation")
					.dataFetcher("addAccount", new AccountFetcher(dao).getAddAccountFetcher())
					.dataFetcher("changeGroup", new AccountFetcher(dao).getChangeGroupFetcher())
					.dataFetcher("delete", new AccountFetcher(dao).getDeleteAccountFetcher())
			)
			.build();
	}

}
