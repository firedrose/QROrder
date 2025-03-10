package com.rose.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rose.constant.MessageConstant;
import com.rose.dto.UserLoginDTO;
import com.rose.entity.User;
import com.rose.exception.LoginFailedException;
import com.rose.mapper.UserMapper;
import com.rose.properties.WeChatProperties;
import com.rose.service.UserService;
import com.rose.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    public static final String WX_LOGIN= "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;
    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    public User wxLogin(UserLoginDTO userLoginDTO) {
        //调用微信接口服务,获取当前微信用户的openid
        String openid = getOpenid(userLoginDTO);
        //判断openid是否为空
        if (openid==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        //判断当前用户是否为新用户
        User user= userMapper.getByOpenid(openid);
        //如果是新用户自动完成注册
        if (user==null){
            User newUser = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(newUser);
        }

        //返回这个用户对象
        return user;
    }

    private String getOpenid(UserLoginDTO userLoginDTO) {
        HashMap<String, String> map = new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code", userLoginDTO.getCode());
        map.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);
        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject.getString("openid");
    }
}
