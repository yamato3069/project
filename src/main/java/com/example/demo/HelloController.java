package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
//@RestController
@RequestMapping("/hello")
public class HelloController {

    @RequestMapping("")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
//        return String.format("Hello %s!", name);
    	  return ("index");
    }
}

