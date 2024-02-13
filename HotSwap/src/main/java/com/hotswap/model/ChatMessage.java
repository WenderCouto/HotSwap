package com.hotswap.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Transient;

public class ChatMessage {
    private String sender;
    private String receiver;
    private String content;
    private String date;

    @Transient
    DateTimeFormatter timeDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy | HH:mm:ss");

    public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = LocalDateTime.now().format(timeDateFormat) + "s";
	}

	public ChatMessage() {}

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
