package com.hotswap.services;

import com.hotswap.model.ChatMessage;
import com.hotswap.model.User;
import com.hotswap.repository.MessageRepository;
import com.hotswap.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MessageWritingService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserObjectDataService userObjectDataService;
    @Autowired
    private MessageRepository messageRepository;

    private final Logger log = LoggerFactory.getLogger(MessageWritingService.class);

    private String userJsonDbDir = "src/main/resources/static/JSON/dbHotSwapChat.json";

    public String messageWriter(@RequestParam Integer registNumber, @RequestParam String userName, @RequestParam Integer receiver, String message) throws IOException { // @RequestParam String receiver
        final String successMessage = "Menssagem enviada com Sucesso!";
        final String errorMessage = "A Menssagem não pode ser enviada!";
        int maxRegistNumber = userRepository.findMaxRegistNumber();
        User existingUser = userRepository.findUserbyId(registNumber);
        User existingReceiver = userRepository.getUserObjectById(receiver);
        User user = new User();

        ChatMessage chatStructure = new ChatMessage();

        user.setRegistNumber(maxRegistNumber);
        chatStructure.setTransmissor(userName);
        if(existingUser == null && registNumber.equals(receiver)){
            chatStructure.setRegisterNumber(maxRegistNumber);
            chatStructure.setReceiverName(userName);
            chatStructure.setContent(message);
            chatStructure.setDate(LocalDateTime.now().toString());
        }
        else if(registNumber.equals(receiver)){
            chatStructure.setContent(message);
            chatStructure.setReceiverName(existingUser.getUserName());
            chatStructure.setDate(LocalDateTime.now().toString());
        }
        else {
            chatStructure.setRegisterNumber(receiver);
            chatStructure.setContent(message);
            chatStructure.setReceiverName(existingReceiver.getUserName());
            chatStructure.setDate(LocalDateTime.now().toString());
        }

        Map<Integer, User> users = new ConcurrentHashMap<>();
        users.put((existingUser == null) ? registNumber : existingUser.getRegistNumber(), (existingUser == null) ? user : existingUser);
        StructureJsonService structureJson = new StructureJsonService((existingUser == null) ? user : existingUser, chatStructure);
        try {
            Path path = Paths.get(userJsonDbDir);
            String content = new String(Files.readAllBytes(path));

            if (content.isEmpty()) {
                String json = structureJson.writeJsonMessage((existingUser == null) ? user : existingUser, chatStructure);
                Files.write(path, json.getBytes());
                log.info("Arquivo JSON HotswapChat criado com sucesso!");
            }
            else if (existingUser != null && registNumber.equals(receiver)) {
                ChatMessage chatMessage = messageRepository.findMessageBindbyId(registNumber, receiver, message);
                log.info("Arquivo JSON de Estrutura de Chat atualizado com sucesso!");
            }else {
                StringBuilder existingJson = new StringBuilder(content);
                String addNewObjectUserToChatMessage = structureJson.toJsonM(chatStructure);
                String addNewMessageObToExistingDoc = structureJson.addNewMessageObj(existingJson, addNewObjectUserToChatMessage);
                Files.write(path, addNewMessageObToExistingDoc.getBytes());
                log.info("Arquivo JSON de Estrutura Nova de Chat atualizado com sucesso!");
            }
            return successMessage;
        } catch (IOException e) {
            e.printStackTrace();
            return errorMessage;
        }
    }
}
