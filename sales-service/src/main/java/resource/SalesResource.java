package resource;

import dao.SaleDAO;
import domain.ErrorMessage;
import domain.Sale;
import io.jooby.Jooby;
import io.jooby.StatusCode;

public class SalesResource extends Jooby {

	public SalesResource(SaleDAO dao) {
		path("/api/sales", () -> {

			get("", ctx -> {
				return dao.getAllSales();
			});

			post("", ctx -> {
				Sale sale = ctx.body().to(Sale.class);

				if (!dao.doesSaleExist(sale.getId())) {
					dao.save(sale);
					ctx.send(StatusCode.CREATED);
				} else {
					ctx
						.setResponseCode(StatusCode.UNPROCESSABLE_ENTITY)
						.render(new ErrorMessage("There is already a sale with that ID."));
				}

				return ctx;
			});

			path("/sale/{saleId}", () -> {

				before(ctx -> {
					String id = ctx.path("saleId").value();

					if (!dao.doesSaleExist(id)) {
						ctx
							.setResponseCode(StatusCode.NOT_FOUND)
							.render(new ErrorMessage(String.format("No sale found with the ID '%s'", id)));
					}
				});

				get("", ctx -> {
					String id = ctx.path("saleId").value();
					return dao.getSaleById(id);
				});

				delete("", ctx -> {
					String id = ctx.path("saleId").value();
					dao.remove(id);
					return ctx.send(StatusCode.NO_CONTENT);
				});

			});

		});

	}

}
