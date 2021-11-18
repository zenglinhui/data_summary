package com.example.druid_demo.mapper;

import com.example.druid_demo.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zenglh
 * @date 2021/11/18 14:21
 */
@Mapper
public interface UserMapper {

    /**
     * 增加用户
     * @param user
     * @return
     */
    long insertUser(User user);

    /**
     * 查询所有用户
     * @return
     */
    List<User> selectAllUsers();

    /**
     * 删除指定的用户
     * @param userId
     * @return
     */
    int deleteUser(@Param("userId") long userId);

}
