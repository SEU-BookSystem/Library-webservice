package booksystem.service;


import java.util.List;
import java.util.Map;

public interface CollectService {
    //根据username获取用户收藏夹
    List<Map<String,Object>> getCollectionByUser(String username);

    //返回用户收藏夹数量
    int getCollectionNum(String username);

    //加入收藏夹
    int addCollection(String reference_num,String username);

    //删除
    void deleteBook(String collection_id);
    void deleteBooks(List<String> collection_ids);




}
