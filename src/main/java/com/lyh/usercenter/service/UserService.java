package com.lyh.usercenter.service;

import com.lyh.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author RAOYAO
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2024-10-07 15:28:42
 */
public interface UserService extends IService<User> {

    /**
     * @param userAccount   账户
     * @param password      密码
     * @param checkPassword 校验密码
     * @return 注册新用户的id
     */
    long userRegister(String userAccount, String password, String checkPassword);

    /**
     * @param userAccount 账户
     * @param password    密码
     * @param request
     * @return 脱敏后的用户信息
     */
    User doLogin(String userAccount, String password, HttpServletRequest request);

    /**
     *
     * @param originUser
     * @return 用户脱敏
     *
     */
    User getSafetyUser(User originUser);

    /**
     *
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);
}
