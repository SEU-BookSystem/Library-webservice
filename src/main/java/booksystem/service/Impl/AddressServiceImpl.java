package booksystem.service.Impl;

import booksystem.dao.AddressDao;
import booksystem.pojo.Address;
import booksystem.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressDao addressDao;
    @Override
    public List<Address> getAllAddress(String user_id) {
        return addressDao.getAllAddress(user_id);
    }

    @Override
    public Map<String, Object> getAddressByID(String address_id) {
        Map<String,Object>map= addressDao.getAddressByID(address_id);
        if(!map.isEmpty()) {
            map.put("create_time", map.get("create_time").toString().replace('T', ' '));
            map.put("update_time", map.get("update_time").toString().replace('T', ' '));
            map.put("receiver_name", map.get("name").toString());
            map.remove("name");
        }
        return map;
    }


    @Override
    public void updateAddress(String id,String address,String name,String phone) {
        addressDao.updateAddress(id,address,name,phone);
    }

    @Override
    public void insertAddress(String address, String name, String phone, String username) {
        addressDao.insertAddress(address,name,phone,username);
    }

    @Override
    public void deleteAddress(String id) {
        addressDao.deleteAddress(id);
    }

    @Override
    public void setDefault(String id) {
        addressDao.setDefault(id);
    }
}
