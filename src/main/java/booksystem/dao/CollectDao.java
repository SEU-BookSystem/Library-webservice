package booksystem.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface CollectDao {

    //根据username来获取收藏夹信息
    List<Map<String,Object>> getCollectionByUser(String username);

    //根据username获取用户收藏夹的总数
    int getCollectionNum(String username);

    //加入收藏夹
    void addCollection(String reference_num,String username);

//    //根据collection_id来查找特定用户收藏夹中的特定书籍
//    Map<String,Object> getBook(String collection_id);
    //根据username和reference_num查询
    Map<String,Object> getBook(String reference_num,String username);

    //删除
    void deleteBook(String collection_id);
    void deleteBooks(List<String> collection_ids);
}
