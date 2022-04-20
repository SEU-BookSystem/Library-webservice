package booksystem.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserLoginDao {
    //登录
    int userLogin(String username,String password);
    int getIdentity(String username);
    int adminLogin(String username,String password);

}
