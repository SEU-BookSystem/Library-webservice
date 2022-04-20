package booksystem.service;

import booksystem.pojo.CartItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CartItemService {
    //根据username来获取购物车信息
    List<Map<String,Object>> getCartItem(String username);

    //添加商品
    int addCartItem(String book_id,String username,int sum);

    //更新商品
    int updateCartItem(String book_id,String username,int sum);

    //批量删除商品
    void multiDeleteCartItem(List<String> CartItem_Ids);
    void deleteCartItem(String cartItem_id);

    //获得购物车种类数
    int getCartItemNum(String username);
}
