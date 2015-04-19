package com.trangpig.model;

import java.util.Date;

public class MessageChat {
	private long idMes;
	private long idSender;
	private String fromName;
	private String text;
	private Date date;

	public MessageChat() {
	}

	public MessageChat(long idMes, long idSender, String fromName, String text,
			Date date) {
		super();
		this.idMes = idMes;
		this.idSender = idSender;
		this.fromName = fromName;
		this.text = text;
		this.date = date;
	}

	public long getIdMes() {
		return idMes;
	}

	public void setIdMes(long idMes) {
		this.idMes = idMes;
	}

	public long getIdSender() {
		return idSender;
	}

	public void setIdSender(long idSender) {
		this.idSender = idSender;
	}

	public String getfromName() {
		return fromName;
	}

	public void setfromName(String fromName) {
		this.fromName = fromName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
