package booksystem.service;

import java.util.List;
import java.util.Map;

public interface BookItemService {

    //根据入库数量添加图书
    int addBookItem(int num,String reference_num);

    //修改一本图书的信息
    void updateBookItem(int bar_code,String reference_num,int status,String address);
    void updateStatus(int bar_code,int status);

    //查询同一reference_num的图书
    List<Map<String,Object>> getBookItemByReferenceNum(String reference_num);
//    Map<String,Object> getBookItemByBarCode(int bar_code);

    //根据bar_code删除一本图书
    int deleteBookItem(int bar_code);
    //根据bar_code批量删除
    void deleteBookItems(List<Integer> bar_codes);

    //返回不同状态下的图书数量
    int getBookNumByStatus(int status);

    //批量图书上架
    void bookShelf(List<Integer> bar_codes);
    //批量图书下架
    void bookUnshelf(List<Integer> bar_codes);
}
