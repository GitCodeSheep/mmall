package com.mmall.dao;

import com.mmall.pojo.Cart;
import com.mmall.pojo.CartKey;

public interface CartMapper {
    int deleteByPrimaryKey(CartKey key);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(CartKey key);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);
}