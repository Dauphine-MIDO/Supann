package io.github.oliviercailloux.supann;

public class Token {
	public static Token given(String id, String password) {
		return new Token(id, password);
	}

	private final String id;

	private final String password;

	private Token(String id, String password) {
		this.id = id;
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}
}
