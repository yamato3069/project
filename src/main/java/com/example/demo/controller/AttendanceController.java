package com.example.demo.controller;

import java.util.Calendar;

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
    	//Calendarクラスのオブジェクトを生成する
        Calendar cl = Calendar.getInstance();
        
        System.out.println("YEAR：" + cl.get(Calendar.YEAR));
        System.out.println("MONTH：" + cl.get(Calendar.MONTH)); 
        System.out.println("DATE：" + cl.get(Calendar.DATE));  
    	  return "attendance/regist";
    }
    
    @RequestMapping("/regist2")
    public String hello2(@RequestParam(value = "name", defaultValue = "World") String name) {
//        return String.format("Hello %s!", name);
    	//Calendarクラスのオブジェクトを生成する
        Calendar cl = Calendar.getInstance();
        
        System.out.println("YEAR：" + cl.get(Calendar.YEAR));
        System.out.println("MONTH：" + cl.get(Calendar.MONTH)); 
        System.out.println("DATE：" + cl.get(Calendar.DATE));  
    	  return "attendance/regist2";
    }
}

