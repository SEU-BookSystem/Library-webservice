package booksystem.dao;

import booksystem.pojo.Shop;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ShopDao {

    //依据apply_status获取店铺
    List<Shop> getApplyShop(int apply_status);
    //依据exist_status获取店铺
    List<Shop> getExistShop(int exist_status);

    //根据三个状态组合来获取店铺数量
    int getShopNum(int apply_status,int pass_status,int exist_status);

    //根据username获取不同申请状态的店铺信息
    List<Shop> getShopByUserAndStatus(String username,int apply_status,int pass_status,int exist_status);
    List<Shop> getShopByUser(String username);

    //根据shop_id来获取店铺信息
    Shop getShopById(String shop_id);

    //添加
    void addShop(Shop shop);

    //更新店铺(不包括头像和店铺id和user_id)
    void updateShop(Shop shop);

    //更新创建时间 用于获取审核时间
    void updateCreateTime(String shop_id);
    List<Shop> fuzzyQuery(int start, int shop_num, int style, String content);
}
