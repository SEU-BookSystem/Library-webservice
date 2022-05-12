package booksystem.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface PunishDao {
    void addPunish(String username,int bar_code,int status,int is_handle,String detail,String money);

    void updatePunish(String punish_id,int status,int is_handle,String detail,String money);

    void deletePunish(String punish_id);

    Map<String,Object> getPunishById(String punish_id);

    List<Map<String,Object>> adminFuzzyQuery(int start, int each_num, int queryWhat, String content);
    List<Map<String,Object>> adminQuery(int start, int each_num);
    List<Map<String,Object>> userQuery(int start, int each_num,String username);

    int adminFuzzyQueryCount(int queryWhat, String content);
    int adminQueryCount();
    int userQueryCount(String username);
}
