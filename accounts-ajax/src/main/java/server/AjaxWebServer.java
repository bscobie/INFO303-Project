package server;

import io.jooby.Jooby;
import io.jooby.Route;
import io.jooby.ServerOptions;
import java.io.IOException;
import java.nio.file.Paths;

public class AjaxWebServer extends Jooby {

	public AjaxWebServer() {
            
            // set the port that the web server will run on
            setServerOptions(new ServerOptions().setPort(8081));

            // handle favicons (sends a 404, but doesn't clutter up the output with 404 errors)
            get("/favicon.ico", Route.FAVICON);

            // serve anything that matches a file in the static folder
            assets("/*", Paths.get("static"));
	}

	public static void main(String[] args) throws IOException {
            System.out.println("\n*** AJAX Client Web Server ***");
            new AjaxWebServer().start();
	}

}
