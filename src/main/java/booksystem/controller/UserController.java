package booksystem.controller;

import booksystem.dao.UserDao;
import booksystem.pojo.User;
import booksystem.service.UploadImgService;
import booksystem.service.UserService;
import booksystem.utils.Result;
import booksystem.utils.ResultEnum;
import booksystem.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    UploadImgService uploadImgService;
    @Autowired
    UserDao userDao;
    //获取所有用户
    @RequestMapping("/admin/getAllUser")
    public Result getAllUser(){
        List<Map<String,Object>> result=userDao.getAllUser();
        for(int i=0;i<result.size();i++){
            result.get(i).put("access_time",result.get(i).get("access_time").toString()
                    .replace('T',' '));
            result.get(i).put("create_time",result.get(i).get("create_time").toString()
                    .replace('T',' '));
            result.get(i).put("update_time",result.get(i).get("update_time").toString()
                    .replace('T',' '));
        }

        if(!result.isEmpty())
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",result);
        }else{
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }
    }

    @RequestMapping("/admin/getUsers")
    public Result getUsers(@RequestParam("page_num")int page_num,
                           @RequestParam("each_num")int book_num){
        if(page_num<=0||book_num<=0){
            return Result.error(33,"数据必须为正");
        }
        int count=userDao.getUsersCount();
        int p_count=(count%book_num==0)?(count/book_num):(count/book_num+1);
        if(page_num>p_count&&p_count!=0){
            return Result.error(34,"页数超过范围");
        }
        List<Map<String,Object>> result=userDao.getUsers((page_num-1)*book_num,book_num);
        for(int i=0;i<result.size();i++){
            result.get(i).put("create_time",result.get(i).get("create_time").toString()
                    .replace('T',' '));
            result.get(i).put("update_time",result.get(i).get("update_time").toString()
                    .replace('T',' '));
            result.get(i).put("access_time",result.get(i).get("access_time").toString()
                    .replace('T',' '));
        }

        if(!result.isEmpty())
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg()).put("page_count",p_count).put("data",result);
        }else{
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }
    }

    //获取所有用户数量
    @RequestMapping("/admin/getAllUserNum")
    public Result getAllUserNum(){
        int result=userService.getAllUserNum();
        if(result>=0)
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",result);
        }else{
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(),ResultEnum.UNKNOWN_ERROR.getMsg());
        }
    }

    //用户username查找用户
    @RequestMapping("/user/getByUsername")
    public Result getUserByName(@RequestParam("username") String username){
        User result=userService.getUserByName(username);
        if(result!=null)
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",result);
        }else{
            return Result.error(ResultEnum.User_NOT_EXIST.getCode(),ResultEnum.User_NOT_EXIST.getMsg());
        }
    }

    @PostMapping("/admin/register")
    public Result register(@RequestParam("phone") String phone,
                           @RequestParam("password") String password,
                           @RequestParam("gender") String gender,
                           @RequestParam("age") String age,
                           @RequestParam("id_card") String id_card,
                           @RequestParam("name") String name) {

        if(phone.isEmpty()||password.isEmpty()||id_card.isEmpty()||age.isEmpty()||gender.isEmpty()){
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }else
        {
            User user=userService.getUserByName(phone);
            if(user!=null){
                return Result.error(23,"已有该用户无法重复添加");
            }
            userService.addUser(phone, password,id_card, Integer.parseInt(age),gender,name);
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        }
    }

    //删除一个用户
    @DeleteMapping("/admin/deleteUser")
    public Result deleteUser(@RequestParam("username") String username){
        userDao.deleteUser(username);
        return Result.ok(ResultEnum.SUCCESS.getMsg());
    }

    //更新用户
    @PostMapping("/updateUser")
    public Result updateUser(@RequestParam("password") String password,
                             @RequestParam("name") String name,
                             @RequestParam("age") String age,
                             @RequestParam("gender") String gender,
                             @RequestParam("id_card") String id_card,
                             ServletRequest request){
        String token=((HttpServletRequest)request).getHeader("token");
        String username= TokenUtils.parseToken(token).get("username").toString();
        int result=userService.updateUser(username,password,name,Integer.parseInt(age),gender,id_card);
        if(result!=0)
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        }else{
            return Result.error(ResultEnum.UPDATE_FAIL.getCode(),ResultEnum.UPDATE_FAIL.getMsg());
        }
    }

    /**
     * @param page_num 第几页
     * @param each_num 每页多少用户
     * @param queryWhat 查询: 1:读者姓名、2:身份证号、3:电话号码
     * @param content 查询内容
     * @return
     */
    @RequestMapping("/admin/user/fuzzyQuery")
    public Result adminFuzzyQuery(@RequestParam("page_num")int page_num,
                                  @RequestParam("each_num")int each_num,
                                  @RequestParam("queryWhat") int queryWhat,//可缺省
                                  @RequestParam("content") String content//可缺省
    ) {
        if(page_num<=0||each_num<=0){
            return Result.error(33,"数据必须为正");
        }
        int count=userDao.adminFuzzyQueryCount(queryWhat,"%"+content+"%");
        int p_count=(count%each_num==0)?(count/each_num):(count/each_num+1);
        if(page_num>p_count&&p_count!=0){
            return Result.error(34,"页数超过范围");
        }
        List<Map<String,Object>> result=userDao.adminFuzzyQuery(
                (page_num-1)*each_num,each_num,
                queryWhat,"%"+content+"%"
        );
        for(int i=0;i<result.size();i++){
            result.get(i).put("create_time",result.get(i).get("create_time").toString()
                    .replace('T',' '));
            result.get(i).put("access_time",result.get(i).get("access_time").toString()
                    .replace('T',' '));
            result.get(i).put("update_time",result.get(i).get("update_time").toString()
                    .replace('T',' '));
        }
        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("page_count",p_count).put("data",result);
    }
}
