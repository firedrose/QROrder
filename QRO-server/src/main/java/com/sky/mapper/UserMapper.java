package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {

    /**
     * 根据openid查询微信用户
     * @param openid
     * @return
     */
    @Select("select * from sky_take_out.user where openid=#{openid}")
    User getByOpenid(String openid);

    void insert(User newUser);

    @Select("select * from sky_take_out.user where id=#{userId}")
    User getById(Long userId);


    Integer getUserAmountByMap(Map<Object, Object> map);



}
