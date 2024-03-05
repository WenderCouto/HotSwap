package com.hotswap.controller;

import com.hotswap.model.User;
import com.hotswap.repository.UserRepository;
import com.hotswap.services.MessageWritingService;
import com.hotswap.services.UserObjectDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("custom/api/shoot")
public class MessagingController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageWritingService run;
    @Autowired
    UserObjectDataService userObjectDataService;

    @PostMapping("/enviar")
    public ResponseEntity<?> sendMessage(@RequestParam Integer registnumber, String username, @RequestParam Integer receiver, String message) throws IOException {
        if(registnumber == null || receiver == null) {
            return ResponseEntity.badRequest().body("Número de registro ou Número do receptor ausente(s).");
        }
        User existingReceiver = userRepository.findUserbyId(receiver);
        if(existingReceiver == null){
            return ResponseEntity.badRequest().body("Receptor Inexistente.");
        }
        run.messageWriter(registnumber, username, receiver, message);
        return ResponseEntity.ok("Menssagem Enviada.");
    }
}
