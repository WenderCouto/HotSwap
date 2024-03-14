package com.hotswap.services;

import com.hotswap.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MessageObjectDataService {

    final Logger log = LoggerFactory.getLogger(MessageObjectDataService.class);
    private final String userJsonDbDir = "src/main/resources/static/JSON/dbHotSwapChat.json";
    private ConcurrentHashMap<String, ChatMessage> chatsMessage;

    public MessageObjectDataService(){
        chatsMessage = new ConcurrentHashMap<>();
        try {
            Path filePath = Paths.get(userJsonDbDir);
            if (Files.size(filePath) == 0) {
                log.warn("O arquivo dbHotSwapChat.json está vazio");
            } else{
                reloadChatMessageObject();
                printChatsStructure();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public ConcurrentHashMap<String, ChatMessage> getChats() {
        return this.chatsMessage;
    } //Security Purposes -- Future

    private void printChatsStructure() {
        for (Map.Entry<String, ChatMessage> entry : chatsMessage.entrySet()) {
            System.out.println("Chat Object - " + "Binding: " + entry.getKey());
        }
    }

    private void reloadChatMessageObject(){
        chatsMessage = new ConcurrentHashMap<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(userJsonDbDir)));
            StringBuilder sb = new StringBuilder();
            int braceCount = 0;
            for (char c : content.toCharArray()) {
                if (c == '{') {
                    braceCount++;
                }
                if (braceCount > 0) {
                    sb.append(c);
                }
                if (c == '}') {
                    braceCount--;
                    if (braceCount == 0) {
                        String chatObject = sb.toString();
                        sb = new StringBuilder();
                        String[] parts = chatObject.split(",");
                        if (parts.length < 3) {
                            continue;
                        }
                        //System.out.println(Arrays.toString(parts)); // Viewing format
                        ChatMessage chatMessage = new ChatMessage();
                        chatMessage.setRegisterNumber(Integer.parseInt(parts[0].split(":")[1].trim().replace("\"", "")));
                        chatMessage.setTransmissor(parts[1].split(":")[1].trim().replace("\"", ""));
                        chatMessage.setReceiverName(parts[3].split(":")[1].trim().replace("\"", ""));

                        for (String part : parts) {
                            if (part.contains("Mensagem")) {
                                String message = part.substring(part.lastIndexOf(":") + 1).trim();
                                if (!message.equals("\"\"")) {
                                    message = message.replace("\"", "");
                                }
                                chatMessage.setContent(message);
                            } else if (part.contains("Data")) {
                                chatMessage.setDate(part.split(":")[1].trim().replace("\"", ""));
                            } else if (part.contains("Binding")) {
                                String[] splitParts = part.split(":");
                                String numbersOnly = splitParts[2].replaceAll("[^0-9]", "").trim();
                                String result = splitParts[1].trim() + ":" + numbersOnly;
                                result = result.replaceFirst("\"", "");
                                chatMessage.setBinding(result);
                            }
                        }
                        chatsMessage.put(chatMessage.getBinding(), chatMessage);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}