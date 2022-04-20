package booksystem.service.Impl;

import booksystem.dao.*;
import booksystem.pojo.Shop;
import booksystem.pojo.User;
import booksystem.service.OrderService;
import booksystem.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    ShopDao shopDao;
    @Autowired
    UserDao userDao;
    @Autowired
    OrderBookDao orderBookDao;
    @Autowired
    OrderDao orderDao;
    @Autowired
    BookDao bookDao;

    @Override
    public List<Shop> getCheckShop() {
        return shopDao.getApplyShop(2);//2：已审核
    }

    @Override
    public int getCheckShopNum() {
        int num1=shopDao.getShopNum(2,1,1);
        int num2=shopDao.getShopNum(2,2,-1);
        int num3=shopDao.getShopNum(2,-1,2);
        return num1+num2+num3;
    }

    @Override
    public List<Shop> getNonCheckShop() {
        return shopDao.getApplyShop(1);//1:未审核
    }

    @Override
    public int getNonCheckShopNum() {
        return shopDao.getShopNum(1,-1,-1);
    }

    @Override
    public List<Shop> getPassShop() {
        return shopDao.getExistShop(1);//1:未注销
    }

    @Override
    public int getPassShopNum() {
        return shopDao.getShopNum(2,1,1);
    }

    @Override
    public List<Shop> getShopByUser(String username) {
        return shopDao.getShopByUser(username);
    }

    @Override
    public List<Shop> getShopByUserAndStatus(String username, int apply_status, int pass_status, int exist_status) {
        return shopDao.getShopByUserAndStatus(username,apply_status,pass_status,exist_status);
    }

    @Override
    public int registerShop(String username,String shopper_name,String shop_name,String apply_reason) {
        //查找是否有未注销的店铺
        List<Shop> shop1 = shopDao.getShopByUserAndStatus(username, 2, 1, 1);
        if (!shop1.isEmpty())
            return -1;//已有店铺

        //查找是否在审核的店铺
        List<Shop> shop2 = shopDao.getShopByUserAndStatus(username, 1, -1, -1);
        if (!shop2.isEmpty())
            return 2;//已在申请店铺

        User user=userDao.getUserByName(username);
        String user_id=user.getId();
        //apply:1 pass:-1 exsit:-1 已提交未审核、审核状态为无效、存在状态为无效
        Shop shop = new Shop(null, user_id, username,"http://47.94.131.208:8888/img/original/avatar.jpg", "http://47.94.131.208:8888/img/compression/avatar.jpg",
                shopper_name, shop_name, 5.0, 1, apply_reason, -1, null, -1, null, null);
        shopDao.addShop(shop);
        return 1;//添加成功
    }

    @Override
    public int checkShop(String username, int pass_status, String check_opinion) {
        //获取要审核的店铺信息 此使为未审核状态
        List<Shop> shopList=shopDao.getShopByUserAndStatus(username,1,-1,-1);
        if(shopList.isEmpty())
            return -1;//数据为空
        //因为一个账号只允许一个未审核的存在  所以未审核的店铺信息存储在shop中
        Shop shop=shopList.get(0);
        //更改为审核状态
        if(pass_status==1)//通过审核
        {
            //已审核2 通过1 未注销1
            shop.setApply_status(2);
            shop.setPass_status(1);
            shop.setExist_status(1);
            shop.setCheck_opinion(check_opinion);
            userDao.updateIdentity(username,1);//更新身份变为商家
        }else if(pass_status==2)//拒绝
        {
            //已审核2 拒绝2 无效-1
            shop.setApply_status(2);
            shop.setPass_status(2);
            shop.setExist_status(-1);
            shop.setCheck_opinion(check_opinion);
        }
        shopDao.updateShop(shop);
        shopDao.updateCreateTime(shop.getId());
        return 1;//成功
    }

    @Override
    public int updateShopApply(String username, String shopper_name, String shop_name, String apply_reason) {
        //默认已经有在申请的店铺了
        List<Shop> shopList = shopDao.getShopByUserAndStatus(username, 1, -1, -1);
        if (shopList.isEmpty())
            return -1;//没有在申请店铺 无需修改

        //因为一个账号只允许一个未审核的存在  所以未审核的店铺信息存储在shop中
        Shop shop=shopList.get(0);
        //修改信息
        shop.setShopper_name(shopper_name);
        shop.setShop_name(shop_name);
        shop.setApply_reason(apply_reason);
        shopDao.updateShop(shop);
        return 1;
    }

    @Override
    public int cancelShopApply(String username) {
        //默认已经有在申请的店铺了
        List<Shop> shopList = shopDao.getShopByUserAndStatus(username, 1, -1, -1);
        if (shopList.isEmpty())
            return -1;//没有在申请店铺 无需修改
        //因为一个账号只允许一个未审核的存在  所以未审核的店铺信息存储在shop中
        Shop shop=shopList.get(0);
        //修改信息
        shop.setApply_status(3);
        shop.setPass_status(-1);
        shop.setExist_status(-1);
        shopDao.updateShop(shop);
        return 1;
    }

    //注销店铺
    @Override
    public int deleteShop(String username) {
        //获取要注销的店铺信息 此使为未注销状态
        List<Shop> shopList=shopDao.getShopByUserAndStatus(username,2,1,1);
        if(shopList.isEmpty())
            return -1;//数据为空
        //因为只允许一个未注销的存在  所以未注销的店铺信息存储在shop中
        Shop shop=shopList.get(0);

        //将所有店铺的书的库存置为-1 无法购买
        bookDao.updateStatus(shop.getId());
        //更改为注销状态 已审核2 通过1 注销2
        shop.setApply_status(2);
        shop.setPass_status(1);
        shop.setExist_status(2);
        shop.setShop_name("店铺已注销");
        shopDao.updateShop(shop);
        userDao.updateIdentity(username,0);//更新身份变为普通用户

        if(shopDao.getShopById(shop.getId()).getPass_status()!=-1&&shopDao.getShopById(shop.getId()).getExist_status()!=2)
            return 0;//注销失败
        return 1;//成功
    }

    @Override
    public int updateShop(String username, String shopper_name, String shop_name) {
        //获取要修改的店铺信息 此使为未注销状态
        List<Shop> shopList=shopDao.getShopByUserAndStatus(username,2,1,1);
        if(shopList.size()==0)
            return -1;//数据为空
        //因为只允许一个未注销的存在  所以未注销的店铺信息存储在shop中
        Shop shop=shopList.get(0);
        //修改信息
        shop.setShopper_name(shopper_name);
        shop.setShop_name(shop_name);
        shopDao.updateShop(shop);
        return 1;
    }

    @Override
    public Shop getShopById(String shop_id) {
        return shopDao.getShopById(shop_id);
    }

    @Override
    public double ShopRate(String shop_id) {
        //获取所有的商家订单目录项
        List<String> Order_Ids=orderDao.getAllOrderIDByShop(shop_id);
        if(Order_Ids.isEmpty())
            return -1;
        List<Map<String,Object>> orderBookList=orderBookDao.getAllOrderBookByShop(Order_Ids);

        double rateTotal=0;
        int sum=0;
        //查找所有remark_status=1的人
        for(Map<String,Object> orderBook:orderBookList)
        {
            if(Integer.valueOf(orderBook.get("remark_status").toString())==1)//已评价
            {
                sum+=1;
                rateTotal+=Double.valueOf(orderBook.get("rate").toString());
            }
        }
        if(sum<=1)
            return -1;
        return (rateTotal/sum);
    }
}
