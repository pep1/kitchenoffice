/******************************************************
 * Image resize servlet
 * 
 *
 * Copyright (C) 2012 by Peter Hedenskog (http://peterhedenskog.com)
 *
 ******************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in 
 * compliance with the License. You may obtain a copy of the License at
 * 
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is 
 * distributed  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   
 * See the License for the specific language governing permissions and limitations under the License.
 *
 *******************************************************
 */
package com.gentics.kitchenoffice.server.thumbnail;

import java.io.File;

/**
 * Fetch a thumbnail object.
 * 
 */
public class ThumbnailFetcher {

	/**
	 * Where the original image will be located, need to be changed in a love
	 * environment.
	 */
	private final String originalBaseDir;

	/**
	 * The base dir where the thumbnails will be created.
	 */
	private final String destinationBaseDir;

	public ThumbnailFetcher(String theOriginalBaseDir, String theDestinationBaseDir) {
		originalBaseDir = theOriginalBaseDir;
		destinationBaseDir = theDestinationBaseDir;
	}

	/**
	 * Get a thumbnail object.
	 * 
	 * @param fileName
	 *            the name of the thumbnail
	 * @param extension
	 * @param height
	 * @param width
	 * @return the thumbnail
	 * @throws ThumbnailException
	 *             if the size isn't valid or the original image doesn't exist
	 */
	public Thumbnail get(String fileName, Integer width, Integer height, String extension) throws ThumbnailException {

		final Thumbnail thumbnail = new Thumbnail(fileName, width, height, extension, originalBaseDir,
				destinationBaseDir);

		// do the original image exist
		if (!doTheOriginalImageExist(thumbnail))
			throw new ThumbnailException(Thumbnail.ERROR_MESSAGE_ORIGINAL_IMAGE_DO_NOT_EXIST);

		return thumbnail;
	}

	/**
	 * Check if the original image exists.
	 * 
	 * @param thumbnail
	 *            the thumbnail
	 * @return true if it exists.
	 */
	protected boolean doTheOriginalImageExist(Thumbnail thumbnail) {
		final File originalFile = new File(thumbnail.getOriginalBaseDir() + File.separator
				+ thumbnail.getOriginalImageNameWithExtension());

		return originalFile.exists();
	}

}
