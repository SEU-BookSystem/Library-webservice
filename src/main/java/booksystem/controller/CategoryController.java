package booksystem.controller;

import booksystem.dao.CategoryDao;
import booksystem.pojo.Category;
import booksystem.service.CategoryService;
import booksystem.utils.Result;
import booksystem.utils.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryDao categoryDao;

    //获取所有目录
    @RequestMapping("/category/getAll")
    public Result getAllCategory(){
        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",categoryService.getAllCategory());
    }

    //添加一级目录
    @PostMapping("/admin/category/addMain")
    public Result addMainCategory(@RequestParam("main_name") String name){
        if(name.isEmpty())
        {
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }
        String id=categoryService.getMainCategory(name);
        if(!(id==null)){
            return Result.error(ResultEnum.REPEAT_ADD.getCode(),ResultEnum.REPEAT_ADD.getMsg());
        }
        categoryService.addMainCategory(name);
        return Result.ok(ResultEnum.SUCCESS.getMsg());
    }

    //添加二级目录
    @PostMapping("/admin/category/addSecond")
    public Result addSecondCategory(@RequestParam("main_name") String main_name,
                                    @RequestParam("second_name") String name){
        if(name.isEmpty()||main_name.isEmpty())
        {
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }
        String main_id=categoryService.getMainCategory(main_name);
        if(main_id==null){
            categoryService.addMainCategory(main_name);
        }
        String id=categoryService.getSecondCategory(main_name,name);
        if(!(id==null)){
            return Result.error(ResultEnum.REPEAT_ADD.getCode(),ResultEnum.REPEAT_ADD.getMsg());
        }
        categoryService.addSecondCategory(main_name,name);
        return Result.ok(ResultEnum.SUCCESS.getMsg());
    }

    //删除一级目录
    @DeleteMapping("/admin/category/deleteMain")
    public Result deleteMainCategory(@RequestParam("category_id") String category_id){
        categoryService.deleteMainCategory(category_id);
        return Result.ok(ResultEnum.SUCCESS.getMsg());
    }

    //删除二级目录
    @DeleteMapping("/admin/category/deleteSecond")
    public Result deleteSecondCategory(@RequestParam("category_id") String category_id){
        categoryService.deleteSecondCategory(category_id);
        return Result.ok(ResultEnum.SUCCESS.getMsg());
    }

    @PostMapping("/admin/category/updateMain")
    public Result updateMainCategory(@RequestParam("category_id") String category_id,
                                     @RequestParam("category_name") String name){
        String id=categoryService.getMainCategory(name);
        if(!(id==null)&&!id.equals(category_id)){
            return Result.error(ResultEnum.REPEAT_ADD.getCode(),ResultEnum.REPEAT_ADD.getMsg());
        }
        categoryService.updateMainCategory(category_id,name);
        return Result.ok(ResultEnum.SUCCESS.getMsg());
    }

    @PostMapping("/admin/category/updateSecond")
    public Result updateSecondCategory(@RequestParam("category_id") String category_id,
                                     @RequestParam("category_name") String name){
        String main_name=categoryDao.getParentCategory(category_id).get("category_name").toString();
        String id=categoryService.getSecondCategory(main_name,name);
        if(!(id==null)&&!id.equals(category_id)){
            return Result.error(ResultEnum.REPEAT_ADD.getCode(),ResultEnum.REPEAT_ADD.getMsg());
        }
        categoryService.updateSecondCategory(category_id,name);
        return Result.ok(ResultEnum.SUCCESS.getMsg());
    }


}
