package booksystem.service.Impl;

import booksystem.dao.BorrowDao;
import booksystem.dao.PunishDao;
import booksystem.dao.UserDao;
import booksystem.service.BookItemService;
import booksystem.service.BorrowService;
import booksystem.service.PunishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PunishServiceImpl implements PunishService {

    @Autowired
    PunishDao punishDao;
    @Autowired
    BorrowService borrowService;
    @Autowired
    BorrowDao borrowDao;
    @Autowired
    BookItemService bookItemService;
    @Autowired
    UserDao userDao;


    @Override
    public int addPunish(String username, int bar_code,int status,String detail,String money) {
        punishDao.addPunish(username,bar_code,status,1,detail,money);
        if(status==3){
            //书籍丢失、删除书籍
            bookItemService.deleteBookItem(bar_code);
        }
        return 0;
    }

    @Override
    public int handlePunish(String punish_id, int status, String detail, String money) {
        punishDao.updatePunish(punish_id,status,1,detail,money);
        Map<String,Object> punish=punishDao.getPunishById(punish_id);
        if(punish==null)
            return 0;

        if(status==1){
            //书籍逾期
            List<Map<String,Object>> overtime=borrowDao.getOvertimeBorrow(punish.get("username").toString());
            if(overtime.size()==1) {
                userDao.updateStatus(punish.get("username").toString(),0);
            }
        }
        return 0;
    }

    @Override
    public int deletePunish(String punish_id) {
        punishDao.deletePunish(punish_id);
        return 0;
    }
}
