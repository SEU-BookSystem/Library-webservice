package booksystem.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@Mapper
public interface PunishDao {
    void addPunish(String username,int bar_code,int status,int is_handle,String detail,String money);

    void updatePunish(String punish_id,int status,int is_handle,String detail,String money);

    void deletePunish(String punish_id);

    Map<String,Object> getPunishById(String punish_id);
}
