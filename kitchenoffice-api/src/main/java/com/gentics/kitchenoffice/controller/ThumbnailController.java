package com.gentics.kitchenoffice.controller;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ServletContextAware;

import com.gentics.kitchenoffice.server.thumbnail.ImageMagickFileThumbnailCreator;
import com.gentics.kitchenoffice.server.thumbnail.Thumbnail;
import com.gentics.kitchenoffice.server.thumbnail.ThumbnailException;
import com.gentics.kitchenoffice.server.thumbnail.ThumbnailFetcher;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Controller
@RequestMapping("/thumbs")
public class ThumbnailController implements ServletContextAware {

	/**
	 * My logger.
	 */
	private final transient Logger logger = LoggerFactory.getLogger(ThumbnailController.class);

	private ServletContext context;

	@Value("media/img/thumbs")
	private String thumbsDir = "media/img/thumbs";

	@Value("media/img")
	private String originalsDir = "media/img";

	private ThumbnailFetcher factory;

	/**
	 * Only a number to make sure thumbnails aren't created more than once (at a
	 * time).
	 */
	private static final int CACHE_MAX_SIZE = 10000;

	/**
	 * Cache.
	 */
	private final Cache<Thumbnail, File> cache = CacheBuilder.newBuilder().maximumSize(CACHE_MAX_SIZE).build();

	@PostConstruct
	public void initialize() {

		factory = new ThumbnailFetcher(context.getRealPath("/") + originalsDir, context.getRealPath("/" + thumbsDir));
	}

	@RequestMapping(value = "/{filename}-{width}x{height}.{extension}", method = RequestMethod.GET)
	public void serveThumb(@PathVariable(value = "filename") String filename,
			@PathVariable(value = "width") Integer width, @PathVariable(value = "height") Integer height,
			@PathVariable(value = "extension") String extension, HttpServletRequest request,
			HttpServletResponse response) throws ThumbnailException {

		final Thumbnail thumbnail = factory.get(filename, width, height, extension);

		if (!doTheThumbnailExist(thumbnail)) {

			try {
				// kind of a simple hack to make sure only
				// one thread creates the actual thumbnail,
				// think the Memoizer
				cache.get(thumbnail, new ImageMagickFileThumbnailCreator(thumbnail));
			} catch (ExecutionException e) {
				if (logger.isErrorEnabled())
					logger.error("Couldn't create thumbnail", e.getCause());
				try {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							Thumbnail.ERROR_MESSAGE_THUMBNAIL_NOT_CREATED);
				} catch (IOException e1) {
					logger.error("could not send error message", e);
				}
				return;
			}
		}

		try {
			returnTheThumbnail(request, response, thumbnail);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Check if the thumbnail already exists.
	 * 
	 * @param thumbnail
	 *            the thumbnail
	 * @return true if the thumbnails exist
	 */
	protected boolean doTheThumbnailExist(Thumbnail thumbnail) {
		final File theImageThumbnail = new File(thumbnail.getDestinationDir() + thumbnail.getImageFileName());

		return theImageThumbnail.exists();
	}

	/**
	 * Return the thumbnail to the user.
	 * 
	 * @param req
	 *            the request
	 * @param resp
	 *            the response
	 * @param thumbnail
	 *            the thumbnail that will be returned
	 * @throws ServletException
	 *             if the thumbnail couldn't be delivered
	 * @throws IOException
	 *             if the thumbnail couldn't be delivered
	 */
	protected void returnTheThumbnail(HttpServletRequest req, HttpServletResponse resp, Thumbnail thumbnail)
			throws ServletException, IOException {
		final RequestDispatcher rd = context.getRequestDispatcher("/" + thumbsDir + thumbnail.getGeneratedFilePath()
				+ thumbnail.getImageFileName());
		rd.forward(req, resp);
	}

	@Override
	public void setServletContext(ServletContext arg0) {
		this.context = arg0;
	}
}
