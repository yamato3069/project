package com.example.demo.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.LoginUser;

@Mapper
public interface LoginMapper {
	
	LoginUser getLoginUsers(@Param("id") Integer id);

}
