package com.hotswap.services;

import com.hotswap.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserObjectDataService {
    final Logger log = LoggerFactory.getLogger(UserObjectDataService.class);

    UserObjectDataService() {
        reloadUserObject();
        printUsers();
    }

    private ConcurrentHashMap<String, User> users;

    public void printUsers() {
        for (Map.Entry<String, User> entry : users.entrySet()) {
            System.out.println("User Object - " + "Número de matricula: " + entry.getKey());
        }
    }

    public void reloadUserObject() {
        users = new ConcurrentHashMap<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get("src/main/resources/static/JSON/dbHotSwapUsers.json")));
            content = content.substring(2, content.length()-2);
            String[] userParts = content.split("\\},\\s*\\{");
            for (int i = 0; i < userParts.length; i++) {
                String userPart = userParts[i];
                if (i == 0) {
                    userPart = "\n" + userPart;
                } if(i != 0) {
                    userPart = "\n" + "  {" + userPart;
                }
                if (i != userParts.length - 1) {
                    userPart = userPart + "}\n";
                }
                String[] parts = userPart.split(",");
                if (parts.length < 9) {
                    continue;
                }
                //System.out.println( Arrays.toString(parts)); // Viewing format
                User user = new User();
                user.setRegistNumber(Integer.parseInt(parts[0].split(":")[1].trim()));
                user.setUserName(parts[1].split(":")[1].trim().replace("\"", ""));
                user.setPassword(parts[2].split(":")[1].trim().replace("\"", ""));
                user.setRoles(parts[3].split(":")[1].trim().replace("\"", ""));
                user.setCreatedDate(parts[4].split(":")[1].trim().replace("\"", ""));
                user.setPathImage(parts[5].split(":")[1].trim().replace("\"", ""));
                user.setFotoPerfil(parts[6].split(":")[1].trim().replace("\"", ""));
                user.setStatus(parts[7].split(":")[1].trim().replace("\"", ""));
                user.isResetPassword(Boolean.parseBoolean(parts[8].split(":")[1].trim()));
                users.put(String.valueOf(user.getRegistNumber()), user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
