package com.hotswap.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;

public class User {

    @NotEmpty
    private int registNumber;
    @NotEmpty
    private String userName;
    @NotEmpty
    private String password;
    @NotEmpty
    private String roles;
    private String createdDate;
    private String pathImage; //camihno
    private String fotoPerfil; //url
    private String status;
    private boolean resetPassword;

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

    public String getPassword() {
        return password;
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

    public String getStatus() {return status;}

    public void setStatus(String status) {this.status = status;}

    public boolean isResetPassword() {
        return resetPassword;
    }

    public void setResetPassword(boolean resetPassword) {
        this.resetPassword = resetPassword;
    }

}
