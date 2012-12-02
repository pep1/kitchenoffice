package com.gentics.kitchenoffice.webapp.view.form.field;

import java.io.OutputStream;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;

import com.gentics.kitchenoffice.data.Image;
import com.gentics.kitchenoffice.repository.ImageRepository;
import com.gentics.kitchenoffice.storage.Storage;
import com.gentics.kitchenoffice.storage.file.FileBuffer;
import com.gentics.kitchenoffice.storage.processing.ImageProcessor;
import com.gentics.kitchenoffice.webapp.util.WebappHelper;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.StreamVariable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Html5File;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.DragAndDropWrapper.WrapperTransferable;

@org.springframework.stereotype.Component
@Scope("prototype")
public class ImageField extends CustomField<Image> implements DropHandler {

	private static Logger log = Logger.getLogger(ImageField.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -9220750574762699338L;
	private VerticalLayout vi;
	private Embedded imageEmbed;
	private DragAndDropWrapper wrapper;

	private Image oldImage;

	@Autowired
	private FileBuffer receiver;

	@Autowired
	private ImageProcessor processor;

	@Autowired
	private Storage<Image> storage;

	@Autowired
	private ImageRepository repository;

	@Autowired
	private WebappHelper helper;
	
	private Html5File[] files;
	private Html5File file;

	private ProgressIndicator indicator = new ProgressIndicator();

	public ImageField() {
	}

	private VerticalLayout buildVerticalImage() {
		// common part: create layout
		vi = new VerticalLayout();
		vi.setWidth("160px");
		vi.setMargin(true);

		vi.setImmediate(true);
		vi.setMargin(false);

		indicator.setImmediate(true);
		indicator.setWidth("100%");
		indicator.setVisible(false);
		indicator.setEnabled(false);

		addStyleName("no-horizontal-drag-hints");
		addStyleName("no-vertical-drag-hints");

		imageEmbed = new Embedded();
		imageEmbed.setStyleName("imageposter");
		imageEmbed.setWidth("160px");
		imageEmbed.setHeight("160px");

		wrapper = new DragAndDropWrapper(imageEmbed);
		wrapper.setImmediate(true);
		wrapper.setDropHandler(null);

		addStyleName("no-horizontal-drag-hints");
		addStyleName("no-vertical-drag-hints");
		// dropWrapper.addStyleName("no-box-drag-hints");

		vi.addComponent(wrapper);
		vi.addComponent(indicator);
		vi.setExpandRatio(wrapper, 1.0f);
		vi.setComponentAlignment(wrapper, Alignment.MIDDLE_CENTER);

		return vi;
	}
	
	@PostConstruct
	private void postConstruct() {
		buildVerticalImage();
	}

	@Override
	protected Component initContent() {
		return vi;
	};

	public void attach() {
		super.attach(); // Must call.

		Image image = this.getValue();
		imageEmbed.setSource(helper.getImageThumbnail(image, 160));
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);

		if (readOnly) {

			this.setCaption("");

			indicator.setVisible(false);
			indicator.setEnabled(false);

			if (wrapper != null) {
				wrapper.setDropHandler(null);
			}

		} else {
			indicator.setVisible(true);
			indicator.setEnabled(false);
			this.setCaption("drop new image here");
			if (wrapper != null) {
				wrapper.setDropHandler(this);
			}
		}
	}

	public void drop(DragAndDropEvent event) {
		log.debug("file dropped");

		DragAndDropWrapper.WrapperTransferable transferable = (WrapperTransferable) event
				.getTransferable();

		files = transferable.getFiles();

		if (files == null || files.length != 1) {

			String msg = "Please only one Image File";

			Notification.show(msg);

			return;
		}

		file = files[0];

		indicator.setVisible(true);
		indicator.setEnabled(true);
		indicator.setPollingInterval(1200);

		file.setStreamVariable(new StreamVariable() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -8053189714492444123L;
			private String name;
			private String mime;

			public OutputStream getOutputStream() {

				return receiver.receiveUpload(name, mime);
			}

			public boolean listenProgress() {
				return true;
			}

			public void onProgress(StreamingProgressEvent event) {

				float p = (float) event.getBytesReceived()
						/ (float) event.getContentLength();

				indicator.setValue(p);

			}

			public void streamingStarted(StreamingStartEvent event) {
				name = event.getFileName();
				mime = event.getMimeType();

				log.debug("streaming started");

			}

			public void streamingFinished(StreamingEndEvent event) {

				indicator.setValue(1.0F);

				wrapper.setDropHandler(null);

				try {

					Image image = processor.createImageObject(receiver
							.getFile());

					Image prevImage = getValue();

					if (prevImage != null && prevImage.getFilePath() != null && !"".equals(prevImage.getFilePath())) {
						oldImage = prevImage;
					}

					setValue(image);

				} catch (Exception e) {

					log.error("failed to process image: "
							+ e.getLocalizedMessage());
					e.printStackTrace();

					String msg = "Failed to process Image!";

					Notification.show(msg, Type.ERROR_MESSAGE);
					
					storage.deleteFile(receiver.getFile());

				} finally {
					receiver.reset();
					setReadOnly(true);
					
					indicator.markAsDirty();
				}

			}

			public void streamingFailed(StreamingErrorEvent event) {
				receiver.reset();
			}

			public boolean isInterrupted() {
				return false;
			}

		});

	}

	public AcceptCriterion getAcceptCriterion() {
		return AcceptAll.get();
	}

	@Override
	public Class<? extends Image> getType() {
		return Image.class;
	}

	@Override
	protected void setInternalValue(Image image) {
		super.setInternalValue(image);

		if(getUI() != null) {
			detach();
			attach();
		}
		

	}

	@Override
	public void commit() {
		// save the new image
		if(isModified() && getValue() != null) {
			repository.save(getValue());
			
			// delete the old image
			if (oldImage != null) {
				processor.removeImageObject(oldImage);
			}
			
		}
		
		super.commit();
		
		oldImage = null;
	}
	
	@Override
	public void discard() {
		
		// if there is a new image uploaded
		if(oldImage != null && getValue() != null) {
			// delete the new image
			processor.removeImageObject(getValue());
		}
		
		super.discard();

		detach();
		attach();

		oldImage = null;
	}
	
	@Override
	public boolean isModified() {
		return oldImage != null;
	}


}
