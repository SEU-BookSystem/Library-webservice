package booksystem.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

//上传图片等
@Repository
@Mapper
public interface UploadImgDao {
    void updateUserImgUrl(String username,String url_b,String url_s);
    void updateShopImgUrl(String shop_id,String url_b,String url_s);
    void updateBookImgUrl(String book_id,String url_b,String url_s);
    void updateReturnImgUrl(String order_book_id,String url_b,String url_s);
    HashMap<String,Object> getUserImgUrl(String username);
    HashMap<String,Object> getShopImgUrl(String username);
    HashMap<String,Object> getUserImgUrlById(String user_id);
    HashMap<String,Object> getShopImgUrlById(String shop_id);
    HashMap<String,Object> getBookImgUrlById(String book_id);
    HashMap<String,Object> getReturnImgUrlById(String order_book_id);
}
