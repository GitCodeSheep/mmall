package com.mmall.dao;

import com.google.common.collect.Lists;
import com.mmall.pojo.Cart;
import com.mmall.pojo.CartKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(CartKey key);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(CartKey key);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectCartByUserIdProductId(@Param("userId") Integer userId,@Param("productId") Integer productId);

    List<Cart> selectCartByUserId(Integer userId);

    int selectProductCheckedStatusByUserId(Integer userId);

    int deleteByUserIdProductIds(@Param("userId") Integer userId, @Param("productIdList")List<String> productIdList);

    int checkOrUncheckedProduct(@Param("userId") Integer userId, @Param("checked")Integer checked ,@Param("productId") Integer productId);

    int selectCartProductCount(@Param("userId") Integer userId);
}