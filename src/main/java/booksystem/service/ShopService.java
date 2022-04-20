package booksystem.service;

import booksystem.pojo.Shop;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ShopService {

    //已审核店铺列表
    List<Shop> getCheckShop();
    int getCheckShopNum();
    //未审核店铺列表
    List<Shop> getNonCheckShop();
    int getNonCheckShopNum();
    //未注销店铺列表
    List<Shop> getPassShop();
    int getPassShopNum();

    //通过username查找所有状态的店铺
    List<Shop> getShopByUser(String username);
    //通过username查找特定状态的店铺
    List<Shop> getShopByUserAndStatus(String username,int apply_status,int pass_status,int exist_status);

    //注册账号
    int registerShop(String username, String shopper_name, String shop_name, String apply_reason);

    //审核是否通过
    int checkShop(String username,int pass_status,String check_opinion);

    //修改申请
    int updateShopApply(String username,String shopper_name,String shop_name,String apply_reason);

    //取消申请
    int cancelShopApply(String username);

    //注销店铺
    int deleteShop(String username);

    //更新店铺信息
    int updateShop(String username,String shopper_name,String shop_name);

    //根据shop_id来获取店铺信息
    Shop getShopById(String shop_id);

    //计算店铺评分
    double ShopRate(String shop_id);
}
