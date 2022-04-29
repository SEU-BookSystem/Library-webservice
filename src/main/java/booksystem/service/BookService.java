package booksystem.service;

import booksystem.pojo.Book;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BookService {
    //获取所有图书
    List<Map<String,Object>>  getAllBook();

    //删除
    void deleteBook(String isbn);
    void deleteBooks(List<String> isbns);

    //添加某种图书信息
    int addBook(String reference_num, String book_name, String author, String page_num,
                String price, String isbn, String detail, String publisher, String image,
                String date, String category_id, int num);

    //根据
//    //根据商家username获取图书
//    List<Book>getBookByShop(String username);
//    //根据订单id获取图书
//    List<Book>getBookByOrder(String order_id);
//    //根据目录名字获取图书
//    List<Book>getBookByCategory(String category);
//    //根据图书名获取图书
//    List<Book>getBookByName(String book_name);
//    //根据图书出版社获取图书
//    List<Book>getBookByPress(String press);

//    //单次添加
//    int addBook( String book_name, String author, double price, int repertory, String press, String edition, String print_time,String main_category_id,String second_category_id,String shop_id);
//    String selectBook( String book_name, String author, double price, String press, String edition, String print_time,String main_category_id,String second_category_id,String shop_id);
//    List<Map<String,Object>> getPage(int page_num,int book_num,int style,String main_id,String second_id,String year,String year_before,String year_after,String shop_id);
//    List<Map<String,Object>> fuzzyQuery(int page_num, int book_num, int style, int queryWhat, String content);

}
