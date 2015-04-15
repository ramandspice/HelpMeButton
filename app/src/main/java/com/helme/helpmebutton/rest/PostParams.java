package com.helme.helpmebutton.rest;

public enum PostParams {
	ACCESS_TOKEN("access_token"),
	ACCESS_PASSWORD("access_password"),
	METHOD("method"),
	Q("q");
	
	private final String param;

	private PostParams(final String param) {
		this.param = param;
	}
	
	@Override
	public String toString() {
		return param;
	}
}
