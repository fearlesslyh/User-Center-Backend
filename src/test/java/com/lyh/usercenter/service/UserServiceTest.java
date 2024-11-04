package com.lyh.usercenter.service;

import java.util.Date;

import com.lyh.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 梁懿豪
 * @version 1.0
 */
@SpringBootTest
class UserServiceTest {
    @Resource
    private UserService userService;

    @Test
    public void test() {
        User user = new User();
        user.setUsername("doggy");
        user.setUserAccount("123");
        user.setAvatar("https://img-home.csdnimg.cn/images/20201124032511.png");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("123");
        user.setEmail("456");
        boolean save = userService.save(user);
        if (save) {
            System.out.println("用户注册成功");
        } else if (!(save)) {
            System.out.println("用户注册失败");
        }
        System.out.println(user.getId());
        Assertions.assertTrue(save);
    }

    @Test
    void userRegister() {
        String userAccount = "yihao";
        String password = "";
        String checkpassword = "123456789";
        long l = userService.userRegister(userAccount, password, checkpassword);
        Assertions.assertEquals(-1, l);
        userAccount = "yi";
        l = userService.userRegister(userAccount, password, checkpassword);
        Assertions.assertEquals(-1, l);
        password="123456";
        userAccount="yihao";
        l = userService.userRegister(userAccount, password, checkpassword);
        Assertions.assertEquals(-1, l);
        userAccount="yupidog";
        password="123456789";
        l = userService.userRegister(userAccount, password, checkpassword);
        Assertions.assertTrue(l>0);
    }
}