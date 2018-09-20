package com.app.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.app.entity.dto.UserInfoDto;

@Mapper
public interface UserMapper {
    int createUser(UserInfoDto user);
    

    int updateUser(@Param("userId")Long userId,
    		@Param("userMobilephone")String userMobilephone,
    		@Param("userDeviceType")String userDeviceType,
    		@Param("userDeviceId")String userDeviceId);
}
