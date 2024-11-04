package com.lyh.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 梁懿豪
 * @version 1.0
 */
@Data
public class UserRegisterRequest implements Serializable {

    private String userAccount;
    private String password;
    private String checkPassword;
}
