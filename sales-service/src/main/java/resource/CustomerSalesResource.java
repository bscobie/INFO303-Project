package resource;

import dao.SaleDAO;
import domain.ErrorMessage;
import io.jooby.Jooby;
import io.jooby.StatusCode;

public class CustomerSalesResource extends Jooby {

	public CustomerSalesResource(SaleDAO dao) {

		path("/api/sales/customer/{customerId}", () -> {

			before(ctx -> {
				String id = ctx.path("customerId").value();

				if (!dao.doesCustomerExist(id)) {
					ctx
						.setResponseCode(StatusCode.NOT_FOUND)
						.render(new ErrorMessage(String.format("No customer found with the ID '%s'", id)));
				}
			});

			get("", ctx -> {
				String id = ctx.path("customerId").value();
				return dao.getSalesByCustomerId(id);
			});

			get("/summary", ctx -> {
				String id = ctx.path("customerId").value();
				return dao.getSummary(id);
			});
		});

	}

}
