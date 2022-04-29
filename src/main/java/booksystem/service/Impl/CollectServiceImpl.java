package booksystem.service.Impl;

import booksystem.dao.CollectDao;
import booksystem.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CollectServiceImpl implements CollectService {

    @Autowired
    CollectDao collectDao;

    @Override
    public List<Map<String, Object>> getCollectionByUser(String username) {
        List<Map<String,Object>> res=collectDao.getCollectionByUser(username);
        for(Map<String,Object> result:res)
        {
            result.put("create_time",result.get("create_time").toString().replace('T',' '));
        }
        return res;
    }

    @Override
    public int getCollectionNum(String username) {
        return collectDao.getCollectionNum(username);
    }

    @Override
    public int addCollection(String reference_num,String username) {
        //先判断书籍有没有在收藏夹中
        if(collectDao.getBook(reference_num,username)!=null)
            return 0;
        //书籍没有被添加
        collectDao.addCollection(reference_num,username);
        return 1;
    }

    @Override
    public void deleteBook(String collection_id){
        collectDao.deleteBook(collection_id);
    }

    @Override
    public void deleteBooks(List<String> collection_ids) {
        collectDao.deleteBooks(collection_ids);
    }
}
