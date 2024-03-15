package com.hotswap.DTO;

import com.hotswap.model.User;

public class UserResult {
    private User user;
    private int registNumber;

    public User getUser() {
        return user;
    }

    public int getRegistNumber() {
        return registNumber;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRegistNumber(int registNumber) {
        this.registNumber = registNumber;
    }

}
