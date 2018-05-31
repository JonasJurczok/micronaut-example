package example.security;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.Secured;

import java.security.Principal;

@Controller("/secure")
@Secured("isAuthenticated()")
public class SecureController {

	@Get("/")
	public String secure(final Principal principal) {
		return principal.getName();
	}
}
