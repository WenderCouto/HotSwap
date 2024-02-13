package com.hotswap.controller;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hotswap.repository.UserRepository;
import com.hotswap.services.ImageExtensionPathService;

@RestController
@RequestMapping("custom/api/imagem")
public class UploadProfileImgController {

	@Autowired
	UserRepository userRepository;
	@Autowired
	ImageExtensionPathService imgServe;

	@RequestMapping(value = "/upload", method = RequestMethod.PUT) //Vou precisar do front-end
	public ResponseEntity<String> putUserImagem(@RequestParam("file") MultipartFile file, int registnumber, String roles) throws FileNotFoundException {

		if (file.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Arquivo não existente ou corrompido.");
		}
		imgServe.processarImagem(file, registnumber, roles);
		userRepository.findUserbyId(registnumber);
		return null;
	}

    @RequestMapping(value = "/return", method = RequestMethod.GET) //Aqui devo passar o token também.
    public ResponseEntity<Resource> getUserImagem(Integer registNumber ) throws FileNotFoundException {
        return imgServe.getUserImagem(registNumber);
    }
}
