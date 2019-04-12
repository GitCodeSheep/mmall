package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import java.io.Serializable;
//保证序列化json的时候，如果为null，key会消失
@JsonSerialize(include = Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {
    private int status;
    private String msg;
    private T data;

    private ServerResponse(int status){
        this.status=status;
    }
    private ServerResponse(int status,T data){
        this.status=status;
        this.data=data;
    }
    private ServerResponse(int status,T data,String msg){
        this.data=data;
        this.status=status;
        this.msg=msg;
    }
    private ServerResponse(int status,String msg){
        this.status=status;
        this.msg=msg;
    }
    @JsonIgnore
    //让其不再json序列化结果当中
    public Boolean isSuccess(){
        return this.status==ResponseCode.SUCCESS.getCode();
    }
    public int getStatus(){
        return status;
    }
    public T getData(){
        return data;
    }
    public String getMsg(){
        return msg;
    }
    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }
    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }
    public static <T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }
    public static <T> ServerResponse<T> createBySuccess(String msg,T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data,msg);
    }

    public static  <T> ServerResponse<T> createByEorror(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }

    public static <T> ServerResponse<T> createByEorrorMessage(String eorrorMessage){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),eorrorMessage);
    }

    public static <T> ServerResponse<T> createByEorrorCodeMessage(int eorrorCode,String eorrorMessage){
        return new ServerResponse<T>(eorrorCode,eorrorMessage);
    }
}
