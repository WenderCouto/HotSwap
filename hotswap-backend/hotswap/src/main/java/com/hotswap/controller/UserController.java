package com.hotswap.controller;

import com.hotswap.repository.UserRepository;
import com.hotswap.services.TokenService;
import com.hotswap.services.UpdateUserDataService;
import com.hotswap.services.UserObjectDataService;
import com.hotswap.services.UserRegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("custom/api/user")
public class UserController {
    final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRegisterService registerService;
    @Autowired
    private UpdateUserDataService updateUserDataService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    UserObjectDataService userObjectDataService;

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestParam String username, @RequestParam String password) throws FileNotFoundException {
        final String successMessage = "Usuário encontrado";
        if(username.isEmpty() || password.isEmpty() || username.isBlank() || password.isBlank()) {
            return new ResponseEntity<>("Opa, parece que alguns dados não estão presentes ou estão incorretos.", HttpStatus.BAD_REQUEST);
        }
        String resultado = userRepository.findUserByCredentials(username, password);
        var token = tokenService.generateToken(Integer.parseInt(resultado));
        return ResponseEntity.ok("Credenciais Válidas." + "\n" + token);
    }

    @PostMapping("/register")
    public ResponseEntity<String> userRegister(Integer registnumber, @RequestParam String username, @RequestParam String password) throws IOException {
        final String permitRegister = "Não é Permitido Usuários Clonados.\n"
                + "Permitido Apenas Dois Usúarios Com Credenciais Idênticas.";
        final String hasTwoProfiles = "Usuário Possui dois Perfis";
        final String successMessage = "Usuário Cadastrado Com Sucesso!";
        final String registered = "Usuário já possui cadastro.";
        final String notFound = "Usuário não encontrado";
        final String error = "Erro ao processar a solicitação.";

        if(registnumber == null){
            ResponseEntity.badRequest().body("Algo deu errado.");
        }
        String resultado = registerService.registerLogic(registnumber, username, password);
        switch (resultado) {
            case permitRegister:
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(permitRegister);
            case hasTwoProfiles:
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(hasTwoProfiles);
            case successMessage:
                return ResponseEntity.status(HttpStatus.OK).body(successMessage);
            case registered:
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(registered);
            case notFound:
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFound);
            case error:
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Algo inesperado aconteceu.");
    }

    @PutMapping("/exchange")
    public ResponseEntity<String> updateJsonUser(@RequestParam Integer registnumber, String username, String status) throws IOException {
        if(registnumber == null){
            ResponseEntity.badRequest().body("Algo deu errado.");
        }
        boolean resultado = updateUserDataService.updateUserDataHandler(registnumber, username, status);
        if(!resultado){
            return ResponseEntity.badRequest().body("Parece que tivemos um problema.");
        }
        return ResponseEntity.ok().body("Atualização Realizada com Sucesso!");
    }
}
