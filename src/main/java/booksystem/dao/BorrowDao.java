package booksystem.dao;

import booksystem.pojo.Borrow;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface BorrowDao {
    //添加借阅记录
    void addBorrow(Borrow borrow);

    //查询用户在借阅书籍的数量(已借未逾期+逾期+预约中)
    int getBorrowNumByUser(String username);
    //查询用户的逾期记录
    Map<String,Object> getOvertimeBorrow(String username);
    //根据条码号查询借阅信息
    Map<String,Object> getBorrowByBarCode(int bar_code);

    //更新状态
    void updateStatus(String lend_id,int status,int is_borrow);
    void renew(int bar_code,String end_time);
}
