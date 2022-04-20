package booksystem.dao;

import booksystem.pojo.Book;
import booksystem.pojo.OrderBook;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface OrderBookDao{
    //获取所有订单
    List<Map<String,Object>> getAllOrderBook();

    //通过username获取用户所有订单项
    List<Map<String,Object>> getAllOrderBookByUser(List<String> Order_Ids);

    //通过shop_id获取所有店铺的订单
    List<Map<String,Object>> getAllOrderBookByShop(List<String> Order_Ids);

    //通过order_id获取一个订单
    List<Map<String,Object>> getOrderBookByID(String order_id);


    //通过order_book_id获取order_book
    Map<String,Object> getBookByID(String order_book_id);

    //通过order_book_id获取book信息
    List<Map<String,Object>> getBook(String order_book_id);

    //书的评价
    void updateRemark(String order_book_id,String remark,double rate);

    //书的退换货
    void updateReturn(String order_book_id,String return_reason,String return_detail,int transport_status,int return_status,String exchange_address_id);

    //添加一个图书信息
    int addOrderBook(OrderBook orderBook);

    //更新退换状态
    void updateReturnStatus(String order_book_id,String check_opinion,int return_status);

    //删除订单书本项
    void deleteOrderBook(List<String> Order_Ids);

    //获取某本书的所有评价
    List<Map<String,Object>> getBookRemark(String book_id);

    //获取评论分页
    List<Map<String,Object>> getRemark(int page_num,int remark_num,int identity,String username);
    List<Map<String,Object>> getReback(int start, int order_num, int status, int identity, String username);
    int getRemarkNum(int identity,String username);

    int getRebackCount(int status, int identity, String username);
}
