package com.pep1.kitchenoffice.data.event;

import java.util.Date;

public interface Feedable {

    public String getContent();

    public String getTitle();

    public Date getPubDate();

    public String getLink();
}
