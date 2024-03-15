package com.hotswap.controller;

import com.hotswap.services.ImageExtensionPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.Map;

@RestController
@RequestMapping("custom/api/imagem")
public class ProfileImgController {

	@Autowired
	private ImageExtensionPathService imgServe;

	@RequestMapping(value = "/upload", method = RequestMethod.PUT)
	public ResponseEntity<?> putUserImagem(@RequestParam("file") MultipartFile file, Integer registnumber, String roles) throws FileNotFoundException {
		if (registnumber == null) { //Objects.isNull(registnumber)  menos um import.
			return new ResponseEntity<>("Opa, parece que seu número de registro único não está presente.", HttpStatus.BAD_REQUEST);
		}
		if (file.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Arquivo não existente ou corrompido.");
		}
		imgServe.processarImagem(file, registnumber, roles);

		return ResponseEntity.ok("Arquivo processado com sucesso");
	}

	@RequestMapping(value = "/return", method = RequestMethod.GET)
	public ResponseEntity<?> getUserImagemWrapper(@RequestBody Map<String, String> payload) {
		if(payload.get("registnumber") == null){
			return new ResponseEntity<>("Opa, Dados Incorretos.", HttpStatus.BAD_REQUEST);
		}
		int registnumber = Integer.parseInt(payload.get("registnumber"));
		try {
			return imgServe.getUserImagem(registnumber);
		} catch (FileNotFoundException e) {
			return new ResponseEntity<>("Arquivo não encontrado", HttpStatus.NOT_FOUND);
		}
	}
}
