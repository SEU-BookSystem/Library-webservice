package booksystem.dao;



import booksystem.pojo.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface OrderDao {
    //根据username获取所有订单的id
    List<String> getAllOrderIDByUser(String username);
    //根据shop_id获取所有订单的id
    List<String> getAllOrderIDByShop(String shop_id);
    List<String> getAllOrderID(int start,int order_num,int status,int identity,String username);
    List<String> fuzzyOrderID(int start,int order_num,int status,String content);

    //通过order_id获取订单的东西
    Map<String,Object> getOrderByID(String order_id);

    //获取不同状态下的订单总数
    int getOrderNum(int status,int identity,String username);

    //status更新
    void updateStatus(int status,String order_id);

    //添加订单
    int addOrder(Order order);

    //时间更新
    void updateFirmTime(String order_id);
    void batUpdateFirmTime(List<String> Order_Ids);
    void updateSendTime(String order_id);
    void batUpdateSendTime(List<String> Order_Ids);

    //删除订单项
    void deleteOrder(List<String> Order_Ids);


    int fuzzyOrderCount(int status, String content);
}
