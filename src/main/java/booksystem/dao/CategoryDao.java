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
    //获取所有目录
    List<Map<String,Object>> getAllCategory();
    List<Map<String,Object>> getAllMainCategory();
    String getMainCategory(String name);
    String getSecondCategory(String main_name,String name);
    Map<String,Object> getParentCategory(String id);
    //添加目录
    void addMainCategory(String name);
    void addSecondCategory(String main_name,String name);
    //删除目录
    void deleteMainCategory(String id);
    void deleteSecondCategory(String id);
    //更新目录名
    void updateMainCategory(String id,String name);
    void updateSecondCategory(String id,String name);
    //通过二级目录和一级目录查找
    Map<String,Object> getCategory(String id,String category_id);
}
