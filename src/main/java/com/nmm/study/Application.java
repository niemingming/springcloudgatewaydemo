package com.nmm.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }

//    @Bean
//    public RouteLocator routeLocator(RouteLocatorBuilder builder){
//
//        return builder.routes()
//                .route("app",r-> r.path("/gateway/api/app/**").filters(f-> f.rewritePath("/gateway/api/app/(.*)","/")).uri("http://www.baidu.com")
//                ).build();
//    }
}
