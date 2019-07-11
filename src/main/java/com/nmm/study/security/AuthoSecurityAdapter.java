package com.nmm.study.security;

import com.nmm.study.security.config.MyUserDetailsService;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * 配置权限信息
 */
@EnableWebSecurity
public class AuthoSecurityAdapter extends WebSecurityConfigurerAdapter{

    /**
     * 配置自定义用户权限获取类
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService((UserDetailsService) getApplicationContext().getBean("myUserDetailsService"));
    }

    /**
     * 配置全局拦截处理
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        //配置安全过滤器
//        web.addSecurityFilterChainBuilder()

//        web.securityInterceptor()
        web.ignoring().mvcMatchers("**/favicon.ico");

    }

    /**
     * 权限配置
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login/**").permitAll()//不授权
                .antMatchers("/api/book/add","/api/book/update/*").hasAnyRole("ADMIN")
                .anyRequest().authenticated();//任意请求，登陆后即可访问

    }
}
