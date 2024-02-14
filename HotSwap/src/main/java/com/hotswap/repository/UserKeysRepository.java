package com.hotswap.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserKeysRepository {

    @Autowired
    UserRepository userRepository;

    public String findUserTokenSecret(){ //retorna a senha

        return null;
    }

    public String findUserTokenKey(){ //retorna o token em si
        return null;
    }

    public void setUserTokenSecret(String secret){ //escreve no documento a nova senha
    }

    public void setUserTokenKey(){ //escreve no documento o token em si.

    }

}
