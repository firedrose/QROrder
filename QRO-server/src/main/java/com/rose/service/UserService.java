package com.rose.service;


import com.rose.dto.UserLoginDTO;
import com.rose.entity.User;

public interface UserService {

    /**
     *微信登录
     * @param userLoginDTO
     * @return
     */
    User wxLogin(UserLoginDTO userLoginDTO);
}
