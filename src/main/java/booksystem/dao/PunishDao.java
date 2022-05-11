package booksystem.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface PunishDao {
    void addPunish(String username,int bar_code,int status,int is_handle,String detail,String money);

    void update(String punish_id,int is_handle,String detail,String money);

    void delete(String punish_id);
}
