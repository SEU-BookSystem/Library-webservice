package booksystem.dao;

import booksystem.pojo.Address;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface AddressDao {

    //根据user_id获取所有地址
    List<Address> getAllAddress(String username);

    //根据address_id获取地址信息
    Map<String,Object> getAddressByID(String address_id);

    //删除用户所有的地址
    void deleteAllAddress(String user_id);

    //修改地址信息
    void updateAddress(String id,String address,String name,String phone);
    void insertAddress(String address,String name,String phone,String username);
    void deleteAddress(String id);
    void setDefault(String id);
}
