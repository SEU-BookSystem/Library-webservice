package booksystem.dao;

import booksystem.pojo.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface CategoryDao {
    //添加目录
    void addCategory(Category category);

    //修改目录
    void updateCategory(Category category);

    //根据category_id查找目录
    Map<String,Object> getCategoryById(String category_id);
    //获取所有目录
    List<Map<String,Object>> getAllCategory();
    //获取指定分类 pid=”0“ 一级 pid=category_id 二级
    List<Map<String,Object>> getCategory(String category_id);
//    //获取指定一级分类下的二级分类
//    List<Map<String,Object>> getSecondCategory(String category_id);
//    //获取所有目录
//    List<Map<String,Object>> getAllCategory();
//    List<Map<String,Object>> getAllMainCategory();
//    String getMainCategory(String name);
//    String getSecondCategory(String main_name,String name);
//    Map<String,Object> getParentCategory(String id);
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
