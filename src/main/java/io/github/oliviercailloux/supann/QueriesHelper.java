package io.github.oliviercailloux.supann;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Verify.verify;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class QueriesHelper {

	public static void setDefaultAuthenticator() {
		final Authenticator myAuth = getTokenAuthenticator();
		Authenticator.setDefault(myAuth);
	}

	public static Authenticator getTokenAuthenticator() {
		final Token token;
		try {
			token = getToken();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		final PasswordAuthentication passwordAuthentication = new PasswordAuthentication(token.getId(),
				token.getPassword().toCharArray());
		final Authenticator myAuth = getConstantAuthenticator(passwordAuthentication);
		return myAuth;
	}

	private static Token getToken() throws IOException {
		{
			final String id = System.getenv("API_id");
			final String password = System.getenv("API_password");
			checkState((id == null) == (password == null));
			if (id != null && password != null) {
				return Token.given(id, password);
			}
		}
		{
			final String id = System.getProperty("API_id");
			final String password = System.getProperty("API_password");
			checkState((id == null) == (password == null));
			if (id != null && password != null) {
				return Token.given(id, password);
			}
		}

		final Path path = Paths.get("API_login.txt");
		checkState(Files.exists(path), "No token found in environment, in property or in file.");
		final List<String> content = Files.readAllLines(path, StandardCharsets.UTF_8);
		verify(content.size() == 2);
		return Token.given(content.get(0), content.get(1));
	}

	private static Authenticator getConstantAuthenticator(PasswordAuthentication passwordAuthentication) {
		final Authenticator myAuth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return passwordAuthentication;
			}
		};
		return myAuth;
	}

}
