package com.helme.helpmebutton.rest;

public enum MethodNames {
    LOGIN("logIn"),
	ADD_USER("addUser");
	
	private final String methodName;

	private MethodNames(final String methodName) {
		this.methodName = methodName;
	}
	
	@Override
	public String toString() {
		return methodName;
	}
}
