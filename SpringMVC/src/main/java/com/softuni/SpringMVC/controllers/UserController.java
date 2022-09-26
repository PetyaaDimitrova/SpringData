package com.softuni.SpringMVC.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/users/register")
    public String register(){
        return "user/register";
    }



}
