package com.gentics.kitchenoffice.webapp.util;

import java.io.File;

public class Filename {

	public static final String pathSeparator = File.separator;
	public static final char EXTENSIONSEPARATOR = '.';

	public static String extension(String fullPath) {
		int dot = fullPath.lastIndexOf(EXTENSIONSEPARATOR);
		return fullPath.substring(dot + 1);
	}

	public static String filename(String fullPath) {
		int dot = fullPath.lastIndexOf(EXTENSIONSEPARATOR);
		int sep = fullPath.lastIndexOf(pathSeparator);
		return fullPath.substring(sep + 1, dot);
	}

	public static String path(String fullPath) {
		int sep = fullPath.lastIndexOf(pathSeparator);
		return fullPath.substring(0, sep);
	}
}
