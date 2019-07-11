package com.nmm.study.security.config;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        System.out.println("登录信息。。。"+ s);

        MyUser user = new MyUser();
        user.setAuthorities(Arrays.asList(new SimpleGrantedAuthority("admin".toUpperCase()),new SimpleGrantedAuthority("employee".toUpperCase())));
        user.setUsername(s);
        user.setPassword("123456");

        return user;
    }
}
