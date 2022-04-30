package booksystem.service;

import booksystem.pojo.Category;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    //添加目录
    void addCategory(Category category);

    //修改目录
    void updateCategory(Category category);

    //根据category_id查找目录
    Map<String,Object> getCategoryById(String category_id);

    //返回所有目录分类
    List<Map<String,Object>> getAllCategory();

    //返回所有的一级分类
    List<Map<String,Object>> getMainCategory();

    //返回特定一级分类的二级分类
    List<Map<String,Object>> getSecondeCategory(String category_id);
//    //获取所有目录
//    List<Map<String,Object>> getAllCategory();
//    String getMainCategory(String name);
//    String getSecondCategory(String main_name,String name);
//    //添加目录
//    void addMainCategory(String name);
//    void addSecondCategory(String main_name,String name);
//    //删除目录
//    void deleteMainCategory(String id);
//    void deleteSecondCategory(String id);
//    //更新目录名
//    void updateMainCategory(String id,String name);
//    void updateSecondCategory(String id,String name);
//    //通过二级目录和一级目录查找
//    Map<String,Object> getCategory(String id,String category_id);
}
