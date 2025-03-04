package com.rose.service;

import com.rose.dto.ShoppingCartDTO;
import com.rose.entity.ShoppingCart;

import java.util.List;


public interface ShoppingCartService {

    /**
     *购物车添加商品接口
     * @param shoppingCartDTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);


    /**
     * 展示购物车
     * @return
     */
    List<ShoppingCart> show();

    /**
     * 清空购物车
     */
    void cleanShoppingCart();

    /**
     *减少购物车商品数量
     * @param shoppingCartDTO
     */
    void sub(ShoppingCartDTO shoppingCartDTO);
}
