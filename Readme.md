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