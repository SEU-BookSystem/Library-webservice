package booksystem.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MessageDao {
    //添加一条消息
    void addMessage(String username,String theme,String content);

    //已读所有消息
    void readAllMessage(String username);
    //已读一条消息
    void readMessage(String message_id);

    //删除用户所有消息
    void deleteAllMessage(String username);
    //删除用户一条消息
    void deleteMessage(String message_id);

    //返回用户未读消息数量
    int nonReadMessage(String username);

}
