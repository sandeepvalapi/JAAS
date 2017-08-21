package com.za.tutorial.java.security;

import java.io.Serializable;
import java.security.Principal;

public class ZaPrincipal implements Principal, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String name;

	public ZaPrincipal(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		System.out.println("ZaPrincipal.getName...");
		return name;
	}

	public boolean equals(Object object) {
		boolean flag = false;
		if (object instanceof ZaPrincipal)
			flag = name.equals(((ZaPrincipal) object).getName());
		return flag;
	}
}
