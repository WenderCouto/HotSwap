package com.hotswap.services;

import com.hotswap.model.User;
import com.hotswap.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserRegisterService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	MessageWritingService messageWritingService;
	@Autowired
	UserObjectDataService userObjectDataService;

	final Logger log = LoggerFactory.getLogger(UserRegisterService.class);

	private String userJsonDbDir = "src/main/resources/static/JSON/dbHotSwapUsers.json";

	public String registerLogic(@RequestParam int registNumber, @RequestParam String userName, @RequestParam String password) throws IOException {
		final String registered = "Usuário já possui cadastro.";
		final String permitRegister = "Usuário Possui dois Perfis";
		final String success = "Usuário Cadastrado Com Sucesso!";
		final String error = "Erro ao processar a solicitação.";
		final String notFound = "Usuário não encontrado";
	    try {
	        User existingUser = userRepository.getUserObjectById(registNumber).getUser();
	        int maxRegistNumber = userRepository.findMaxRegistNumber();
	        boolean isDuplicated = userRepository.findDuplicated(userName, password);
	        if (existingUser != null) {
	            return registered;
	        }
			if(isDuplicated) {
				return permitRegister;
			}
	        else {
	    		User user = new User();

				user.setRegistNumber(maxRegistNumber + 1);
				user.setUserName(userName);
	        	user.setPassword(passwordEncoder.encode(password));
	        	user.setRoles("user");
				user.setStatus("Olá, Eu me chamo: " + user.getUserName());
	        	user.setCreatedDate(LocalDateTime.now().toString());
				Map<Integer, User> users = new ConcurrentHashMap<>();
				users.put(user.getRegistNumber(), user);
				StructureJsonService structureJson = new StructureJsonService(user);
	        	try {
	        	    Path path = Paths.get(userJsonDbDir);
	        	    String content = new String(Files.readAllBytes(path));

	        	    if (content.isEmpty()) {
	        	        String json = structureJson.writeJsonUser(user);
	        	        Files.write(path, json.getBytes());
						log.info("Arquivo JSON HotswapUSer criado com sucesso!");
	        	    } else {
	        	        StringBuilder existingJson = new StringBuilder(content);
	        	        String newUser = structureJson.addNewUserObj(existingJson, user);
	        	        Files.write(path, newUser.getBytes());
	        	        log.info("Arquivo JSON de usuários atualizado com sucesso!");
	        	    }
					String message = "";
					messageWritingService.messageWriter(registNumber, userName, registNumber, message);
					return success;
	        	} catch (IOException e) {
	        	    e.printStackTrace();
	        	    return error;
	        	}

	        }
	    } catch (FileNotFoundException e) {
	        return notFound;
	    }
	}
}
