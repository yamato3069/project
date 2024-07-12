package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
//@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @RequestMapping("/regist")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
//        return String.format("Hello %s!", name);
    	  return "attendance/regist";
    }
}

