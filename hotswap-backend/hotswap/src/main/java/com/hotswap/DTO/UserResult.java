package com.hotswap.DTO;

import com.hotswap.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserResult implements UserDetails  {
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
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
}
