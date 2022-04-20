package booksystem.dao;

import booksystem.pojo.CartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
//功能存疑 感觉方法少了又好像够了
public interface CartItemDao {

    //根据user_id来获取购物车信息
    List<Map<String,Object>> getCartItem(String username);

    //获取用户购物车的种数
    int getCartItemNum(String username);

    //加入购物车
    void addCartItem(String book_id,String user_id,int sum);

    //根据book_id和user_id查找购物车
    HashMap<String,Object> getBook(String book_id,String username);

    //根据cartItem来查找购物车
    List<Map<String,Object>> getCartItemByID(List<String> CartItem_Ids);

    //更新商品
    void updateCartItem(String book_id,String username,int sum,String create_time,String update_time);

    //删除商品
    void deleteCartItem(String cartItem_id);

    void multiDeleteCartItem(List<String> CartItem_Ids);
}
