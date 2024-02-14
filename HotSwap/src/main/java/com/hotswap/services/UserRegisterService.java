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

@Service
public class UserRegisterService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	final Logger log = LoggerFactory.getLogger(UserRegisterService.class);

	public String registerLogic(@RequestParam int registNumber, @RequestParam String userName, @RequestParam String password) throws IOException {
		final String registered = "Usuário já possui cadastro.";
		final String permitRegister = "Usuário Possui dois Perfis";
		final String success = "Usuário Cadastrado Com Sucesso!";
		final String error = "Erro ao processar a solicitação.";
		final String notFound = "Usuário não encontrado";
	    try {
	        User existingUser = userRepository.findUserbyId(registNumber);
	        int maxRegistNumber = userRepository.findMaxRegistNumber(); //increase
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

	        	StructureJsonService structureJson = new StructureJsonService(user);
	        	try {
	        	    Path path = Paths.get("src/main/resources/static/JSON/dbHotSwapUsers.json");
	        	    String content = new String(Files.readAllBytes(path));

	        	    if (content.isEmpty()) {
	        	        String json = structureJson.writeJsonUser(user);
	        	        Files.write(path, json.getBytes());
	        	        log.info("Arquivo JSON criado com sucesso!");
	        	    } else {
	        	        StringBuilder existingJson = new StringBuilder(content);
	        	        String newUser = structureJson.addNewUserObj(existingJson, user);
	        	        Files.write(path, newUser.getBytes());
	        	        log.info("Arquivo JSON de usuários atualizado com sucesso!");
	        	    }

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
