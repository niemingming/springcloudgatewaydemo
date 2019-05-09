package com.nmm.study.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
//@Component
public class MyGatewayFilter implements GatewayFilter,Ordered {
   //过滤处理
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求和响应
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        //重写request
        request = request.mutate().header("x-user-info","test").build();
        Mono res = chain.filter(exchange);
        //如果要范围空可以
        res = Mono.empty();
        //或者
        exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
        res = exchange.getResponse().setComplete();
        //需要注意的是route返回的状态码最好不要是标准之外的状态码，虽然springcloud不会拦截，但是在k8s环境下，有可能会被负载均衡器拦截处理。
        return res;
    }
    //优先级
    @Override
    public int getOrder() {
        return 1;
    }
}
