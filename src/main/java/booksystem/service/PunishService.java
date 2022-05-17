package booksystem.service;

import java.util.List;
import java.util.Map;

public interface PunishService {

    //添加违规记录
    int addPunish(String username, int bar_code,int status,String detail,String money);

    //处理违规记录
    int handlePunish(String punish_id,int status,String detail,String money);

    //删除违规记录
    int deletePunish(String punish_id);

    //获取不同状态下的记录数量
    int getNumByStatus(int number);

}
