package booksystem.dao;

import booksystem.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface UserDao {
    //获取所有用户
    List<Map<String,Object>> getAllUser();
    List<Map<String,Object>> getUsers(int start, int book_num);
    int getUsersCount();
    int getAllUserNum();
    //根据用户username获取用户信息
    User getUserByName(String username);
//    User getUserByID(String user_id);
    //添加一个用户
    void addUser(String username,String password,String id_card,int age,String gender,String name);
    //删除一个用户
    void deleteUser(String username);
    //更新用户信息
    int updateUser(String username,String password,String name,int age,String gender,String id_card);
    //更新用户激活状态和激活码
    int updateStatus(String username,int status);
    int updateTime(String username);
    int accessTime(String username);

    List<Map<String,Object>> adminFuzzyQuery(int start, int each_num, int queryWhat, String content);
    int adminFuzzyQueryCount(int queryWhat, String content);
}
