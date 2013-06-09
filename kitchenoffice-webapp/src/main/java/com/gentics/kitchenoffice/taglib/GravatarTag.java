package com.gentics.kitchenoffice.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import de.bripkens.gravatar.DefaultImage;
import de.bripkens.gravatar.Gravatar;
import de.bripkens.gravatar.Rating;

public class GravatarTag extends SimpleTagSupport {
	
	private String email = "";
	
	private Integer size = 50;
	
	private Boolean https = true;
	
	private Rating rating = Rating.GENERAL_AUDIENCE;
	
	private DefaultImage defaultImage = DefaultImage.MONSTER;
	
	public void doTag() throws JspException {

		PageContext context = (PageContext) getJspContext();
		JspWriter out = context.getOut();

		try {
			StringBuffer sb = new StringBuffer();
			
			sb.append(new Gravatar()
		    .setSize(size)
		    .setHttps(https)
		    .setRating(rating)
		    .setStandardDefaultImage(defaultImage)
		    .getUrl(email));

			out.println(sb.toString());

		} catch (Exception e) {
			throw new JspException(e);
		}
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public void setHttps(Boolean https) {
		this.https = https;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}

	public void setDefaultImage(DefaultImage image) {
		this.defaultImage = image;
	}
}
