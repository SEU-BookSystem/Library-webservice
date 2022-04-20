package booksystem.service;

import booksystem.pojo.Book;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface OrderBookService {

    //通过order_id获取订单的详细信息
    Map<String,Object> getOrderByOrderID(String order_id);
    //根据username获取所有订单
    List<Map<String,Object>> getAllOrderByUser(String username);
    //根据shop_id获取所有订单
    List<Map<String,Object>> getAllOrderByShop(String shop_id);
    //获取所有的订单
    List<Map<String,Object>> getAllOrder();

    List<Map<String,Object>> getOrder(int start,int order_num,int status,int identity,String username);


    //通过order_book_id获取orderBook
    Map<String,Object> getBookByID(String order_book_id);

    //用户对某本书的评价
    int updateRemark(String order_book_id,String remark,double rate);

    //某本书的退款
    int returnBook(String order_book_id, String return_reason, String return_detail, int transport_status);
    //某本书的换货
    int exchangeBook(String order_book_id,String return_reason,String return_detail,int transport_status,String exchange_address_id);
    //某本书的退货退款
    int returnBook(String order_book_id,String return_reason,String return_detail);


    //同意退换货
    int refundOrder(String order_book_id,int return_status);
    //批量同意退换货
    int batRefundOrder(List<String> Order_Book_Ids,int return_status);

    //拒绝退换货
    int failRefundOrder(String order_book_id,String check_opinion,int return_status);
    //批量拒绝退换货
    int batFailRefundOrder(List<Map<String,Object>> checkList,int return_status);


    //获取某本书的所有评价
    List<Map<String,Object>> getBookRemark(String book_id);

    List<Map<String,Object>> getRemark(int page_num,int remark_num,int identity,String username);
    int getRemarkNum(int identity,String username);

    List<Map<String,Object>> fuzzyOrder(int start, int order_num, int status, String content);

    Object getReback(int start,int order_num, int status,int identity, String username);

    //某一个店铺的所有退款订单信息

    //某一个退款订单的详情

    //某一个店铺的所有退款订单信息

    //某一个退款订单的详情
}
