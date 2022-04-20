package booksystem.service.Impl;

import booksystem.dao.BookDao;
import booksystem.dao.OrderBookDao;
import booksystem.dao.OrderDao;
import booksystem.service.OrderBookService;
import booksystem.utils.OrderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderBookServiceImpl implements OrderBookService{
    @Autowired
    OrderBookDao  orderBookDao;
    @Autowired
    OrderDao orderDao;
    @Autowired
    BookDao bookDao;

    @Override
    public Map<String,Object> getOrderByOrderID(String order_id) {
        List<Map<String, Object>> mapList=orderBookDao.getOrderBookByID(order_id);
        return OrderUtils.OrderOutput(mapList).get(0);
    }

    @Override
    public List<Map<String, Object>> getAllOrderByUser(String username) {
        List<String> Order_Ids=orderDao.getAllOrderIDByUser(username);
        List<Map<String,Object>> mapList=orderBookDao.getAllOrderBookByUser(Order_Ids);
        List<Map<String, Object>> temp=new ArrayList<>();
        if(mapList.isEmpty()){
            return temp;
        }
        return OrderUtils.OrderOutput(mapList);
    }

    @Override
    public List<Map<String, Object>> getOrder(int start,int order_num,int status,int identity,String username) {
        List<String> Order_Ids=orderDao.getAllOrderID(start,order_num,status,identity,username);
        List<Map<String, Object>> temp=new ArrayList<>();
        if(Order_Ids.isEmpty()){
            return temp;
        }
        List<Map<String,Object>> mapList=orderBookDao.getAllOrderBookByUser(Order_Ids);
        return OrderUtils.OrderOutput(mapList);
    }

    @Override
    public List<Map<String, Object>> getAllOrderByShop(String shop_id) {
        List<String> Order_Ids=orderDao.getAllOrderIDByShop(shop_id);
        List<Map<String,Object>> mapList=orderBookDao.getAllOrderBookByShop(Order_Ids);
        List<Map<String, Object>> temp=new ArrayList<>();
        if(mapList.isEmpty()){
            return temp;
        }
        return OrderUtils.OrderOutput(mapList);
    }

    @Override
    public List<Map<String,Object>> getAllOrder() {
        List<Map<String,Object>> mapList=orderBookDao.getAllOrderBook();
        List<Map<String, Object>> temp=new ArrayList<>();
        if(mapList.isEmpty()){
            return temp;
        }
        return OrderUtils.OrderOutput(mapList);
    }

    @Override
    public Map<String, Object> getBookByID(String order_book_id) {
        List<Map<String,Object>> mapList=orderBookDao.getBook(order_book_id);
        return OrderUtils.OrderOutput(mapList).get(0);
    }

    @Override
    public int updateRemark(String order_book_id, String remark, double rate) {
        Map<String,Object> orderBook=orderBookDao.getBookByID(order_book_id);
        if(orderBook==null)
            return -1;//订单不存在
        String order_id=orderBook.get("order_id").toString();
        List<Map<String,Object>> order=orderBookDao.getOrderBookByID(order_id);
        if(order.isEmpty())
            return -1;//订单不存在

        //判断取消订单是在 确认订单之后的时候  评价状态为0 未评价
        if(5==Integer.valueOf(order.get(0).get("status").toString())&&
                Integer.valueOf(orderBook.get("remark_status").toString())==0)//差一个7天后默认好评
        {
            orderBookDao.updateRemark(order_book_id,remark,rate);
            return 1;
        }
        return 0;//处于其他状态不可取消
    }

    @Override
    public int returnBook(String order_book_id, String return_reason, String return_detail, int transport_status) {
        //检查状态
        Map<String,Object> orderBook=orderBookDao.getBookByID(order_book_id);
        String order_id=orderBook.get("order_id").toString();
        Map<String,Object> order=orderDao.getOrderByID(order_id);
        if(Integer.valueOf(order.get("status").toString())<=3)
            return -1;//未付款

        //当确认订单之后 七天之内才能退货
        if (Integer.valueOf(order.get("status").toString()) == 5) {
            try {

                String firm_time = order.get("firm_time").toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
                Date firm = simpleDateFormat.parse(firm_time);
                Date nowDate = new Date(System.currentTimeMillis());
                long difference = nowDate.getTime() - firm.getTime();
                if (difference >= (1000 * 60 * 60 * 24 * 7))
                    return -2;//超时了
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //退款状态 -1无效
        if(Integer.valueOf(orderBook.get("return_status").toString())!=-1)
            return -1;//
        orderBookDao.updateReturn(order_book_id,return_reason,return_detail,transport_status,1,null);
        return 1;
    }

    @Override
    public int exchangeBook(String order_book_id, String return_reason, String return_detail, int transport_status,String exchange_address_id) {
        //检查状态
        Map<String,Object> orderBook=orderBookDao.getBookByID(order_book_id);
        String order_id=orderBook.get("order_id").toString();
        Map<String,Object> order=orderDao.getOrderByID(order_id);
        if(Integer.valueOf(order.get("status").toString())<=3)
            return -1;//未付款

        //当确认订单之后 七天之内才能退货
        if (Integer.valueOf(order.get("status").toString()) == 5) {
            try {

                String firm_time = order.get("firm_time").toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
                Date firm = simpleDateFormat.parse(firm_time);
                Date nowDate = new Date(System.currentTimeMillis());
                long difference = nowDate.getTime() - firm.getTime();
                if (difference >= (1000 * 60 * 60 * 24 * 7))
                    return -2;//超时了
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //退款状态 -1无效
        if(Integer.valueOf(orderBook.get("return_status").toString())!=-1)
            return -1;//
        orderBookDao.updateReturn(order_book_id,return_reason,return_detail,transport_status,4,exchange_address_id);
        return 1;
    }

    @Override
    public int returnBook(String order_book_id, String return_reason, String return_detail) {
        //检查状态
        Map<String,Object> orderBook=orderBookDao.getBookByID(order_book_id);
        String order_id=orderBook.get("order_id").toString();
        Map<String,Object> order=orderDao.getOrderByID(order_id);
        if(Integer.valueOf(order.get("status").toString())<=3)
            return -1;//未付款

        //当确认订单之后 七天之内才能退货
        if (Integer.valueOf(order.get("status").toString()) == 5) {
            try {

                String firm_time = order.get("firm_time").toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
                Date firm = simpleDateFormat.parse(firm_time);
                Date nowDate = new Date(System.currentTimeMillis());
                long difference = nowDate.getTime() - firm.getTime();
                if (difference >= (1000 * 60 * 60 * 24 * 7))
                    return -2;//超时了
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //退款状态 -1无效
        if(Integer.valueOf(orderBook.get("return_status").toString())!=-1)
            return -1;//
        orderBookDao.updateReturn(order_book_id,return_reason,return_detail,2,7,null);
        return 1;
    }

    @Override
    public int refundOrder(String order_book_id,int return_status) {
        Map<String,Object> bookMap=orderBookDao.getBookByID(order_book_id);
        if (Integer.valueOf(bookMap.get("return_status").toString()) != 7&&Integer.valueOf(bookMap.get("return_status").toString()) != 1&&Integer.valueOf(bookMap.get("return_status").toString()) != 4)//申请退货未审核
            return -1;
        //更改状态
        orderBookDao.updateReturnStatus(order_book_id,null,return_status);
        return 1;
    }

    @Override
    public int batRefundOrder(List<String> Order_Book_Ids,int return_status) {
        for(String order_book_id:Order_Book_Ids) {
            Map<String, Object> bookMap = orderBookDao.getBookByID(order_book_id);
            if (Integer.valueOf(bookMap.get("return_status").toString()) != 7&&Integer.valueOf(bookMap.get("return_status").toString()) != 1&&Integer.valueOf(bookMap.get("return_status").toString()) != 4)
                return -1;
        }
        //更改状态
        for(String order_book_id:Order_Book_Ids)
            orderBookDao.updateReturnStatus(order_book_id,null,return_status);
        return 1;
    }

    @Override
    public int failRefundOrder(String order_book_id, String check_opinion,int return_status) {
        Map<String,Object> bookMap=orderBookDao.getBookByID(order_book_id);
        if (Integer.valueOf(bookMap.get("return_status").toString()) != 7
                &&Integer.valueOf(bookMap.get("return_status").toString()) != 1
                &&Integer.valueOf(bookMap.get("return_status").toString()) != 4)
            return -1;
        //更改状态
        orderBookDao.updateReturnStatus(order_book_id,check_opinion,return_status);
        return 1;
    }

    @Override
    public int batFailRefundOrder(List<Map<String, Object>> checkList,int return_status) {
        for(Map<String,Object> check:checkList) {
            String order_book_id=check.get("order_book_id").toString();
            Map<String, Object> bookMap = orderBookDao.getBookByID(order_book_id);
            if (Integer.valueOf(bookMap.get("return_status").toString()) != 7&&Integer.valueOf(bookMap.get("return_status").toString()) != 1&&Integer.valueOf(bookMap.get("return_status").toString()) != 4)
                return -1;
        }
        //更改状态
        for(Map<String,Object> check:checkList) {
            String order_book_id = check.get("order_book_id").toString();
            String check_opinion=check.get("check_opinion").toString();
            orderBookDao.updateReturnStatus(order_book_id, check_opinion, return_status);
        }
        return 1;
    }

    @Override
    public List<Map<String, Object>> getBookRemark(String book_id) {
        List<Map<String,Object>> remarkList=orderBookDao.getBookRemark(book_id);
        if(!remarkList.isEmpty()) {
            for (Map<String, Object> remark : remarkList) {
                remark.put("remark_time",remark.get("remark_time").toString().replace("T", " "));
            }
        }
        return remarkList;
    }

    @Override
    public List<Map<String, Object>> getRemark(int page_num, int remark_num, int identity, String username) {
        List<Map<String,Object>> remark=orderBookDao.getRemark(page_num,remark_num,identity,username);
        System.out.println(remark);
        List<Map<String,Object>>res=new ArrayList<>();
        for(int i=0;i<remark.size();i++){
            HashMap<String,Object>tmp=new HashMap<>();
            tmp.put("remark",remark.get(i));
            remark.get(i).put("remark_time",remark.get(i).get("remark_time").toString().replace("T"," "));
            Map<String,Object>book=bookDao.getBookByID(remark.get(i).get("book_id").toString());
            book.put("update_time",book.get("update_time").toString().replace("T"," "));
            book.put("create_time",book.get("create_time").toString().replace("T"," "));
            book.put("print_time",book.get("print_time").toString().replace("T"," "));
            tmp.put("book",book);
            res.add(tmp);
        }
        return res;
    }

    @Override
    public int getRemarkNum(int identity, String username) {
        return orderBookDao.getRemarkNum(identity,username);
    }

    @Override
    public List<Map<String,Object>> fuzzyOrder(int start, int order_num, int status, String content) {
        List<String> Order_Ids=orderDao.fuzzyOrderID(start,order_num,status,content);
        List<Map<String, Object>> temp=new ArrayList<>();
        if(Order_Ids.isEmpty()){
            return temp;
        }
        List<Map<String,Object>> mapList=orderBookDao.getAllOrderBookByUser(Order_Ids);
        return OrderUtils.OrderOutput(mapList);
    }

    @Override
    public List<Map<String,Object>> getReback(int start, int order_num, int status, int identity, String username) {
        List<Map<String, Object>> list=orderBookDao.getReback(start,order_num,status,identity,username);
        List<Map<String, Object>> res=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            list.get(i).put("remark_time",list.get(i).get("remark_time").toString().replace("T"," "));
            list.get(i).put("return_time",list.get(i).get("return_time").toString().replace("T"," "));
            list.get(i).put("firm_time",list.get(i).get("firm_time").toString().replace("T"," "));
            Map<String,Object>book=bookDao.getBookByID(list.get(i).get("book_id").toString());
            book.put("update_time",book.get("update_time").toString().replace("T"," "));
            book.put("create_time",book.get("create_time").toString().replace("T"," "));
            book.put("print_time",book.get("print_time").toString().replace("T"," "));
            list.get(i).put("books",book);
            res.add(list.get(i));
        }
        return res;
    }
}
