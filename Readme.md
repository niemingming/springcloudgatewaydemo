# 配置项说明先看代码配置：
```
 @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder){

        return builder.routes()
                .route("app",r-> r.path("/gateway/api/app/**").filters(f-> f.rewritePath("/gateway/api/app/(.*)","/")).uri("http://www.baidu.com")
                ).build();
    }
```
这里面配置了route和filter
可以看到route中uri和id是作为属性的，我们其他的配置对应的相对的实现方法。
filter，更是除了name都是对应了响应的filter来实现过滤。我们在yml文件中，实际上的key值就
指定了相关filter
```
spring:
  cloud:
    gateway:
      routes:
      - id: console
        uri: http://mobilityconsole:8080/console/
        predicates:
        - Path=/console/**
      - id: authoration
        uri: ${systemuri.author}
        predicates:
        - Path=/gateway/author/**
      - id: app
        uri: http://mobilityrobot:8008/api/app/
        predicates:
        - Path=/gateway/api/app/**
      - id: wx
        uri: http://mobilityrobot:8008/api/wx/
        predicates:
        - Path=/gateway/api/wx/**
        #配置了默认的过滤器，也可以为每个映射配置单独的过滤器
      default-filters:
      - name: authorFilterFactory
      #这里实际上指定了RewritePathGatewayFilter
      - RewritePath=/gateway/(?<segment>.*), /$\{segment}
      #指定了AddResponseHeaderGatewayFilter,要求route返回的响应头必须带x-user-info
      - AddResponseHeader=x-user-info
      #全局过滤器，重写url
```

## 自定义gateway
### 全局过滤器
我们只需要实现GlobaleFilter即可，并加入spring管理。相当于配置了default-filters
```
package com.nmm.study.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局过滤器，应该是适用于全部的过滤拦截
 */
@Component
public class MyGlobleFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("globalfilter");
        return chain.filter(exchange);
    }
}
```
### 定向/指定过滤器
这类过滤器需要实现GatewayFilter接口，需要注意的是，这类过滤器由于在设计初衷是需要配置到某一项或某几项路由中作为过滤器使用，
因此它是通过工厂设计模式来工作的，在代码中使用我们就不需要说了，重点说下配置文件+GatewayFilter怎么工作的。

1、实现GatewayFilter接口
注意，这里不强制加入spring容器管理，需要说明的是，个人理解这种工厂模式下，本身就不太希望
过滤器被spring管理，需要通过工厂创建。
```
package com.nmm.study.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class MyGatewayFilterone implements GatewayFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("gatewayfilter");
        return chain.filter(exchange);
    }
}

```
这里是加入到spring容器中了。

2、实现GatewayFilterFactory接口
```
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
    //必须实现接口，用于配置项的实例化，相当于创建一个配置项。注意配置项类必须可以new
    @Override
    public Object newConfig() {
        return new Config();
    }
    //非必须实现接口，用于配置项的配置
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("name","age");
    }
    //必须实现接口，实现会报错，如果不需要使用配置项，可以返回基础类型，比如String.class
    @Override
    public Class getConfigClass() {
        return Config.class;
    }

    //必须实现接口，这个name很关键，是配置文件使用的key值，类似于spring实体的id，但并不等同
    @Override
    public String name() {
        return "mygatewayfactory";
    }
    //必须实现方法，这里返回的filter就是针对route真正起作用的filter
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

```
3、我们看下配置文件怎么写
```
spring:
  cloud:
    gateway:
      routes:
      - id: baidu
        uri: https://www.baidu.com
        filters:
        - RewritePath=/api/baidu/.*,/
#        - name: mygatewayfactory
        - mygatewayfactory=test,18
        predicates:
        - Path=/api/baidu/**
      - id: github
        uri: https://github.com
        predicates:
        - Path=/api/github/**
        filters:
        - RewritePath=/api/github/(?<param>.*),/$\{param}
        - mygatewayfactory=github,20
```
看上文，我么通过filters. -mygatewayfactory=test,18
这种形式配置的，这里的mygatewayfactory就是我们name返回的名称。
如果我们不需要单独配置，可以通过-name: mygatewayfactory的形式，指定gateway的name。
这里的name也是name接口返回的。

我们来看看配置项怎么做，我们实现shortcutFieldOrder，就是指定了配置项的字段顺序和名称，
yml中通过,分隔各配置项。这里容易得出，spring是通过反射设置了配置项属性，并在apply方法时
传给了factory，用于针对不同的情况。如果filter我们托管给spring，name单例下，很可能会出现
类似于线程安全的问题。

### 思考
考虑了下为什么GatewayFilter需要使用工厂模式，而GlobalFilter不需要个人以为有以下原因：
* globalfilter，是全局的只需要一个实例就可以了，因此直接交给spring管理创建即可，但是GatewayFilter可能被不同的route复用
* 为什么会是工厂模式？而不是直接配置实例id，个人猜测是因为引入了config，考虑不同的route在使用同一个gatewayfilter的时候，存在主体逻辑一致，但是需要不同参数的情况，因此需要传入配置项，这时候通过单例很难实现。通过工厂模式，配置不同的config，即可实现，不同route有不同的filter。这也是为什么前面说，GatewayFilter建议由工厂创建，不建议由spring管理。
* GatewayFilter可以成为全局的吗？是可以的，只需要在配置文件中，default-filters指定它就可以了，但是这样不如直接使用GlobalFilter来的优雅
