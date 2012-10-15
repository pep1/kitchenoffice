package com.gentics.kitchenoffice.webapp.view.form.field;

import java.awt.image.BufferedImage;
import java.io.OutputStream;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Scope;

import com.gentics.kitchenoffice.data.Image;
import com.gentics.kitchenoffice.storage.Storage;
import com.gentics.kitchenoffice.storage.file.FileBuffer;
import com.gentics.kitchenoffice.storage.file.ImageProcessor;
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
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.DragAndDropWrapper.WrapperTransferable;

@org.springframework.stereotype.Component
@Scope("prototype")
public class ImageField extends CustomField<Image> implements DropHandler{
	
	private static Logger log = Logger.getLogger(ImageField.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -9220750574762699338L;
	private VerticalLayout vi;
	private Embedded imageEmbed;
	private DragAndDropWrapper wrapper;
	
	@Autowired
	private FileBuffer receiver;
	
	@Autowired
	private ImageProcessor processor;
	
	@Autowired
	private Storage<Image> storage;
	
	@Value("${webapp.imagepath}")
	private String imagePath;
	
	@Value("${storage.imagepath}")
	private String imagePathToStore;
	
	private ProgressIndicator indicator = new ProgressIndicator();
	
	public ImageField(){
		
	}
	
	private VerticalLayout buildVerticalImage() {
		// common part: create layout
		vi = new VerticalLayout();
		vi.setWidth("120px");
		
		vi.setImmediate(true);
		vi.setMargin(false);
		
		indicator.setImmediate(true);
		indicator.setPollingInterval(500);
		
		indicator.setWidth("100%");
		indicator.setVisible(false);
		indicator.setEnabled(false);
		
		addStyleName("no-horizontal-drag-hints");
		addStyleName("no-vertical-drag-hints");
		
		
		
		imageEmbed = new Embedded();
		imageEmbed.setWidth("120px");
		imageEmbed.setHeight("120px");
		
		wrapper = new DragAndDropWrapper(imageEmbed);
		wrapper.setDropHandler(null);
		
		
		
		
		addStyleName("no-horizontal-drag-hints");
		addStyleName("no-vertical-drag-hints");
		//dropWrapper.addStyleName("no-box-drag-hints");
		
		vi.addComponent(wrapper);
		vi.addComponent(indicator);
		vi.setExpandRatio(wrapper, 1.0f);
		vi.setComponentAlignment(wrapper, Alignment.MIDDLE_CENTER);
		
		
		
		return vi;
	}
	
	@Override
	protected Component initContent() {
		
		return buildVerticalImage();
	};
	
	
	public void attach() {
        super.attach(); // Must call.
        
        Image image = this.getInternalValue();
       
        String url;
        if(image == null) {
        	url = imagePath + "no_image.png";
        } else {
        	url = imagePath + image.getFileName();
        }
        
        ExternalResource icon = new ExternalResource(url);
		imageEmbed.setSource(icon);
    }
	
	  
	
	@Override
	public void setReadOnly(boolean readOnly){
		super.setReadOnly(readOnly);
		
		if(readOnly){
			
			
			this.setCaption(null);
			indicator.setVisible(false);
			if(wrapper != null){
				wrapper.setDropHandler(null);
			}
			
		} else {
			indicator.setVisible(true);
			this.setCaption("drop new image here");
			if(wrapper != null) {
				wrapper.setDropHandler(this);
			}
		}
	}
	

	
	@Override
	public void discard(){
		super.discard();
		
		//detach();
		//attach();
	}

	public void drop(DragAndDropEvent event) {
		log.debug("dropped");
		
		DragAndDropWrapper.WrapperTransferable transferable = (WrapperTransferable) event
		.getTransferable();
		
	
		
		Html5File[] files = transferable.getFiles();
		
		log.debug(files[0].getType());
		
		if(files == null || files.length != 1){
			
			String msg = "Please only one Image File";
			
			return;
		}
		
		final Html5File file = files[0];
		
		
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
				
				float p = (float) event.getBytesReceived() / (float) event.getContentLength();
				
				indicator.setValue(p);
				
			}

			public void streamingStarted(StreamingStartEvent event) {
				name = event.getFileName();
				mime = event.getMimeType();
				
				
				log.debug("streaming started");
				
				indicator.setVisible(true);
				indicator.setEnabled(true);
				indicator.setPollingInterval(500);
			}
			
			public void streamingFinished(StreamingEndEvent event) {
				//SessionHandler.getApplication().getMainWindow().showNotification(file.getFileName());
				
				indicator.setVisible(false);
				indicator.setEnabled(false);
				wrapper.setDropHandler(null);
				
				try {
					
					setValue(processor.createImageObject(receiver.getFile()));
					
				} catch (Exception e) {
					
					log.error("failed to process image: " + e.getLocalizedMessage());
					e.printStackTrace();
					
					String msg = "FileType is not supported!";
					
					Notification.show(msg, Notification.TYPE_ERROR_MESSAGE);
					storage.deleteFile(receiver.getFile());
					
				}
				
				
				
			}
			
			public void streamingFailed(StreamingErrorEvent event) {
				//uploadQueue.removeComponent(l);
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
		// TODO Auto-generated method stub
		return Image.class;
	}
	
}
