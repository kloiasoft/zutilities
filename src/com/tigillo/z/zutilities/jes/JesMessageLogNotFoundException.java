package com.tigillo.z.zutilities.jes;

public class JesMessageLogNotFoundException extends Exception {

	private static final long serialVersionUID = 5290435369505568013L;

	public JesMessageLogNotFoundException() {
		super("Jes message log file (JESMSGLG) not found.");
	}

}
