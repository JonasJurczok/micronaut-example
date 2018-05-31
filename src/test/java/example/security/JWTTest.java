package example.security;

import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import org.hamcrest.beans.HasPropertyWithValue;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.text.ParseException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class JWTTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	private static EmbeddedServer server;
	private static HttpClient client;

	@BeforeClass
	public static void setupServer() {
		server = ApplicationContext.run(EmbeddedServer.class);
		client = server
			.getApplicationContext()
			.createBean(HttpClient.class, server.getURL());
	}

	@AfterClass
	public static void stopServer() {
		if(server != null) {
			server.stop();
		}
		if(client != null) {
			client.stop();
		}
	}

	@Test
	public void testUnauthorized() {
		expectedException.expect(HttpClientResponseException.class);
		expectedException.expect(new HasPropertyWithValue<HttpClientResponseException>("status", is(HttpStatus.UNAUTHORIZED)));

		client.toBlocking().exchange(HttpRequest.GET("/secure"));

	}

	@Test
	public void testAuthorized() throws ParseException {
		BlockingHttpClient client = JWTTest.client.toBlocking();

		UsernamePasswordCredentials creds = new UsernamePasswordCredentials("sherlock", "password");
		HttpRequest request = HttpRequest.POST("/login", creds);
		HttpResponse<BearerAccessRefreshToken> rsp = client.exchange(request, BearerAccessRefreshToken.class);

		assertThat(rsp.getStatus(), is(HttpStatus.OK));
		assertThat(rsp.body().getUsername(), is("sherlock"));

		assertThat(rsp.body().getAccessToken(), is(not(nullValue())));
		assertThat(JWTParser.parse(rsp.body().getAccessToken()), instanceOf(SignedJWT.class));

		assertThat(rsp.body().getRefreshToken(), is(not(nullValue())));
		assertThat(JWTParser.parse(rsp.body().getRefreshToken()), instanceOf(SignedJWT.class));

		String accessToken = rsp.body().getAccessToken();
		HttpRequest requestWithAuthorization = HttpRequest.GET("/secure").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		HttpResponse<String> response = client.exchange(requestWithAuthorization, String.class);

		assertThat(response.getStatus(), is(HttpStatus.OK));
		assertThat(response.body(), is("sherlock"));
	}
}
