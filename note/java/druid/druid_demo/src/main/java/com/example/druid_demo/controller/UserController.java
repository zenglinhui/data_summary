package com.example.druid_demo.controller;

import com.example.druid_demo.mapper.UserMapper;
import com.example.druid_demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zenglh
 * @date 2021/11/18 15:18
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public String userList() {
        List<User> userList = userMapper.selectAllUsers();
        return "success";
    }

}
