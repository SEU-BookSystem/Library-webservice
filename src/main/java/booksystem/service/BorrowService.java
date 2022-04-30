package booksystem.service;

import java.util.List;

public interface BorrowService {

    //预约   用户、管理员都能操作
    //取消预约
    int cancelReserve(String lend_id);
    //批量取消预约
    int batCancelReserve(List<String> lend_ids);

    //收藏夹预约
    int addCollectionReserve(List<String> reference_nums,String user_id);
    //直接预约
    int addDirectReserve(String reference_num,String user_id);

    //续借
    int renew(int bar_code,String user_id);

    //借阅 只有管理员能操作
    //借书
    int borrowBook(int bar_code,String user_id);
    //还书
    int lendBook(int bar_code,String user_id);

}
