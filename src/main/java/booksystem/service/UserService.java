package booksystem.service;

import booksystem.pojo.User;

import java.util.List;

public interface UserService {
    //获取所有用户
    List<User> getAllUser();
    int getAllUserNum();
    //根据用户username获取用户信息
    User getUserByName(String username);
    User getUserByID(String user_id);
    //添加
    void addUser(String email,String password,String name,int status,String activationCode);

    //删除用户
    void deleteUser(String user_id);
    //更新用户
    int updateUser(String username,String password,String name);
    //更新用户激活状态和激活码
    int updateCode(String username,String activationCode);

    //发送邮件
    int sendMimeMail( String password,String email,String name);
    //生成随机激活码
    String randomCode();
    //注册用户
    int register(String password,String email,String name,String activationCode);

    //修改绑定邮箱
    void updateEmail(String user_id,String email);

}
