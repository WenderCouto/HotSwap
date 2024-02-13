package com.hotswap.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.hotswap.model.User;

@Repository
public class UserRepository {

    public String userJsonDbDir = "src/main/resources/static/JSON/dbHotSwapUsers.json";

    public User findUserbyId(int registernumber) throws FileNotFoundException {
        try {
            File file = new File(userJsonDbDir);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String linha;
            User user = null;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().startsWith("\"Número de Registro\":")) {
                    String numeroRegistro = linha.split(":")[1].trim();
                    numeroRegistro = numeroRegistro.substring(0, numeroRegistro.length() - 1); // remove a vírgula
                    if (numeroRegistro.equals(Integer.toString(registernumber))) {
                        user = new User();
                        user.setRegistNumber(Integer.parseInt(numeroRegistro));
                    }
                }
                if (user != null) {
                    if (linha.trim().startsWith("\"Nome de Usuário\":")) {
                        String userName = linha.split(":")[1].trim();
                        userName = userName.substring(1, userName.length() - 2); // vírgula e Aspas
                        user.setUserName(userName);
                    }
                    if (linha.trim().startsWith("\"Credenciais\":")) {
                        String password = linha.split(":")[1].trim();
                        password = password.substring(1, password.length() - 2);
                        user.setPassword(password);
                    }
                }
            }
            //br.close();
            return user;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int findMaxRegistNumber() throws IOException {
        int maxRegistNumber = 0;
        File file = new File(userJsonDbDir);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().startsWith("\"Número de Registro\":")) {
                String numeroRegistro = line.split(":")[1].trim();
                numeroRegistro = numeroRegistro.substring(0, numeroRegistro.length() - 1);
                int registNumber = Integer.parseInt(numeroRegistro);
                if (registNumber > maxRegistNumber) {
                    maxRegistNumber = registNumber;
                }
            }
        }
        //br.close();
        return maxRegistNumber;
    }

    public String findUserByCredentials(@RequestParam String username, @RequestParam String password) {
        try {
            File file = new File(userJsonDbDir);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().startsWith("\"Nome de Usuário\":")) {
                    String userNameMatcher = linha.split(":")[1].trim();
                    userNameMatcher = userNameMatcher.substring(1, userNameMatcher.length() - 2);
                    if (username.equals(userNameMatcher)) {
                        // se match no user, continue verificando a senha
                        linha = br.readLine();
                        if (linha.trim().startsWith("\"Credenciais\":")) {
                            String passwordMatcher = linha.split(":")[1].trim();
                            passwordMatcher = passwordMatcher.substring(1, passwordMatcher.length() - 2);
                            if (password.equals(passwordMatcher)) {
                                return "Usuário encontrado";
                            }
                        }
                    }
                }
                //br.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Usuário ou senha Inválidos";
    }

    public boolean findDuplicated(@RequestParam String username, @RequestParam String password) {
        try {
            File file = new File(userJsonDbDir);
            Set<String> userCredentials = new HashSet<>();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().startsWith("\"Nome de Usuário\":")) {
                    String userNameMatcher = linha.split(":")[1].trim();
                    userNameMatcher = userNameMatcher.substring(1, userNameMatcher.length() - 2);
                    linha = br.readLine();
                    if (linha.trim().startsWith("\"Credenciais\":")) {
                        String passwordMatcher = linha.split(":")[1].trim();
                        passwordMatcher = passwordMatcher.substring(1, passwordMatcher.length() - 2);
                        String userCredentialsKey = userNameMatcher + "|" + passwordMatcher;
                        // Verifica se as credenciais recebidas pelo endpoint já existem
                        String newUserCredentialsKey = username + "|" + password;
                        if (userCredentialsKey.equals(newUserCredentialsKey)) {
                            return true;
                        }
                        userCredentials.add(userCredentialsKey);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public String findImageFullPathByRegNumber(int registnumber) { //elses visiveis no logger
        try {
            File file = new File(userJsonDbDir);
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                String linha = scan.nextLine().trim();
                if (linha.startsWith("\"Número de Registro\":")) {
                    String numeroRegistro = linha.split(":")[1].trim();
                    numeroRegistro = numeroRegistro.substring(0, numeroRegistro.length() - 1);
                    if (Integer.parseInt(numeroRegistro) == registnumber) {
                        while (scan.hasNextLine()) {
                            linha = scan.nextLine().trim();
                            if (linha.startsWith("\"Caminho de Imagem\":")) {
                                String imageFullPath = linha.split(":")[1].trim();
                                imageFullPath = imageFullPath.substring(1, imageFullPath.length() - 2);
                                return imageFullPath;
                            }
                        }
                        return "Não foi possivel identificar o Parametro: \"Caminho de Imagem\"";
                    }
                }
                //scan.close();
            }
            return "Não foi possivel identificar o Parametro: \"Número de Registro\"";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}