package booksystem.service;

import booksystem.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    //获取所有用户
    List<Map<String,Object>> getAllUser();
    int getAllUserNum();
    //根据用户username获取用户信息
    User getUserByName(String username);
    //添加
    void addUser(String username,String password,String id_card,int age,String gender,String name);

    //删除用户
    void deleteUser(String username);
    //更新用户
    int updateUser(String username,String password,String name,int age,String gender,String id_card);

}
