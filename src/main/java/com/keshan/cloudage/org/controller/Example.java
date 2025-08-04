package com.keshan.cloudage.org.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Example {

    @GetMapping("/")
    public String helloworld(){
        return "Hello world";
    }
}
