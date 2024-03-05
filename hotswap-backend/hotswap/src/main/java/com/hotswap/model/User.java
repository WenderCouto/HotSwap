package com.hotswap.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class User implements UserDetails {

    @NotEmpty
    @JsonProperty("Número de Registro")
    private int registNumber;
    @NotEmpty
    @JsonProperty("Nome de Usuário")
    private String userName;
    @NotEmpty
    @JsonProperty("Credenciais")
    private String password;
    @NotEmpty
    @JsonProperty("Cargo")
    private String roles;
    @JsonProperty("Data de Criação")
    private String createdDate;
    @JsonProperty("Caminho de Imagem")
    private String pathImage; //camihno
    @JsonProperty("Foto de Perfil")
    private String fotoPerfil; //url
    @JsonProperty("Recado")
    private String status;
    @JsonProperty("Reset de Credenciais")
    private boolean resetPassword;

    private String tokenSecret;
    private String tokenJWT;

    public User() {
    }

    public User(int registNumber, String userName, String password, String roles, String pathImage) {
        this.registNumber = registNumber;
        this.userName = userName;
        this.password = password;
        this.roles = roles;
    }

    @Transient
    DateTimeFormatter timeDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy | HH:mm:ss");

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = LocalDateTime.now().format(timeDateFormat) + "s";
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isResetPassword(boolean interruptor) {
        return this.resetPassword = interruptor;
    }

    public void setResetPassword(boolean resetPassword) {
        this.resetPassword = resetPassword;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public String getTokenJWT() {
        return tokenJWT;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    public void setTokenJWT(String tokenJWT) {
        this.tokenJWT = tokenJWT;
    }

    public int getRegistNumber() {
        return registNumber;
    }

    public void setRegistNumber(int registNumber) {
        this.registNumber = registNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null) {
            roles = "";
        }
        return Arrays.stream(roles.split(","))
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.trim()))
                .collect(Collectors.toList());
    }

    public boolean hasRole(String role) {
        return getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
    }

    public void validateRoles() {
        Set<String> validRoles = Set.of("user", "admin", "staff");
        List<String> rolesList = Arrays.asList(roles.split(","));

        if (!rolesList.stream().allMatch(validRoles::contains)) {
            throw new IllegalArgumentException("Invalid role found");
        }
    }
}
