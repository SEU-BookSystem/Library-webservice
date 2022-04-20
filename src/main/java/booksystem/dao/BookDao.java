package booksystem.dao;

import booksystem.pojo.Book;
import booksystem.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface BookDao {

    //获取所有图书
    HashMap<String,Object> getAllBook();


    //添加
    int addBook(Book book);
    //查找用户
    String selectBook( String book_name, String author, double price, String press, String edition, String print_time,String main_category_id,String second_category_id,String shop_id);
    //删除
    int deleteBook(String book_id);
    int deleteBooks(List<String> book_ids);
    //更新图书信息
    int updateBook(String book_id,String book_name, String author, double price, int repertory, String press, String edition, String print_time,String main_category_id,String second_category_id,String shop_id);
    void updateDetail(String book_id,String detail);
    //根据book_id获取图书
    HashMap<String,Object> getBookByID(String book_id);

    List<Map<String,Object>> getPage(int start, int book_num, int style, String main_id, String second_id, String year,String year_before,String year_after,String shop_id);
    List<Map<String,Object>> fuzzyQuery(int start, int book_num, int style, int queryWhat, String content);

    void updateVolume(String book_id,int volume);//更新销量
    void updateRepertory(String book_id,int repertory);//更新库存

    Map<String,Object> getDetail(String book_id);

    int getPageCount(String main_category_id, String second_category_id, String year, String year_before, String year_after, String shop_id);


    int fuzzyQueryCount(int queryWhat, String content);


    void updateStatus(String shop_id);

}
