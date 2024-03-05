package com.hotswap.model;

import jakarta.persistence.Transient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatMessage {
    private String sender;
    private int receiver;
    private String receiverName;
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

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
