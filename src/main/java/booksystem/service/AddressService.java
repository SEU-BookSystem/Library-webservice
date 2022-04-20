package booksystem.service;

import booksystem.pojo.Address;

import java.util.List;
import java.util.Map;

public interface AddressService {
    //根据user_id获取所有地址
    List<Address> getAllAddress(String user_id);

    //根据address_id获取地址信息
    Map<String,Object> getAddressByID(String address_id);

    //修改地址信息
    void updateAddress(String id,String address,String name,String phone);
    void insertAddress(String address,String name,String phone,String username);
    void deleteAddress(String id);
    void setDefault(String id);
}
