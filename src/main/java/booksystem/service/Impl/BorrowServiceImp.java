package booksystem.service.Impl;

import booksystem.dao.*;
import booksystem.pojo.Borrow;
import booksystem.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BorrowServiceImp implements BorrowService {
    @Autowired
    BorrowDao borrowDao;
    @Autowired
    BookItemDao bookItemDao;
    @Autowired
    MessageDao messageDao;
    @Autowired
    UserDao userDao;
    @Autowired
    PunishDao punishDao;
    @Autowired
    BookDao bookDao;

    @Override
    public String[] countTime(int num){
        Date currdate=new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime=format.format(currdate).toString();
        Calendar ca = Calendar.getInstance();
        ca.setTime(currdate);
        ca.add(Calendar.DATE, num);
        currdate = ca.getTime();
        String enddate = format.format(currdate);
        return new String[]{startTime,enddate};
    }

    @Override
    public int cancelReserve(String lend_id) {
        //将对应的状态改为5.预约失败
        borrowDao.updateStatus(lend_id,5,0);
        //更新书籍的状态为2.在馆
        Map<String,Object> book=borrowDao.getById(lend_id);
        bookItemDao.updateStatus(Integer.parseInt(book.get("bar_code").toString()),2);
        return 0;
    }

    @Override
    public int batCancelReserve(List<String> lend_ids) {
        for(String lend_id:lend_ids) {
            borrowDao.updateStatus(lend_id, 5, 0);
            Map<String,Object> book=borrowDao.getById(lend_id);
            bookItemDao.updateStatus(Integer.parseInt(book.get("bar_code").toString()),2);
        }
        return 0;
    }

    //收藏夹预约
    @Override
    public Map<String,Object> addCollectionReserve(List<String> reference_nums, String username) {
        //存储库存不足的书
        ArrayList<String> book_names=new ArrayList<>();
        Map<String,Object> results=new HashMap<>();
        //首先查询用户现在借阅书籍（已借未逾期+逾期+预约中）的数量  <7本
        int sum = borrowDao.getBorrowNumByUser(username);
        if((sum+reference_nums.size())<=7) {
            //书籍要求：在馆且无人借阅无人预约
            for (String reference_num : reference_nums) {
                List<Map<String, Object>> books = bookItemDao.getBookItemByReferenceNum(reference_num);
                boolean flag=false;
                int bar_code=0;
                for (Map<String, Object> book : books) {

                    //查找bookitem status为2.可借的书籍
                    //预约成功
                    if (book.get("status").equals(2)) {
                        //生成预约记录信息 状态为4.预约成功
                        bar_code=Integer.parseInt(book.get("bar_code").toString());
                        flag=true;
                        break;
                    }
                }

                String str[]=countTime(1);

                if(flag)
                {
                    //预约成功
                    //生成预约记录信息 状态为4.预约成功
                    Borrow borrow = new Borrow(null, username, str[0], str[1], bar_code,
                            0, 0, 4, null);
                    borrowDao.addBorrow(borrow);
                    //更改书籍状态为4.预约
                    bookItemDao.updateStatus(bar_code, 4);
                    //发送预约成功信息
                    String book_name=bookDao.getBookNameByCode(bar_code);
                    messageDao.addMessage(username,"预约通知",
                            "尊敬的会员,您已成功预约《"+book_name+"》，注意预约时间，请在期限内到馆借阅，过期无效。");
                }else {
                    //预约失败
                    //生成预约信息 状态为5.预约失败
                    Borrow borrow = new Borrow(null, username, str[0], str[1],
                            Integer.parseInt(books.get(0).get("bar_code").toString()),
                            0, 0, 5, null);
                    borrowDao.addBorrow(borrow);
                    //发送预约失败信息
                    String book_name=bookDao.getBookNameByCode(Integer.parseInt(books.get(0).get("bar_code").toString()));
                    messageDao.addMessage(username,"预约通知",
                            "尊敬的会员,您所预约书籍《"+book_name+"》由于库存不足，暂时无法预约，对您造成的不便我们感到十分抱歉。");
                    book_names.add(book_name);
                }
            }
            if(book_names!=null) {
                results.put("result",0);
                results.put("data",Arrays.toString(book_names.toArray()).replace("[","").replace("]",""));
            }else {
                results.put("result",1);
                results.put("data",null);
            }
            return results;
        }
        results.put("result",-1);
        results.put("data",null);
        return results;
    }

    //直接预约
    @Override
    public int addDirectReserve(String reference_num, String username) {
        //首先查询用户现在借阅书籍（已借未逾期+逾期）的数量  <7本
        int sum = borrowDao.getBorrowNumByUser(username);
        if (sum < 7) {
            //书籍要求：在馆且无人借阅无人预约
            List<Map<String, Object>> books = bookItemDao.getBookItemByReferenceNum(reference_num);
            Map<String,Object> temp=bookDao.getBookByReferenceNum(reference_num);

            for (Map<String, Object> book : books) {
                //查找bookitem status为2.可借的书籍
                if (book.get("status").equals(2)) {
                    //生成预约记录信息
                    String str[]=countTime(1);
                    Borrow borrow=new Borrow(null,username,str[0],str[1],
                            Integer.parseInt(book.get("bar_code").toString()),
                            0,0,4,null);
                    borrowDao.addBorrow(borrow);
                    //更改书籍状态为4.预约
                    bookItemDao.updateStatus(Integer.parseInt(book.get("bar_code").toString()), 4);
                    String book_name=bookDao.getBookNameByCode(Integer.parseInt(book.get("bar_code").toString()));
                    messageDao.addMessage(username,"预约通知",
                            "尊敬的会员,您已成功预约书籍《"+book_name+"》，注意预约时间，请在期限内到馆借阅，过期无效。");
                    return 1;
                }
            }
            return 0;
        }
        return -1;
    }

    //续借
    @Override
    public int renew(int bar_code,String username) {
        Map<String,Object> book=borrowDao.getBorrowByBarCode(bar_code);
        //1.判断这本书是否续借过 一本书只能续借一次
        if(book.get("is_lend_again").equals(0))
        {
            //2.判断这本书是否逾期 逾期不能续借
            if(book.get("status").equals(1))
            {
                borrowDao.renew(bar_code);
                String book_name=bookDao.getBookNameByCode(bar_code);
                messageDao.addMessage(username,"借阅通知",
                        "尊敬的会员,您已成功续借《"+book_name+"》，请注意还书时间，请在期限内按时归还。");
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
        //获取用户所有逾期的记录
        List<Map<String,Object>> overtime=borrowDao.getOvertimeBorrow(username);

        if(book.get("status").equals(2)) {
            //用户 借阅数量不超过7本并且现在没有违规记录
            int sum = borrowDao.getBorrowNumByUser(username);
            //借阅数量小于7本
            if(sum<7) {
                //没有逾期记录
                if(overtime.isEmpty()) {
                    //生成借书信息，状态为1.借阅未逾期
                    String str[]=countTime(14);

                    Borrow borrow=new Borrow(null,username,str[0],str[1],bar_code,
                            0,1,1,null);
                    borrowDao.addBorrow(borrow);
                    //修改书籍状态为3.已借
                    bookItemDao.updateStatus(bar_code,3);
                    String book_name=bookDao.getBookNameByCode(bar_code);
                    messageDao.addMessage(username,"借阅通知",
                            "尊敬的会员,您已成功借阅《"+book_name+"》，注意还书时间，请在期限内按时归还。");
                    return 2;
                }
                return 1;//有逾期记录，不能借书
            }
            return 0;//超过最大借阅数
        }
        //被预约 判断预约人是否和借阅人相同
        if(book.get("status").equals(4))
        {
            Map<String,Object> temp=borrowDao.getReserveByBarCode(bar_code);
            if(username.equals(temp.get("username").toString())) {
                //没有违约记录
                if (overtime==null) {
                    //将预约信息转变为借阅信息  将is_borrow=1 status=1
                    String str[] = countTime(14);
                    borrowDao.deleteBorrow(temp.get("lend_id").toString());
                    Borrow borrow = new Borrow(null, username, str[0], str[1], bar_code,
                            1, 1, 1, null);
                    borrowDao.addBorrow(borrow);
                    //修改书籍状态为3.已借
                    bookItemDao.updateStatus(bar_code, 3);
                    String book_name=bookDao.getBookNameByCode(bar_code);
                    messageDao.addMessage(username, "借阅通知",
                            "尊敬的会员,您已成功借阅《"+book_name+"》，注意还书时间，请在期限内按时归还。");
                    return 2;
                }
                return 1;
            }
            return -1;
        }
        return -1;//书籍不可借
    }

    //还书
    @Override
    public int lendBook(int bar_code, String username){
        Map<String,Object> book=borrowDao.getBorrowByBarCode(bar_code);
        if(book!=null) {
            //将借阅状态更改为3.已还
            borrowDao.updateStatus(book.get("lend_id").toString(), 3, 1);
            //更新书籍状态为1.在库
            bookItemDao.updateStatus(bar_code, 1);
            String book_name=bookDao.getBookNameByCode(bar_code);
            messageDao.addMessage(username, "借阅通知",
                    "尊敬的会员,您已成功归还《"+book_name+"》，感谢您的配合。");
            return 1;
        }
        return 0;
    }

    @Override
    public void deleteBorrow(String lend_id) {
        borrowDao.deleteBorrow(lend_id);
    }

    @Override
    public int setBookOvertime(String username, String lend_id) {
        //将书籍状态从1.借阅未逾期 更改为2.逾期
        borrowDao.updateStatus(lend_id,2,1);
        //将用户状态从0.正常设置为1.违约
        userDao.updateStatus(username,1);
        //产生违规信息
        Map<String,Object> book=borrowDao.getById(lend_id);
        punishDao.addPunish(username,Integer.parseInt(book.get("bar_code").toString()),1,0,null,null);
        //发送逾期提醒
        String book_name=bookDao.getBookNameByCode(Integer.parseInt(book.get("bar_code").toString()));
        messageDao.addMessage(username,"借阅通知",
                "尊敬的会员,您借阅的书籍《"+book_name+"》已逾期，请尽早归还，谢谢您的配合。");
        return 0;
    }



    @Override
    public int handleBookOvertime(String username, String lend_id) {
        List<Map<String,Object>> overtime=borrowDao.getOvertimeBorrow(username);
        if(overtime.size()==1) {
            userDao.updateStatus(username,0);
        }
        return 0;
    }

    @Override
    public int ReserveOvertime(String username, String lend_id) {
        //将借阅状态更改为5.预约失败
        borrowDao.updateStatus(lend_id,5,0);
        //将书籍状态更改为2.可借
        Map<String,Object> book=borrowDao.getById(lend_id);
        bookItemDao.updateStatus(Integer.parseInt(book.get("bar_code").toString()),2);
        //发送过期消息
        String book_name=bookDao.getBookNameByCode(Integer.parseInt(book.get("bar_code").toString()));
        messageDao.addMessage(username,"借阅通知",
                "尊敬的会员,您借阅的书籍《"+book_name+"》已逾期，请尽早归还，谢谢您的配合。");
        return 0;
    }

    @Override
    public int getNumByStatus(int number) {
        int num=0;
        //number=1 表示正在预约
        if(number==1) {
            num=borrowDao.getNumByStatus(4);
        }else if(number==2){
            //正在借阅（1 2）
            num=borrowDao.getNumByStatus(1)+borrowDao.getNumByStatus(2);
        }else if(number==3){
            //3 历史借阅
            num=borrowDao.getNumByStatus(3);
        }
        return num;
    }
}
