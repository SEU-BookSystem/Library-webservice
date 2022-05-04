package booksystem.service.Impl;

import booksystem.dao.MessageDao;
import booksystem.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImp implements MessageService {
    @Autowired
    MessageDao messageDao;


    @Override
    public int sendOvertimeMessage(String username,String book_name) {
        messageDao.addMessage(username,"借阅通知",
                "尊敬的会员,您借阅的书籍"+book_name+"已逾期，请尽快到图书馆办理还书。");
        return 1;
    }

    @Override
    public int sendReserveMessage(String username,String book_name) {
        messageDao.addMessage(username,"预约通知",
                "尊敬的会员,由于您预约的书籍"+book_name+"未按时达到图书馆进行借阅，因此取消了您的预约。" +
                        "如有需要，请再次进行预约。");
        return 0;
    }

    @Override
    public int readAllMessage(String username) {
        messageDao.readAllMessage(username);
        return 0;
    }

    @Override
    public int readMessage(String message_id) {
        messageDao.readMessage(message_id);
        return 0;
    }

    @Override
    public int deleteAllMessage(String username) {
        messageDao.deleteAllMessage(username);
        return 0;
    }

    @Override
    public int deleteMessage(String message_id) {
        messageDao.deleteMessage(message_id);
        return 0;
    }

    @Override
    public int nonReadMessage(String username) {
        messageDao.nonReadMessage(username);
        return 0;
    }
}
