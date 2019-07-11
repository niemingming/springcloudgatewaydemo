package com.nmm.study.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/book")
public class TestSecurityController {

    @GetMapping("/add")
    public String add(){

        return "add";
    }

    @GetMapping("/update/{name}")
    public String update(@PathVariable String name){

        return "update:: " + name;
    }
}
