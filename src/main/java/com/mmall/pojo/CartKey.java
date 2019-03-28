package com.mmall.pojo;

public class CartKey {
    private Integer id;

    private Integer userId;

    public CartKey(Integer id, Integer userId) {
        this.id = id;
        this.userId = userId;
    }

    public CartKey() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}