package com.hotswap.services;

import com.hotswap.model.User;
import com.hotswap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

@Service
public class UpdateUserDataService {
    @Autowired
    UserRepository userRepository;

    private String userJsonDbDir = "src/main/resources/static/JSON/dbHotSwapUsers.json";

    public boolean updateUserDataHandler(@RequestParam int registnumber, @RequestParam String username, @RequestParam String status) throws FileNotFoundException {
        User user = userRepository.getUserObjectById(registnumber).getUser();
        if (user == null) {

        }
        File file = new File(userJsonDbDir);
        StringBuilder fileContent = new StringBuilder();
        Scanner scan = new Scanner(file);
        while (scan.hasNextLine()) {
            String linha = scan.nextLine();
            if (linha.contains("\"Número de Registro\": " + registnumber + ",")) {
                fileContent.append(linha).append("\n");
                while (scan.hasNextLine()) {
                    linha = scan.nextLine();
                    if (linha.contains("\"Nome de Usuário\":")) {
                        linha = "    \"Nome de Usuário\": \"" + username + "\",";
                    } else if (linha.contains("\"Recado\":")) {
                        linha = "    \"Recado\": \"" + status + "\",";
                    }
                    fileContent.append(linha).append("\n");
                    if (linha.contains("}")) {
                        break;
                    }
                }
            } else {
                fileContent.append(linha).append("\n");
            }
        }
        PrintWriter out = new PrintWriter(file);
        out.write(fileContent.toString());
        out.close();
        return true;
    }
}
