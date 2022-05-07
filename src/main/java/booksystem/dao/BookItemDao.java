package booksystem.dao;

import booksystem.pojo.BookItem;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface BookItemDao {
    //添加一本图书
    void addBookItem(BookItem bookItem);

    //修改一本图书
    void updateBookItem(BookItem bookItem);
    void updateRef(String pre,String now);
    //修改图书的状态
    void updateStatus(int bar_code,int status);

    //根据bar_code删除一本图书
    void deleteBookItem(int bar_code);
    //根据bar_code批量删除
    void deleteBookItems(List<Integer> bar_codes);

    //根据bar_code查询一本图书
    Map<String,Object> getBookItemByBarCode(int bar_code);
    //根据reference_num查询某一种类的所有图书
    List< Map<String,Object>> getBookItemByReferenceNum(String Reference_num);

    //返回不同状态下书籍的数量
    int getBookItemNumByStatus(int status);
}
