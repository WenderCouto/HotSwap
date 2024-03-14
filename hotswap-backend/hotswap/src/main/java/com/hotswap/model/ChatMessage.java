package com.hotswap.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatMessage {

    @NotEmpty
    @JsonProperty("Número de Registro")
    private int registerNumber;
    @NotEmpty
    @JsonProperty("Transmissor")
    private String transmissor;
    @NotEmpty
    @JsonProperty("Nome do Receptor")
    private String receiverName;
    @NotEmpty
    @JsonProperty("Binding")
    private String binding;
    @JsonProperty("Mensagem")
    private String content;
    @NotEmpty
    @JsonProperty("Data")
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

    public String getTransmissor() {
        return transmissor;
    }

    public void setTransmissor(String transmissor) {
        this.transmissor = transmissor;
    }

    public int getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(int registerNumber) {
        this.registerNumber = registerNumber;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getBinding(){
        return binding;
    }
    public void setBinding(String binding) {
        this.binding = binding;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "\n" +
                "ChatMessage{" + "\n" +
                registerNumber + "\n" +
                transmissor + "\n" +
                receiverName + "\n" +
                content + "\n" +
                date + "\n" +
                binding + "\n" +
                '}';
    }

}
