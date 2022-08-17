package service;

import dao.AccountsDAO;
import graphql.AccountWiring;
import io.jooby.Cors;
import io.jooby.CorsHandler;
import io.jooby.Jooby;
import io.jooby.ServerOptions;
import io.jooby.graphql.GraphQLModule;
import io.jooby.graphql.GraphiQLModule;
import io.jooby.json.GsonModule;
import java.io.IOException;

public class AccountsService extends Jooby {

	public AccountsService() {

		AccountsDAO dao = new AccountsDAO();

		setServerOptions(new ServerOptions().setPort(8082));

                decorator(new CorsHandler(new Cors().setMethods("GET", "POST", "PUT", "DELETE")));
                
		install(new GsonModule());
		install(new GraphQLModule(new AccountWiring(dao).getWiring()));
		install(new GraphiQLModule());

		get("/", (ctx) -> ctx.sendRedirect("/graphql"));
	}

	public static void main(String[] args) throws IOException {
		System.out.println("\n\n****** Accounts Service ******\n\n");
		new AccountsService().start();
	}

}
