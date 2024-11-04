package com.lyh.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyh.usercenter.common.ErrorCode;
import com.lyh.usercenter.exception.BusinessException;
import com.lyh.usercenter.mapper.UserMapper;
import com.lyh.usercenter.model.domain.User;
import com.lyh.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.lyh.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author RAOYAO
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-10-07 15:28:42
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;
    /**
     * SALT 盐，混淆密码
     */
    private static final String SALT = "lyh";

    @Override
    /*
    用户注册逻辑
     */
    public long userRegister(String userAccount, String password, String checkPassword) {
        //1.校验
        if (StringUtils.isAnyBlank(userAccount, password, checkPassword)) {
            //todo 自定义异常
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");

        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账户长度过短");
        }
        if (password.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度不够");
        }
        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户重复");
        }//这里就是如果count大于0则代表有相同的，返回-1
        //账户不能包含特殊字符
        //校验密码二次相不相同
        if (!password.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //加密
        String digest = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        //向数据库插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(digest);
        boolean save = this.save(user);
        if (!save) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return user.getId();
    }

    @Override
    /*
    用户登录逻辑
     */
    public User doLogin(String userAccount, String password, HttpServletRequest request) {
        //1.校验
        if (StringUtils.isAnyBlank(userAccount, password)) {
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (password.length() < 8) {
            return null;
        }
        String digest = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        //账户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", digest);//eq是查询符合条件的数据条，并包装到queryWrapper里面
        User user = userMapper.selectOne(queryWrapper);// userMapper是plus带的实体对象，用this也可以。selectOne方法是查询唯一的entity，并把值返回给user，若不唯一就抛出异常
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword.");
            return null;
        }
        /* 用户脱敏后再返回给前端 */
        //记录用户的登录态
        User safetyUser = getSafetyUser(user);
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);//这里和下面的返回肯定也得是脱敏后的用户信息
        //脱敏就是允许返回给前端的值，肯定是要去掉敏感信息——直接输入的值
        return safetyUser;
    }

    @Override
    public User getSafetyUser(User user) {
        if (user==null){
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatar(user.getAvatar());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime());

        return safetyUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}




