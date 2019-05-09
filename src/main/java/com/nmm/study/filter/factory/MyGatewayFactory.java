package com.nmm.study.filter.factory;

import com.nmm.study.filter.MyGatewayFilterone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class MyGatewayFactory implements GatewayFilterFactory {
    @Autowired
    private MyGatewayFilterone myGatewayFilterone;

    @Override
    public Object newConfig() {
        return new Config();
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("name","age");
    }

    @Override
    public Class getConfigClass() {
        return Config.class;
    }


    @Override
    public String name() {
        return "mygatewayfactory";
    }

    @Override
    public GatewayFilter apply(Object config) {
        System.out.println(config);
        return myGatewayFilterone;
    }

    public static class Config{
        private String name;
        private String age;

        public Config setName(String name ){
            this.name = name;
            return this;
        }

        public void setAge(String age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "name=" + name + ";;;;;age="+age;
        }
    }
}
