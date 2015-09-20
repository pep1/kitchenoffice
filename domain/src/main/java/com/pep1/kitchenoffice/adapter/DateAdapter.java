package com.pep1.kitchenoffice.adapter;

import org.joda.time.DateTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAdapter extends XmlAdapter<String, Date> {

    // Strict ISO 8601 date format with UTC offset
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    public Date unmarshal(String v) throws Exception {
        DateTime dateTime = new DateTime(v);
        return dateTime.toDate();
    }

    public String marshal(Date v) throws Exception {
        return dateFormat.format(v);
    }

}
