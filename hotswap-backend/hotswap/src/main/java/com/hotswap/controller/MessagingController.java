package com.hotswap.controller;

import com.hotswap.model.User;
import com.hotswap.repository.UserRepository;
import com.hotswap.services.MessageObjectDataService;
import com.hotswap.services.MessageWritingService;
import com.hotswap.services.UserObjectDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("custom/api/shoot")
public class MessagingController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageWritingService run;
    @Autowired
    UserObjectDataService userObjectDataService;
    @Autowired
    MessageObjectDataService messageObjectDataService;

    @PostMapping("/enviar")
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, String> payload) throws IOException {
        if(payload.get("registnumber") == null || payload.get("receiver") == null || payload.get("registnumber").isEmpty() || payload.get("receiver").isEmpty()){
            return ResponseEntity.badRequest().body("Preencha todos os dados corretamente.");
        }
        int registnumber = Integer.parseInt(payload.get("registnumber")), receiver = Integer.parseInt(payload.get("receiver"));
        String username = payload.get("username"), message = payload.get("message");
        User existingReceiver = userRepository.findUserbyId(receiver);
        if(existingReceiver == null){
            return ResponseEntity.badRequest().body("Receptor Inexistente.");
        }
        run.messageWriter(registnumber, username, receiver, message);
        return ResponseEntity.ok("Menssagem Enviada.");
    }
}
