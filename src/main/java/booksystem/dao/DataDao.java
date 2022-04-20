package booksystem.dao;


import booksystem.pojo.Book;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface DataDao {
    List<Map<String,Object>> getData(int start,int data_num);
    void addBook(String book_name,String author,double price,int volume,
                   int repertory,String press,String edition,String print_time,
                   String image_b,String image_s,String main_category_id,
                   String second_category_id,String shop_id,String detail);
}
