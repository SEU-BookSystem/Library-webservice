package booksystem.service.Impl;

import booksystem.dao.CategoryDao;
import booksystem.pojo.Category;
import booksystem.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryDao categoryDao;

    @Override
    public void addCategory(Category category) {
        categoryDao.addCategory(category);
    }

    @Override
    public void updateCategory(Category category) {
        categoryDao.updateCategory(category);
    }

    @Override
    public Map<String, Object> getCategoryById(String category_id) {
        return categoryDao.getCategoryById(category_id);
    }

    @Override
    public List<Map<String, Object>> getAllCategory() {
        return categoryDao.getAllCategory();
    }

    @Override
    public List<Map<String, Object>> getMainCategory() {
        return categoryDao.getCategory("0");
    }

    @Override
    public List<Map<String, Object>> getSecondeCategory(String category_id) {
        return categoryDao.getCategory(category_id);
    }

//    @Autowired
//    CategoryDao categoryDao;
//
//    @Override
//    public List<Map<String,Object>> getAllCategory() {
//        List<Map<String,Object>>allList=categoryDao.getAllCategory();
//        List<Map<String,Object>>mainList=categoryDao.getAllMainCategory();
//        for(int i=0;i<mainList.size();i++){
//            List<Map<String,Object>>res=new ArrayList<>();
//            for(int j=0;j<allList.size();j++){
//                if(mainList.get(i).get("main_id").toString().equals(allList.get(j).get("main_id").toString())){
//                    Map<String,Object>map=new HashMap<>();
//                    map.put("second_name",allList.get(j).get("second_name").toString());
//                    map.put("second_id",allList.get(j).get("second_id").toString());
//                    map.put("book_num",allList.get(j).get("book_num").toString());
//                    res.add(map);
//                }
//            }
//
//            mainList.get(i).put("second_category",res);
//        }
//        return mainList;
//    }
//
//    @Override
//    public String getMainCategory(String name) {
//        return categoryDao.getMainCategory(name);
//    }
//
//    @Override
//    public String getSecondCategory(String main_name,String name) {
//        return categoryDao.getSecondCategory(main_name,name);
//    }
//
//    @Override
//    public void addMainCategory(String name) {
//        categoryDao.addMainCategory(name);
//    }
//
//    @Override
//    public void addSecondCategory(String main_name, String name) {
//        categoryDao.addSecondCategory(main_name,name);
//    }
//
//    @Override
//    public void deleteMainCategory(String id) {
//        categoryDao.deleteMainCategory(id);
//    }
//
//    @Override
//    public void deleteSecondCategory(String id) {
//        categoryDao.deleteSecondCategory(id);
//    }
//
//    @Override
//    public void updateMainCategory(String id, String name) {
//        categoryDao.updateMainCategory(id,name);
//    }
//
//    @Override
//    public void updateSecondCategory(String id, String name) {
//        categoryDao.updateSecondCategory(id,name);
//    }
//
//    @Override
//    public Map<String, Object> getCategory(String id, String category_id) {
//        return null;
//    }
}
