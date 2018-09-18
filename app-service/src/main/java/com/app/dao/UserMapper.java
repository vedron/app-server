package com.app.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    int createUser(@Param("userId")Long userId,
    		@Param("userMobilephone")String userMobilephone,
    		@Param("userDeviceType")String userDeviceType,
    		@Param("userDeviceId")String userDeviceId);
}
