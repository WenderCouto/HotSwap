package com.hotswap.services;

import com.hotswap.model.ChatMessage;
import com.hotswap.model.User;

public class StructureJsonService {

	User userStructure = new User();

	public StructureJsonService() {
	}

	public StructureJsonService(User structure) {
		this.userStructure = structure;
	}

	public String writeJsonUser(User userStructure) {
		StringBuilder json = new StringBuilder();
		json.append("[\n");
		json.append("  {\n");
		json.append("    \"Número de Registro\": ").append(userStructure.getRegistNumber()).append(",\n");
		json.append("    \"Nome de Usuário\": \"").append(userStructure.getUserName()).append("\",\n");
		json.append("    \"Credenciais\": \"").append(userStructure.getPassword()).append("\",\n");
		json.append("    \"Cargo\": \"").append(userStructure.getRoles()).append("\",\n");
		json.append("    \"Data de Criação\": \"").append(userStructure.getCreatedDate()).append("\",\n");
		json.append("    \"Caminho de Imagem\": \"").append(userStructure.getPathImage()).append("\",\n");
		json.append("    \"Foto de Perfil\": \"").append(userStructure.getFotoPerfil()).append("\",\n");
		json.append("    \"Recado\": \"").append(userStructure.getStatus()).append("\",\n");
		json.append("    \"Reset de Credenciais\": ").append(userStructure.isResetPassword()).append("\n");
		json.append("  }");
		json.append("\n]");
		return json.toString();
	}

	public String addNewUserObj(StringBuilder existingJson, User userStructure) {
		int lastBracketIndex = existingJson.lastIndexOf("}");
		existingJson.insert(lastBracketIndex + 1, ",");
		existingJson.insert(lastBracketIndex + 2, "\n");
		existingJson.insert(lastBracketIndex + 3, toJsonU(userStructure));
		return existingJson.toString();
	}

	public String toJsonU(User userStructure) {
		StringBuilder json = new StringBuilder();
		json.append("  {\n");
		json.append("    \"Número de Registro\": ").append(userStructure.getRegistNumber()).append(",\n");
		json.append("    \"Nome de Usuário\": \"").append(userStructure.getUserName()).append("\",\n");
		json.append("    \"Credenciais\": \"").append(userStructure.getPassword()).append("\",\n");
		json.append("    \"Cargo\": \"").append(userStructure.getRoles()).append("\",\n");
		json.append("    \"Data de Criação\": \"").append(userStructure.getCreatedDate()).append("\",\n");
		json.append("    \"Caminho de Imagem\": \"").append(userStructure.getPathImage()).append("\",\n");
		json.append("    \"Foto de Perfil\": \"").append(userStructure.getFotoPerfil()).append("\",\n");
		json.append("    \"Recado\": \"").append(userStructure.getStatus()).append("\",\n");
		json.append("    \"Reset de Credenciais\": ").append(userStructure.isResetPassword()).append("\n");
		json.append("  }");
		return json.toString();
	}

	ChatMessage chatStructure = new ChatMessage();

	public StructureJsonService(ChatMessage chatStructure) {
		this.chatStructure = chatStructure;
	}

	public String writeJsonMessage(ChatMessage chatStructure) {
		StringBuilder json = new StringBuilder();
		json.append("[\n");
		json.append("  {\n");
		json.append("    \"Transmissor\": \"").append(chatStructure.getSender()).append("\",\n");
		json.append("    \"Receptor\": \"").append(chatStructure.getReceiver()).append("\",\n");
		json.append("    \"Conteúdo\": [\n");
		json.append("      {\n");
		json.append("        \"Mensagem\": \"").append(chatStructure.getContent()).append("\",\n");
		json.append("        \"Data\": \"").append(chatStructure.getDate()).append("\"\n");
		json.append("      }\n");
		json.append("    ]\n");
		json.append("  }");
		json.append("\n]");
		return json.toString();
	}

	public String addNewMessageObj(StringBuilder existingJson, ChatMessage chatStructure) {
		int lastBracketIndex = existingJson.lastIndexOf("}");
		existingJson.insert(lastBracketIndex + 1, ",");
		existingJson.insert(lastBracketIndex + 2, "\n");
		existingJson.insert(lastBracketIndex + 3, toJsonM(chatStructure));
		return existingJson.toString();
	}

	public String toJsonM(ChatMessage chatStructure) {
		StringBuilder json = new StringBuilder();
		json.append("  {\n");
		json.append("    \"Transmissor\": \"").append(chatStructure.getSender()).append("\",\n");
		json.append("    \"Receptor\": \"").append(chatStructure.getReceiver()).append("\",\n");
		json.append("    \"Conteúdo\": [\n");
		json.append("      {\n");
		json.append("        \"Mensagem\": \"").append(chatStructure.getContent()).append("\",\n");
		json.append("        \"Data\": \"").append(chatStructure.getDate()).append("\"\n");
		json.append("      }\n");
		json.append("    ]\n");
		json.append("  }");
		return json.toString();
	}
}
