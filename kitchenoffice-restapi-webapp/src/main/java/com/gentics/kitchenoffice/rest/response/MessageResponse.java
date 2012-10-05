package com.gentics.kitchenoffice.rest.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MessageResponse {

    private String message;

    /**
     * Constructor used by JAXB
     */
    public MessageResponse() {
    }

    /**
     * Create a message response with the given message.
     * 
     * @param message
     */
    public MessageResponse(String message) {
        this.message = message;
    }

    /**
     * Get the message
     * 
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message
     * 
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
