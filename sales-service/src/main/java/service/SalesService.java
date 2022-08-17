package service;

import dao.SaleDAO;
import io.jooby.Jooby;
import io.jooby.OpenAPIModule;
import io.jooby.ServerOptions;
import io.jooby.json.GsonModule;
import java.io.IOException;
import resource.CustomerSalesResource;
import resource.SalesResource;

public class SalesService extends Jooby {

	public SalesService() {
		SaleDAO dao = new SaleDAO();

		setServerOptions(new ServerOptions().setPort(8083));

		install(new GsonModule());
		install(new OpenAPIModule());

		assets("/openapi.json", "sales-openapi.json");
		assets("/openapi.yaml", "sales-openapi.yaml");

		mount(new SalesResource(dao));
		mount(new CustomerSalesResource(dao));

		get("/", ctx -> ctx.sendRedirect("/swagger"));
	}

	public static void main(String[] args) throws IOException {
		System.out.println("\n\n****** Sales Service ******\n\n");
		new SalesService().start();
	}

}
