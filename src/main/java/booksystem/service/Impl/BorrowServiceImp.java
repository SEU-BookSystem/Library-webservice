package booksystem.service.Impl;

import booksystem.dao.BookItemDao;
import booksystem.dao.BorrowDao;
import booksystem.pojo.Borrow;
import booksystem.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BorrowServiceImp implements BorrowService {
    @Autowired
    BorrowDao borrowDao;
    @Autowired
    BookItemDao bookItemDao;

    @Override
    public int cancelReserve(String lend_id) {
        //将对应的状态改为5.预约失败
        borrowDao.updateStatus(lend_id,5,0);
        return 0;
    }

    @Override
    public int batCancelReserve(List<String> lend_ids) {
        for(String lend_id:lend_ids)
            borrowDao.updateStatus(lend_id,5,0);
        return 0;
    }

    //收藏夹预约
    @Override
    public int addCollectionReserve(List<String> reference_nums, String username) {
        //首先查询用户现在借阅书籍（已借未逾期+逾期+预约中）的数量  <7本
        int sum = borrowDao.getBorrowNumByUser(username);
        if((sum+reference_nums.size())<=7) {
            //书籍要求：在馆且无人借阅无人预约
            for (String reference_num : reference_nums) {
                List<Map<String, Object>> books = bookItemDao.getBookItemByReferenceNum(reference_num);
                boolean flag=false;
                int bar_code=0;
                for (Map<String, Object> book : books) {

                    //查找bookitem status为1.在馆的书籍
                    //预约成功
                    if (book.get("status").equals(1)) {
                        //生成预约记录信息 状态为4.预约成功
                        bar_code=Integer.parseInt(book.get("bar_code").toString());
                        flag=true;
                        break;
                    }
                }
                if(flag)
                {
                    //预约成功
                    //生成预约记录信息 状态为4.预约成功
                    Borrow borrow = new Borrow(null, username, null, null, bar_code,
                            0, 0, 4, null);
                    borrowDao.addBorrow(borrow);
                    //更改书籍状态为4.预约
                    bookItemDao.updateStatus(bar_code, 4);
                }else {
                    //预约失败
                    //生成预约信息 状态为5.预约失败
                    Borrow borrow = new Borrow(null, username, null, null,
                            Integer.parseInt(books.get(0).get("bar_code").toString()),
                            0, 0, 5, null);
                    borrowDao.addBorrow(borrow);
                }
            }
            return 1;
        }
        return -1;
    }

    //直接预约
    @Override
    public int addDirectReserve(String reference_num, String username) {
        //首先查询用户现在借阅书籍（已借未逾期+逾期）的数量  <7本
        int sum = borrowDao.getBorrowNumByUser(username);
        if (sum < 7) {
            //书籍要求：在馆且无人借阅无人预约
            List<Map<String, Object>> books = bookItemDao.getBookItemByReferenceNum(reference_num);

            for (Map<String, Object> book : books) {
                //查找bookitem status为1.在馆的书籍
                if (book.get("status").equals(1)) {
                    //生成预约记录信息
                    Borrow borrow=new Borrow(null,username,null,null,
                            Integer.parseInt(book.get("bar_code").toString()),
                            0,0,4,null);
                    borrowDao.addBorrow(borrow);
                    //更改书籍状态为4.预约
                    bookItemDao.updateStatus(Integer.parseInt(book.get("bar_code").toString()), 4);
                    return 1;
                }
            }
            return 0;
        }
        return -1;
    }

    //续借
    @Override
    public int renew(int bar_code, String end_time) {
        Map<String,Object> book=borrowDao.getBorrowByBarCode(bar_code);
        //1.判断这本书是否续借过 一本书只能续借一次
        if(book.get("is_lend_again").equals(0))
        {
            //2.判断这本书是否逾期 逾期不能续借
            if(book.get("status").equals(1))
            {
                borrowDao.renew(bar_code,end_time);
                return 1;
            }
            return 0;
        }
        return -1;
    }

    //借书
    @Override
    public int borrowBook(int bar_code, String username) {
        //书籍状态为 2.在库 3.预约
        Map<String,Object> book=bookItemDao.getBookItemByBarCode(bar_code);
        Map<String,Object> overtime=borrowDao.getOvertimeBorrow(username);
        if(book.get("status").equals(2)) {
            //用户 借阅数量不超过7本并且现在没有违规记录
            int sum = borrowDao.getBorrowNumByUser(username);
            //借阅数量小于7本
            if(sum<7) {
                //没有逾期记录
                if(overtime.isEmpty()) {
                    //生成借书信息，状态为1.借阅未逾期
                    Borrow borrow=new Borrow(null,username,null,null,bar_code,
                            0,1,1,null);
                    borrowDao.addBorrow(borrow);
                    //修改书籍状态为3.已借
                    bookItemDao.updateStatus(bar_code,3);
                    return 2;
                }
                return 1;//有逾期记录，不能借书
            }
            return 0;//超过最大借阅数
        }
        //被预约 判断预约人是否和借阅人相同
        if(book.get("status").equals(3)&&username==book.get("username"))
        {
            //没有违约记录
            if(overtime.isEmpty())
            {
                //将预约信息转变为借阅信息  将is_borrow=1 status=1
                borrowDao.updateStatus(book.get("lend_id").toString(),1,1);
                //修改书籍状态为3.已借
                bookItemDao.updateStatus(bar_code,3);
                return 2;
            }
            return 1;
        }
        return -1;//书籍不可借
    }

    //还书
    @Override
    public int lendBook(int bar_code, String username){
        Map<String,Object> book=borrowDao.getBorrowByBarCode(bar_code);
        //将借阅状态更改为3.已还
        borrowDao.updateStatus(book.get("lend_id").toString(), 3,1);
        //更新书籍状态为1.在库
        bookItemDao.updateStatus(bar_code, 1);
        return 1;
    }
}
