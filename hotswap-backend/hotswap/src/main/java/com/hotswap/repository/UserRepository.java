package com.hotswap.repository;

import com.hotswap.DTO.UserResult;
import com.hotswap.model.User;
import com.hotswap.services.UserObjectDataService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepository {

    public String userJsonDbDir = "src/main/resources/static/JSON/dbHotSwapUsers.json";

    private final UserObjectDataService userObjectDataService;

    public UserRepository(UserObjectDataService userObjectDataService) {
        this.userObjectDataService = userObjectDataService;
    }

    public UserResult getUserObjectById(int registNumber) {
        UserObjectDataService userObjectDataService1 = new UserObjectDataService();
        ConcurrentHashMap<Integer, User> usersMap = userObjectDataService1.getUsers();
        UserResult result = new UserResult();
        if (usersMap != null) {
            result.setUser(usersMap.get(registNumber));
        }
        result.setRegistNumber(registNumber);
        return result;
    }

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
            String registro = null;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().startsWith("\"Número de Registro\":")) {
                    registro = linha.split(":")[1].trim();
                    registro = registro.replace(",", "").trim();
                    registro = (registro.length() >= 3) ? registro.substring(1, registro.length() - 2) : registro;
                } else if (linha.trim().startsWith("\"Nome de Usuário\":")) {
                    String userNameMatcher = linha.split(":")[1].trim();
                    userNameMatcher = userNameMatcher.substring(1, userNameMatcher.length() - 2);
                    if (username.equals(userNameMatcher)) {
                        // se match no user, continue verificando a senha
                        linha = br.readLine();
                        if (linha.trim().startsWith("\"Credenciais\":")) {
                            String passwordHash = linha.split(":")[1].trim();
                            passwordHash = passwordHash.substring(1, passwordHash.length() - 2);
                            if (BCrypt.checkpw(password, passwordHash)) {
                                return registro;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Usuário ou senha Inválidos";
    }

    public boolean findDuplicated(@RequestParam String username, @RequestParam String password) {
        int count = 0;
        try {
            File file = new File(userJsonDbDir);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().startsWith("\"Nome de Usuário\":")) {
                    String userNameMatcher = linha.split(":")[1].trim();
                    userNameMatcher = userNameMatcher.substring(1, userNameMatcher.length() - 2);
                    if (userNameMatcher.equals(username)) {
                        linha = br.readLine();
                        if (linha.trim().startsWith("\"Credenciais\":")) {
                            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                            String storedPassword = linha.split(":")[1].trim();
                            storedPassword = storedPassword.substring(1, storedPassword.length() - 2);
                            if (passwordEncoder.matches(password, storedPassword)) {
                                count++;
                                if (count >= 1) {
                                    return true;
                                }
                            }
                        }
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