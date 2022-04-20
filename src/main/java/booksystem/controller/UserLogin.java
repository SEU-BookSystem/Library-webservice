package booksystem.controller;

import booksystem.service.UserLoginService;
import booksystem.utils.Result;
import booksystem.utils.ResultEnum;
import booksystem.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserLogin {

    @Autowired
    UserLoginService userLoginService;

    @PostMapping ("/login")
    public Result login(@RequestParam("username") String username,
                        @RequestParam("password") String password){
        int user=userLoginService.userLogin(username,password);
        if(user==0){
            int admin=userLoginService.adminLogin(username,password);
            if(admin==0){
                Result result=Result.error(ResultEnum.LOGIN_FAIL.getCode(),ResultEnum.LOGIN_FAIL.getMsg());
                return result;
            }
            String token=TokenUtils.generateToken(username,password,2);
            Result result=Result.ok("登录成功").put("token",token).put("identity",2);
            return result;

        }
        int identity=userLoginService.getIdentity(username);
        String token=TokenUtils.generateToken(username,password,identity);
        Result result=Result.ok("登录成功").put("token",token).put("identity",identity);
        return result;
    }
}
