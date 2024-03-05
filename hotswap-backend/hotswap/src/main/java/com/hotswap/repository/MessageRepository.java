package com.hotswap.repository;

import com.hotswap.model.ChatMessage;
import com.hotswap.services.StructureJsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Repository
public class MessageRepository {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StructureJsonService structureJsonService;

    public String userJsonDbDir = "src/main/resources/static/JSON/dbHotSwapChat.json";

    public ChatMessage findMessageBindbyId(int numeroRegistro, int receptor, String message) throws IOException {
        Path path = Paths.get(userJsonDbDir);
        String content = new String(Files.readAllBytes(path));
        ChatMessage chatMessage = null;

        int indexRegistro = content.indexOf("\"Número de Registro\": " + numeroRegistro);
        if (indexRegistro != -1) {
            int indexReceptor = content.indexOf("\"Identificador Único\": " + receptor, indexRegistro);
            if (indexReceptor != -1) {
                int indexConteudo = content.indexOf("\"Conteúdo\": [", indexReceptor);
                if (indexConteudo != -1) {
                    int indexFimConteudo = content.lastIndexOf("}", content.indexOf("]", indexConteudo));
                    if (indexFimConteudo != -1) {
                        chatMessage = new ChatMessage();
                        chatMessage.setContent(message);
                        chatMessage.setDate(LocalDateTime.now().toString());
                        chatMessage = structureJsonService.toJsonMobj(chatMessage);
                        String newMessage = ",\n" + chatMessage.getContent();
                        content = content.substring(0, indexFimConteudo + 1) + newMessage + content.substring(indexFimConteudo + 1);
                        StringBuilder sb = new StringBuilder();
                        for (String line : content.split("\n")) {
                            if (!line.trim().isEmpty()) {
                                sb.append(line).append("\n");
                            }
                        }
                        content = sb.toString();
                    }
                }
            }
        }

        Files.write(path, content.getBytes());
        return chatMessage;
    }
}
