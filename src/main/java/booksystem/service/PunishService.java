package booksystem.service;

import java.util.List;
import java.util.Map;

public interface PunishService {

    //添加违规记录
    int addPunish(String username,int bar_code);

    //处理违规记录
    int handlePunish(String punish_id);

    //删除违规记录
    int deletePunish(String punish_id);
}
