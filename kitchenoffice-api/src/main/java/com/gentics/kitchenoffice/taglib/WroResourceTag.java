package com.gentics.kitchenoffice.taglib;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class WroResourceTag extends TagSupport {
	
	private static final long serialVersionUID = -3140104631626098644L;

	private static final String WROMAPPING_PROPERTIES_FILENAME = "wromapping.properties";

	private static Properties wroMapping;

	private String resource;

	private String path;

	@Override
	public int doStartTag() throws JspException {

		StringBuilder sb = new StringBuilder();

		try {
			String contextPath = pageContext.getServletContext().getContextPath();
			
			sb.append(contextPath);
			sb.append(path);

			if (!path.endsWith("/")) {
				sb.append('/');
			}
			sb.append(getWroMapping().getProperty(resource, resource));
			pageContext.getOut().write(sb.toString());
		} catch (IOException io) {
			throw new JspException(io);
		}
		return super.doStartTag();
	}

	private Properties getWroMapping() throws IOException {
		if (wroMapping == null) {
			wroMapping = new Properties();
			String wroMappingFile = WROMAPPING_PROPERTIES_FILENAME;
			InputStream inputStream = pageContext.getServletContext().getResourceAsStream("/WEB-INF/" + wroMappingFile);
			if (inputStream == null) {
				throw new FileNotFoundException("property file '" + wroMappingFile + "' not found in the classpath");
			}
			wroMapping.load(inputStream);
		}
		return wroMapping;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}