package booksystem.controller;

import booksystem.dao.ShopDao;
import booksystem.pojo.Shop;
import booksystem.pojo.User;
import booksystem.service.ShopService;
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
import java.util.ArrayList;
import java.util.List;

@RestController
public class ShopController {
    @Autowired
    ShopService shopService;
    @Autowired
    UploadImgService uploadImgService;
    @Autowired
    UserService userService;
    @Autowired
    ShopDao shopDao;

    //获取审核过的店铺
    @RequestMapping("/admin/getCheckShop")
    public Result getCheckShop(){
        List<Shop>result=shopService.getCheckShop();
        if(!result.isEmpty())
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",result);
        }else{
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }
    }

    //获取未审核的店铺
    @RequestMapping("/admin/getNonCheckShop")
    public Result getNonCheckShop(){
        List<Shop>result=shopService.getNonCheckShop();
        if(!result.isEmpty())
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",result);
        }else{
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }
    }

    //获取现有店铺
    @RequestMapping("/admin/getPassShop")
    public Result getPassShop(){
        List<Shop>result=shopService.getPassShop();
        if(!result.isEmpty())
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",result);
        }else{
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }
    }

    //获取现有店铺总数
    @RequestMapping("/admin/getPassShopNum")
    public Result getPassShopNum(){
        int result=shopService.getPassShopNum();//未注销
        if(result>=0)
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",result);
        }else{
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(),ResultEnum.UNKNOWN_ERROR.getMsg());
        }
    }

    //获取审核过的总数
    @RequestMapping("/admin/getCheckShopNum")
    public Result getCheckShopNum(){
        int result=shopService.getCheckShopNum();
        if(result>=0)
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",result);
        }else{
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(),ResultEnum.UNKNOWN_ERROR.getMsg());
        }
    }

    //获取未审核店铺总数
    @RequestMapping("/admin/getNonCheckShopNum")
    public Result getNonCheckShopNum(){
        int result=shopService.getNonCheckShopNum();
        if(result>=0)
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",result);
        }else{
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(),ResultEnum.UNKNOWN_ERROR.getMsg());
        }
    }

    //根据username获取所有状态的店铺信息
    @RequestMapping("/user/getAllShop")
    public Result getShopByUsername(@RequestParam("username")String username)
    {
        List<Shop> result=shopService.getShopByUser(username);
        if(!result.isEmpty())
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",result);
        }else
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
    }

    //根据username获取现有的店铺信息
    @RequestMapping("/shop/getPassed")
    public Result getPassShopByUsername(@RequestParam("username")String username)
    {
        List<Shop> result=shopService.getShopByUserAndStatus(username,2,1,1);
        if(!result.isEmpty())
        {
            Shop shop=result.get(0);
            return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",shop);
        }else
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
    }

    /**
     * @param page_num 第几页
     * @param shop_num 每页多少数据
     * @param style 1:按创建时间,2:按店铺图书销量,
     * @param content 查询内容
     * @return
     */
    @RequestMapping("/shop/public/fuzzyQuery")
    public Result fuzzyQuery(@RequestParam("page_num")String page_num,
                             @RequestParam("shop_num")String shop_num,
                             @RequestParam("style")String style,
                             @RequestParam("content") String content//可缺省
    ) {
        if(page_num.isEmpty()||shop_num.isEmpty()||style.isEmpty()){
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }
        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",shopDao.fuzzyQuery(
                (Integer.parseInt(page_num)-1)*Integer.parseInt(shop_num),
                Integer.parseInt(shop_num), Integer.parseInt(style), "%"+content+"%"
        ));
    }

    //店铺注册
    @PostMapping("/registerShop")
    public Result register(@RequestParam("shopper_name") String shopper_name,
                           @RequestParam("shop_name") String shop_name,
                           @RequestParam("apply_reason") String apply_reason,
                           @RequestParam("img") MultipartFile img,
                           ServletRequest request){
        String token=((HttpServletRequest)request).getHeader("token");
        String username= TokenUtils.parseToken(token).get("username").toString();
        int result=shopService.registerShop(username,shopper_name,shop_name,apply_reason);

        if(result==1)
        {
            List<Shop> shopList=shopService.getShopByUserAndStatus(username,1,-1,-1);
            Shop shop=shopList.get(0);
            if (!img.isEmpty()) {
                uploadImgService.uploadShopImg(img, shop.getId());
            }
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        }else if(result==-1)
        {
            return Result.error(ResultEnum.SHOP_IS_EXISTS.getCode(),ResultEnum.SHOP_IS_EXISTS.getMsg());

        }else if(result==2)
        {
            return Result.error(ResultEnum.SHOP_IS_APPLY.getCode(),ResultEnum.SHOP_IS_APPLY.getMsg());
        }else
        {
            return Result.error(ResultEnum.REGISTER_FAIL.getCode(),ResultEnum.REGISTER_FAIL.getMsg());
        }
    }

    //审核是否通过
    @PostMapping("/admin/checkShop")
    public Result checkShop(@RequestParam("username") String username,
                            @RequestParam("pass_status") int pass_status,
                            @RequestParam("check_opinion") String check_opinion){
        int result=shopService.checkShop(username,pass_status,check_opinion);
        if(result==1)
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg());

        }else if(result==-1)
        {
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());

        }else
        {
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(),ResultEnum.UNKNOWN_ERROR.getMsg());
        }
    }

    //申请信息修改
    @PostMapping("/user/updateShopApply")
    public Result updateShopApply(@RequestParam("shopper_name") String shopper_name,
                           @RequestParam("shop_name") String shop_name,
                           @RequestParam("apply_reason") String apply_reason,
                           @RequestParam("img") MultipartFile img,
                           ServletRequest request){
        String token=((HttpServletRequest)request).getHeader("token");
        String username= TokenUtils.parseToken(token).get("username").toString();
        int result=shopService.updateShopApply(username,shopper_name,shop_name,apply_reason);

        if(result==1)
        {
            List<Shop> shopList=shopService.getShopByUserAndStatus(username,1,-1,-1);
            Shop shop=shopList.get(0);
            if (!img.isEmpty()) {
                uploadImgService.uploadShopImg(img, shop.getId());
            }
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        }else if(result==-1)
        {
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());

        }else
        {
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(),ResultEnum.UNKNOWN_ERROR.getMsg());
        }
    }

    //取消申请
    @PostMapping("/user/cancelShopApply")
    public Result cancelShopApply(ServletRequest request){

        String token=((HttpServletRequest)request).getHeader("token");
        String username= TokenUtils.parseToken(token).get("username").toString();
        int result=shopService.cancelShopApply(username);
        if(result==1)
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg());

        }else if(result==-1)
        {
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());

        }else
        {
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(),ResultEnum.UNKNOWN_ERROR.getMsg());
        }
    }

    //店铺修改
    @PostMapping("/shop/updateShop")
    public Result updateShop(@RequestParam("shopper_name") String shopper_name,
                                  @RequestParam("shop_name") String shop_name,
                                  ServletRequest request){
        String token=((HttpServletRequest)request).getHeader("token");
        String username= TokenUtils.parseToken(token).get("username").toString();
        int result=shopService.updateShop(username,shopper_name,shop_name);

        if(result==1)
        {
            List<Shop> shopList=shopService.getShopByUserAndStatus(username,2,1,1);
            Shop shop=shopList.get(0);
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        }else if(result==-1)
        {
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());

        }else
        {
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(),ResultEnum.UNKNOWN_ERROR.getMsg());
        }
    }

    //店铺修改
    @PostMapping("/admin/updateShop")
    public Result updateShopAdmin(@RequestParam("username")String username,
                                  @RequestParam("shopper_name") String shopper_name,
                             @RequestParam("shop_name") String shop_name
                            ){

        int result=shopService.updateShop(username,shopper_name,shop_name);
        if(result==1)
        {
            List<Shop> shopList=shopService.getShopByUserAndStatus(username,2,1,1);
            Shop shop=shopList.get(0);
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        }else if(result==-1)
        {
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());

        }else
        {
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(),ResultEnum.UNKNOWN_ERROR.getMsg());
        }
    }

    //店铺注销 根据username注销店铺
    @DeleteMapping("/shop/logoutShop")
    public Result deleteShop(@RequestParam("username") String username){
        int result=shopService.deleteShop(username);
        if (result == 1) {
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        } else if(result==-1){
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(), ResultEnum.DATA_IS_NULL.getMsg());
        }
        else if(result==0){
            return Result.error(ResultEnum.DELETE_FAIL.getCode(), ResultEnum.DELETE_FAIL.getMsg());
        }else {
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(), ResultEnum.UNKNOWN_ERROR.getMsg());
        }
    }


    //修改头像
    @PostMapping("/shop/updateAvatar")
    public Result updateAvatar(@RequestParam("username") String username,
                               @RequestParam("img") MultipartFile img) {
        List<Shop> shopList = shopService.getShopByUserAndStatus(username, 2, 1, 1);
        Shop shop = shopList.get(0);
        uploadImgService.uploadShopImg(img, shop.getId());
        return Result.ok(ResultEnum.SUCCESS.getMsg());
    }

    //通过shop_id来获取信息
    @RequestMapping("/user/public/getShopById")
    public Result getShopById(@RequestParam("shop_id") String shop_id){
        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",shopService.getShopById(shop_id));
    }

    //计算店铺的评分
    @RequestMapping("/shop/rate")
    public Result shopRate(@RequestParam("shop_id") String shop_id){
        double rate=shopService.ShopRate(shop_id);
        if(rate<0)
            return Result.error(ResultEnum.UPDATE_FAIL.getCode(),ResultEnum.UPDATE_FAIL.getMsg());
        else
            return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",rate);
    }
}
