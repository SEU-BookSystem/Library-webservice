package booksystem.service;

import java.util.List;
import java.util.Map;

public interface MessageService {
    //发送逾期消息
    int sendOvertimeMessage(String username,String book_name);
    //发送预约失败消息
    int sendReserveMessage(String username,String book_name);

    //已读所有消息
    int readAllMessage(String username);
    //已读一条消息
    int readMessage(String message_id);

    //删除用户所有消息
    int deleteAllMessage(String username);
    //删除用户一条消息
    int deleteMessage(String message_id);

    //返回用户未读消息数量
    int nonReadMessage(String username);
}
