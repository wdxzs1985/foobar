package com.foobar.mapper;

import java.util.Map;

import com.foobar.domain.UserBean;
import com.foobar.domain.UserProfileBean;

public interface UserMapper {

    public UserBean fetchBean(Map<String, Object> param);

    public UserProfileBean fetchProfile(Map<String, Object> param);

    public void insert(UserBean userBean);

    public void update(Map<String, Object> param);

}
