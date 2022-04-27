package booksystem.controller;

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

@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    UploadImgService uploadImgService;

    //获取所有用户
    @RequestMapping("/admin/getAllUser")
    public Result getAllUser(){
        List<User> result=userService.getAllUser();
        if(!result.isEmpty())
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",result);
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

    //用户user_id查找用户
    @RequestMapping("/user/getByID")
    public Result getUserByID(@RequestParam("user_id") String user_id){
        User result=userService.getUserByID(user_id);
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
                           @RequestParam("id_card") String id_card) {

        if(phone.isEmpty()||password.isEmpty()||id_card.isEmpty()||age.isEmpty()||gender.isEmpty()){
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }else
        {
            User user=userService.getUserByName(phone);
            if(user!=null){
                return Result.error(23,"已有该用户无法重复添加");
            }
            userService.addUser(phone, password,id_card, Integer.parseInt(age),gender);
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        }
    }

    //删除一个用户
    @DeleteMapping("/deleteUser")
    public Result deleteUser(@RequestParam("username") String username){
        User user=userService.getUserByName(username);
        if(user==null)
        {
            return Result.error(ResultEnum.User_NOT_EXIST.getCode(),ResultEnum.User_NOT_EXIST.getMsg());
        }
        userService.deleteUser(user.getId());
        User result=userService.getUserByName(username);
        if(result==null)
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        }else{
            return Result.error(ResultEnum.DELETE_FAIL.getCode(),ResultEnum.DELETE_FAIL.getMsg());
        }
    }

    //更新用户
    @PostMapping("/updateUser")
    public Result updateUser(@RequestParam("password") String password,
                             @RequestParam("name") String name,
                             ServletRequest request){
        String token=((HttpServletRequest)request).getHeader("token");
        String username= TokenUtils.parseToken(token).get("username").toString();
        int result=userService.updateUser(username,password,name);
        if(result!=0)
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        }else{
            return Result.error(ResultEnum.UPDATE_FAIL.getCode(),ResultEnum.UPDATE_FAIL.getMsg());
        }
    }

    //修改头像
    @PostMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam("img") MultipartFile img,
                             ServletRequest request){
        String token=((HttpServletRequest)request).getHeader("token");
        String username= TokenUtils.parseToken(token).get("username").toString();
        if(!img.isEmpty()){
            uploadImgService.uploadUserImg(img,username);
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        }else{
            return Result.error(ResultEnum.UPDATE_FAIL.getCode(),ResultEnum.UPDATE_FAIL.getMsg());
        }
    }


    //修改头像
    @PostMapping("/admin/updateAvatar")
    public Result updateAvatar(@RequestParam("img") MultipartFile img,
                               @RequestParam("user_id")String user_id){

        User user=userService.getUserByID(user_id);
        uploadImgService.uploadUserImg(img,user.getUsername());
        return Result.ok(ResultEnum.SUCCESS.getMsg());
    }

    //修改头像
    @PostMapping("/admin/updateUser")
    public Result updateEmail(@RequestParam("user_id")  String user_id,
                              @RequestParam("name") String name,
                              @RequestParam("password") String password,
                              @RequestParam("email") String email){
        User user=userService.getUserByID(user_id);
        userService.updateUser(user.getUsername(),password,name);
        userService.updateEmail(user_id, email);
        return Result.ok(ResultEnum.SUCCESS.getMsg());
    }
}
