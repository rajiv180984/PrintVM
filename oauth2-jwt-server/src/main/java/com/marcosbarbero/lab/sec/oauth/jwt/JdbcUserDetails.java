/*package com.marcosbarbero.lab.sec.oauth.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.marcosbarbero.lab.sec.oauth.jwt.web.CredentialRepository;
import com.marcosbarbero.lab.sec.oauth.jwt.web.Credentials;

*//**
 * Created by ahmed on 21.5.18.
 *//*
@Component
public class JdbcUserDetails implements UserDetailsService{

    @Autowired
    private CredentialRepository credentialRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Credentials credentials = credentialRepository.findByName(username);


        if(credentials==null){

            throw new UsernameNotFoundException("User"+username+"can not be found");
        }

        User user = new User(credentials.getName(),credentials.getPassword(),credentials.isEnabled(),true,true,true,credentials.getAuthorities());

        return  user;


    }
}
*/