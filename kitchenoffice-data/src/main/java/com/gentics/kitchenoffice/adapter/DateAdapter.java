package com.gentics.kitchenoffice.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class DateAdapter extends XmlAdapter<String, Date> {

	public Date unmarshal(String v) throws Exception {
		DateTime dateTime = new DateTime(v);
		return dateTime.toDate();
	}

	public String marshal(Date v) throws Exception {
		DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
		return fmt.print(new DateTime(v));
	}

}
