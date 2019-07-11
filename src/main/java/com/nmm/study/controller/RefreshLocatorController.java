package com.nmm.study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.CachingRouteLocator;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 尝试做路由刷新
 */
@RestController
@RequestMapping("/api/route/refresh")
public class RefreshLocatorController {
    @Autowired
    private RouteLocator routeLocator;
    @GetMapping
    public String refresh(){
        ((CachingRouteLocator)routeLocator).refresh().subscribe();
        return "success";
    }

}
