package booksystem.service;

import java.text.ParseException;
import java.util.List;

public interface BorrowService {
    //计算时间
    String[] countTime(int num);

    //预约   用户、管理员都能操作
    //取消预约
    int cancelReserve(String lend_id);
    //批量取消预约
    int batCancelReserve(List<String> lend_ids);

    //收藏夹预约
    int addCollectionReserve(List<String> reference_nums,String username);
    //直接预约
    int addDirectReserve(String reference_num,String username);

    //续借
    int renew(int bar_code,String username);

    //借阅 只有管理员能操作
    //借书
    int borrowBook(int bar_code,String username);
    //还书
    int lendBook(int bar_code,String username);

    //删除借阅记录
    void deleteBorrow(String lend_id);

    //管理员设置书籍逾期
    int setBookOvertime(String username,String lend_id);
    //管理员处理逾期
    int handleBookOvertime(String username, String lend_id);
}
