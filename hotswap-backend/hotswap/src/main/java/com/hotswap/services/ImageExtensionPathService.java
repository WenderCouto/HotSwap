package com.hotswap.services;

import com.hotswap.model.User;
import com.hotswap.repository.UserRepository;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

@Service
public class ImageExtensionPathService {
	@Autowired
	UserObjectDataService userObjectDataService;

	final Logger log = LoggerFactory.getLogger(ImageExtensionPathService.class);

	private String domainName = "http://localhost:8080/";

	private String userJsonDbDir = "src/main/resources/static/JSON/dbHotSwapUsers.json";

	private static String UPLOADED_FOLDER0 = "src/main/resources/static/imagens/perfil/imagensGIF/";
	private static String UPLOADED_FOLDER1 = "src/main/resources/static/imagens/perfil/imagensJPG/";
	private static String UPLOADED_FOLDER2 = "src/main/resources/static/imagens/perfil/imagensPNG/";
	private static String UPLOADED_FOLDER3 = "src/main/resources/static/imagens/perfil/imagensSVG/";
	private static String UPLOADED_FOLDER4 = "src/main/resources/static/imagens/perfil/imagensWebp/";
	private static String PROFILE_DEFAULTPHOTO = "src/main/resources/static/imagens/perfil/";

	@Autowired
	private UserRepository userRepository;

    public String processarImagem(MultipartFile file, int registnumber, String roles) throws FileNotFoundException {
    	DateTimeFormatter timeDateFormat = DateTimeFormatter.ofPattern("dd_MM_yyyy");
		String nomeImagem = null;
		String novoNome = null;
		String uploadImagePath = null;
		String asciiGrant = null;
		String url = null;

		log.info("Uma Imagem Sofreu Upload: " + file.getOriginalFilename() + ", Número do user: " + registnumber);
		log.info("Role of User: " + roles);

		User user = userRepository.getUserObjectById(registnumber);

		try {
			byte[] bytes = file.getBytes();

			if (file.getOriginalFilename().endsWith(".gif")) {
				Path path = Paths.get(UPLOADED_FOLDER0);
				if (!Files.exists(path)) {
					Files.createDirectory(path);
				}
				nomeImagem = file.getOriginalFilename();
				asciiGrant = toAscii(nomeImagem);
				novoNome = user.getRegistNumber() + "-" + LocalDateTime.now()
				.format(timeDateFormat) + "-" + asciiGrant;
				Files.write(path.resolve(novoNome), bytes);
				uploadImagePath = UPLOADED_FOLDER0 + novoNome;
			} else if (file.getOriginalFilename().endsWith(".jpeg") || file.getOriginalFilename().endsWith(".jpg")
					|| file.getOriginalFilename().endsWith(".jpe")) {
				Path path = Paths.get(UPLOADED_FOLDER1);
				if (!Files.exists(path)) {
					Files.createDirectory(path);
				}
				nomeImagem = file.getOriginalFilename();
				asciiGrant = toAscii(nomeImagem);
				novoNome = user.getRegistNumber() + "-" + LocalDateTime.now().format(timeDateFormat) + "-"
						+ asciiGrant;
				Files.write(path.resolve(novoNome), bytes);
				uploadImagePath = UPLOADED_FOLDER1 + novoNome;
			} else if (file.getOriginalFilename().endsWith(".png")) {
				Path path = Paths.get(UPLOADED_FOLDER2);
				if (!Files.exists(path)) {
					Files.createDirectory(path);
				}
				nomeImagem = file.getOriginalFilename();
				asciiGrant = toAscii(nomeImagem);
				novoNome = user.getRegistNumber() + "-" + LocalDateTime.now().format(timeDateFormat) + "-"
						+ asciiGrant;
				Files.write(path.resolve(novoNome), bytes);
				uploadImagePath = UPLOADED_FOLDER2 + novoNome;
			} else if (file.getOriginalFilename().endsWith(".svg")) {
				Path path = Paths.get(UPLOADED_FOLDER3);
				if (!Files.exists(path)) {
					Files.createDirectory(path);
				}
				nomeImagem = file.getOriginalFilename();
				asciiGrant = toAscii(nomeImagem);
				novoNome = user.getRegistNumber() + "-" + LocalDateTime.now().format(timeDateFormat) + "-"
						+ asciiGrant;
				Files.write(path.resolve(novoNome), bytes);
				uploadImagePath = UPLOADED_FOLDER3 + novoNome;
			} else if (file.getOriginalFilename().endsWith(".webp")) {
				Path path = Paths.get(UPLOADED_FOLDER4);
				if (!Files.exists(path)) {
					Files.createDirectory(path);
				}
				nomeImagem = file.getOriginalFilename();
				asciiGrant = toAscii(nomeImagem);
				novoNome = user.getRegistNumber() + "-" + LocalDateTime.now().format(timeDateFormat) + "-"
						+ asciiGrant;
				Files.write(path.resolve(novoNome), bytes);
				uploadImagePath = UPLOADED_FOLDER4 + novoNome;
			}
			url = domainName + "custom/api/imagem/return";

			File dbFile = new File(userJsonDbDir);
			StringBuilder fileContent = new StringBuilder();
			Scanner scan = new Scanner(dbFile);
			while (scan.hasNextLine()) {
				String linha = scan.nextLine();
				if (linha.contains("\"Número de Registro\": " + registnumber + ",")) {
					fileContent.append(linha).append("\n");
					while (scan.hasNextLine()) {
						linha = scan.nextLine();
						if (linha.contains("\"Caminho de Imagem\":")) {
							linha = "    \"Caminho de Imagem\": \"" + uploadImagePath + "\",";
						} else if (linha.contains("\"Foto de Perfil\":")) {
							linha = "    \"Foto de Perfil\": \"" + url + "\",";
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
			PrintWriter out = new PrintWriter(dbFile);
			out.write(fileContent.toString());
			out.close();

			log.info("O caminho da imagem postada: " + uploadImagePath);
            return "A imagem foi processada com sucesso";

		} catch (IOException e) {
			e.printStackTrace();
            return "Erro ao processar a imagem";
		}
    }

    public ResponseEntity<Resource> getUserImagem(int registnumber) throws FileNotFoundException {
		String fullPath = userRepository.findImageFullPathByRegNumber(registnumber);
		String defaultImage = "profile-picture.png";

		log.info(fullPath);

		if (fullPath == null || !fileExists(fullPath)) {
			fullPath = PROFILE_DEFAULTPHOTO + defaultImage;
		}

		log.info("A Imagem: " + fullPath + ", foi carregada.");

		try {
			Path path = Paths.get(fullPath);
			Resource resource = new UrlResource(path.toUri());

			if (resource.exists() || resource.isReadable()) {
				String extension = FilenameUtils.getExtension(fullPath).toLowerCase();
				MediaType mediaType = MediaType.IMAGE_JPEG;

				if (extension.equals("gif")) {
					mediaType = MediaType.IMAGE_GIF;
				} else if (extension.equals("jpg") || extension.equals("jpe") || extension.equals("jpeg")) {
					mediaType = MediaType.IMAGE_JPEG; // os jpg's tem o mesmo MIME
				} else if (extension.equals("png")) {
					mediaType = MediaType.IMAGE_PNG;
				} else if (extension.equals("svg") || extension.equals("svg+xml")) {
					mediaType = MediaType.valueOf("image/svg+xml");
				} else if (extension.equals("webp")) {
					mediaType = MediaType.valueOf("image/webp");
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
							if (linha.contains("\"Caminho de Imagem\":")) {
								linha = "    \"Caminho de Imagem\": \"" + fullPath + "\",";
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
				return ResponseEntity.ok().contentType(mediaType).body(resource);
			} else {
				log.error("Arquivo não existe ou não é legivel " + path);
				throw new RuntimeException("Falha ao carregar imagem");
			}
		} catch (MalformedURLException e) {
			log.error("Erro ao criar URL: " + e.getMessage());
			throw new RuntimeException("Falha ao carregar imagem");
		}
	}

	@ExceptionHandler(MultipartException.class)
	public ResponseEntity<?> handleMultipartException(MultipartException ex) {
		return ResponseEntity.badRequest().body(ex.getMessage());
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<?> tamanhoUploadException(MaxUploadSizeExceededException e) {
		String errorMessage = "Erro: O tamanho do arquivo excede o limite máximo permitido.";
		return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(errorMessage);
	}

	public String toAscii(String str) {
		// Garantir padrão ASCII
		String asciiStr = str.replaceAll("[^\\p{ASCII}]", "_");
		return asciiStr;
	}

	public boolean fileExists(String filePath) {
		Path path = Paths.get(filePath);
		return Files.exists(path) && Files.isRegularFile(path);
	}
}
