package com.chen.controller;

import com.chen.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/home")
public class HomeController {

    @RequestMapping("/index.do")
    @ResponseBody
    public Object test1(  User user){
        System.out.println("id "+ user.getId());
        System.out.println("userName "+ user.getUserName());
        System.out.println("password "+ user.getPassword());
        return user;
    }
}
