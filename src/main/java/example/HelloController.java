package example;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.Secured;
import io.micronaut.security.rules.SecurityRule;

import static io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS;

@Controller("/hello")
@Secured(IS_ANONYMOUS)
public class HelloController {

    @Get("/")
    public String index() {
        return "Hello World";
    }
}
