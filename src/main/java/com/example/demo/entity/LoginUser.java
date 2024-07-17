package com.example.demo.entity;
import java.util.Date;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class LoginUser {
	
    // ユーザーID
    private Integer id;
    // パスワード
    private String password;
    // 氏名
    private String name;
    // 役職
    private String role;
    // 有効開始日
    private Date startDate;

}
