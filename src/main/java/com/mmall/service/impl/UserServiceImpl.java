package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.Const.Role;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0){
            return ServerResponse.createByEorrorMessage("用户名不存在");
        }
        //密码登录MD5
            String md5Password = MD5Util.MD5EncodeUtf8(password);
            User user = userMapper.selectLogin(username,md5Password);
            if (user==null){
                return ServerResponse.createByEorrorMessage("密码错误");
            }

            user.setPassword(StringUtils.EMPTY);
            return  ServerResponse.createBySuccess("登录成功",user);
        }

        public ServerResponse<String> regist(User user){
//            int resultCount= userMapper.checkUsername(user.getUsername());
//            if (resultCount > 0){
//                return ServerResponse.createByEorrorMessage("用户名已存在");
//            }
//            resultCount = userMapper.checkEmail(user.getEmail());
//            if (resultCount > 0){
//                return  ServerResponse.createByEorrorMessage("email已存在");
//            }
            ServerResponse valiResponse =this.checkVaild(user.getUsername(),Const.USERNAME);
            if (!valiResponse.isSuccess()){
                return valiResponse;
            }
            valiResponse =this.checkVaild(user.getEmail(),Const.EMAIL);
            if (!valiResponse.isSuccess()){
                return valiResponse;
            }
            user.setRole(Role.ROLE_CUSTOMR);
            //MD5加密
            user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
            int resultCount = userMapper.insert(user);
            if (resultCount == 0){
                return ServerResponse.createByEorrorMessage("注册失败");
            }
            return ServerResponse.createBySuccessMessage("注册成功");
        }

        public ServerResponse<String> checkVaild(String str,String type){
            if(org.apache.commons.lang3.StringUtils.isNotBlank(type)){
                //开始校验
                if (Const.USERNAME.equals(type)){
                    int resultCount = userMapper.checkUsername(str);
                    if (resultCount > 0){
                        return ServerResponse.createByEorrorMessage("用户名已存在");
                    }
                }
                if (Const.EMAIL.equals(type)){
                    int resultCount = userMapper.checkEmail(str);
                    if (resultCount > 0){
                        return ServerResponse.createByEorrorMessage("emasil已存在");
                    }
                }
            }else {
                return ServerResponse.createByEorrorMessage("参数错误");
            }
            return ServerResponse.createBySuccessMessage("校验成功");
        }
        public  ServerResponse selectQuestion(String username){
            ServerResponse validResponse = this.checkVaild(username,Const.USERNAME);
            if (validResponse.isSuccess()){
                //用户不存在
                return ServerResponse.createByEorrorMessage("用户不存在");
            }
            String question =userMapper.selectQuestionByUsername(username);
            if (StringUtils.isNotBlank(question)){
                return  ServerResponse.createBySuccess(question);
            }
            return ServerResponse.createByEorrorMessage("找回密码的问题是空的");

        }
        public  ServerResponse<String> checkAnswer(String username,String question,String answer){
            int resultCount = userMapper.checkAnswer(username,question,answer);
            if (resultCount > 0){
                //说明问题及问题答案是这个用户的，并且是正确的
                String forgetToken = UUID.randomUUID().toString();
                TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
                return ServerResponse.createBySuccess(forgetToken);
            }
            return ServerResponse.createByEorrorMessage("问题的答案错误");
        }

        public ServerResponse<String> forgetRestPassword(String username,String passwordNew,String forgetToken) {
            if (StringUtils.isBlank(forgetToken)) {
                return ServerResponse.createByEorrorMessage("参数错误，token需要传递");
            }
            ServerResponse validResponse = this.checkVaild(username, Const.USERNAME);
            if (validResponse.isSuccess()) {
                //用户不存在
                return ServerResponse.createByEorrorMessage("用户不存在");
            }
            String token = TokenCache.getkey(TokenCache.TOKEN_PREFIX + username);
            if (StringUtils.isBlank(token)) {
                return ServerResponse.createByEorrorMessage("token无效或者过期");
            }
            if (StringUtils.equals(forgetToken, token)) {
                String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
                int rowCount = userMapper.updatePasswordByUsername(username, md5Password);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccessMessage("修改密码成功");
                }
            } else {
                    return ServerResponse.createByEorrorMessage("token错误，请重新获取重置密码的token");
                }
                return ServerResponse.createByEorrorMessage("修改密码失败");
            }
    public ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user){
        //防止横向越权 校验用户旧密码一定是指定该用户
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if (resultCount == 0){
            return ServerResponse.createByEorrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0){
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByEorrorMessage("密码更新失败");
    }


    public ServerResponse<User> updateInformation(User user){
        //username是不能被更新的
        //email也要进行校验 校验新的email是不是已经存在 并且存在的email如果效果相同的话 不能是我们当前这个用户的
        int reultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if (reultCount > 0){
            return ServerResponse.createByEorrorMessage("email已存在，请更换email在尝试更新");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0){
            return ServerResponse.createBySuccess("更新个人信息成功！",updateUser);
        }
        return ServerResponse.createByEorrorMessage("更新个人信息失败！");
    }

    public ServerResponse<User> getInformation(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null ){
            return ServerResponse.createByEorrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    //backend
    //校验是否为管理员
    public ServerResponse checkAdminRole(User user){
        if (user != null && user.getRole().intValue() == Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByEorror();
    }
}

