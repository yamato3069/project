package com.example.demo.mapper;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.LoginUser;

@Mapper
public interface LoginMapper {
	
	List<LoginUser> findAllUsers();
	
	LoginUser getLoginUsers(@Param("id") Integer id);
	
	LoginUser serchUsers(@Param("name") String name);
	
	boolean insertUser(@Param("id") Integer id, @Param("password") String password,
			@Param("name") String name, @Param("role") String role,
			@Param("startDate") Date startDate);

	void deleteUser(@Param("id") Integer id);
}
