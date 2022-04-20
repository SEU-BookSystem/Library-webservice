package booksystem.controller;

import booksystem.pojo.Address;
import booksystem.service.AddressService;
import booksystem.service.Impl.AddressServiceImpl;
import booksystem.utils.Result;
import booksystem.utils.ResultEnum;
import booksystem.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@CrossOrigin(origins="*",maxAge = 3600)
@RestController
public class AddressController {
    @Autowired
    AddressService addressService;

    //根据user_id获取所有地址
    @RequestMapping("/user/address/getAll")
    public Result getAllAddress(ServletRequest request){
        String token=((HttpServletRequest)request).getHeader("token");
        String username= TokenUtils.parseToken(token).get("username").toString();
        return Result.ok().put("data",addressService.getAllAddress(username));
    }

    //根据address_id获取地址
    @RequestMapping("/address/public/getByID")
    public Result getAddressByID(@RequestParam("address_id") String address_id){
        return Result.ok().put("data",addressService.getAddressByID(address_id));
    }

    //添加一个地址
    @PostMapping("/user/address/add")
    public Result addAddress(@RequestParam("address") String address,
                             @RequestParam("name") String name,
                             @RequestParam("phone") String phone,
                             ServletRequest request){
        String token=((HttpServletRequest)request).getHeader("token");
        String username= TokenUtils.parseToken(token).get("username").toString();
        addressService.insertAddress(address,name,phone,username);
        return Result.ok(ResultEnum.SUCCESS.getMsg());
    }
    //删除一个地址
    @DeleteMapping("/user/address/delete")
    public Result deleteAddress(@RequestParam("addressId") String addressId){
        addressService.deleteAddress(addressId);
        return Result.ok(ResultEnum.SUCCESS.getMsg());
    }
    //修改地址信息
    @PostMapping("/user/address/update")
    public Result updateAddress(
            @RequestParam("addressId") String addressId,
            @RequestParam("address") String address,
            @RequestParam("name") String name,
            @RequestParam("phone") String phone){
        addressService.updateAddress(addressId,address,name,phone);
        return Result.ok(ResultEnum.SUCCESS.getMsg());
    }

    @PostMapping("/user/address/setDefault")
    public Result setDefault(@RequestParam("addressId") String addressId){
        addressService.setDefault(addressId);
        return Result.ok(ResultEnum.SUCCESS.getMsg());
    }
}
