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

    //获取所有种图书
    List<Map<String,Object>> getAllBook();
    //根据书名查询图书
    Map<String,Object> getBookByName(String bookname);
    //根据出版社查询图书
    Map<String,Object> getBookByPublisher(String publisher);
    //根据isbn号查找图书
    Map<String,Object> getBookByIsbn(String isbn);
    //根据reference_num查找图书
    Map<String,Object> getBookByReferenceNum(String reference_num);

    //获取多少天以来被借阅过的图书
    Map<String,Object> getBorrowBook(int day);

    //添加一种书
    void addBook(Book book);
    //删除
    void deleteBook(String isbn);
    void deleteBooks(List<String> isbns);
    //更新图书信息
    void updateBook(Book book);

    //查找用户
//    String selectBook( String book_name, String author, double price, String press, String edition, String print_time,String main_category_id,String second_category_id,String shop_id);
//    void updateDetail(String book_id,String detail);

//    HashMap<String,Object> getBookByID(String book_id);

    //分页和模糊查询
    List<Map<String,Object>> getPage(int start, int book_num, int style, String main_id, String second_id, String year,String year_before,String year_after,String shop_id);
    List<Map<String,Object>> fuzzyQuery(int start, int book_num, int style, int queryWhat, String content);

    int getPageCount(String main_category_id, String second_category_id, String year, String year_before, String year_after, String shop_id);
    int fuzzyQueryCount(int queryWhat, String content);

    //有关联错误待删
//    void updateStatus(String shop_id);
}
