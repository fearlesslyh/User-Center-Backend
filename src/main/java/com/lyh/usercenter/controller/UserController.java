package com.lyh.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyh.usercenter.common.BaseResponse;
import com.lyh.usercenter.common.ErrorCode;
import com.lyh.usercenter.common.ResultUtils;
import com.lyh.usercenter.exception.BusinessException;
import com.lyh.usercenter.model.domain.User;
import com.lyh.usercenter.model.domain.request.UserLoginRequest;
import com.lyh.usercenter.model.domain.request.UserRegisterRequest;
import com.lyh.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.lyh.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.lyh.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author 梁懿豪
 * @version 1.0
 */
@RestController
@RequestMapping("/user")


public class UserController {
    @Resource
    private UserService userService;


    //注册
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        //requestBody是为了和前端请求的参数，请求体，对应上
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        //校验请求参数是否为空
        if (StringUtils.isAnyBlank(userAccount, password, checkPassword)) {
            return null;
        }

        long result = userService.userRegister(userAccount, password, checkPassword);
        return ResultUtils.success(result);

    }


    //登录
    @PostMapping("/login")
    public BaseResponse<User> doLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        //requestBody是为了和前端请求的参数，请求体，对应上
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);

        }
        String userAccount = userLoginRequest.getUserAccount();
        String password = userLoginRequest.getPassword();
        //校验请求参数是否为空
        if (StringUtils.isAnyBlank(userAccount, password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);

        }
        User user = userService.doLogin(userAccount, password, request);
        return ResultUtils.success(user);
    }

    //注销用户
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);

        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object attribute = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) attribute;
        if (currentUser == null) {
           throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Long userId = currentUser.getId();
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    //查询用户名，模糊查询
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String userName, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "缺少管理员权限");

        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userName)) {
            queryWrapper.like("username", userName);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> {
            user.setUserPassword(null);
            return user;
        }).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    //删除用户
    @GetMapping("/delete")
    public BaseResponse<Boolean> deleteUsers(@RequestBody long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */

    private boolean isAdmin(HttpServletRequest request) {
        //仅管理员才能查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User userNew = (User) userObj;
        if (userNew == null || userNew.getUserRole() != ADMIN_ROLE) {
            return false;
        }
        return true;
    }
}
