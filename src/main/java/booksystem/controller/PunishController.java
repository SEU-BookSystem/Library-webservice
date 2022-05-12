package booksystem.controller;


import booksystem.dao.PunishDao;
import booksystem.service.PunishService;
import booksystem.utils.Result;
import booksystem.utils.ResultEnum;
import booksystem.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@CrossOrigin(origins="*",maxAge = 3600)
@RestController
public class PunishController {
    @Autowired
    PunishService punishService;
    @Autowired
    PunishDao punishDao;



    @PostMapping("/admin/addPunish")
    public Result addPunish(@RequestParam("username") String username,
                            @RequestParam("bar_code") int bar_code,
                            @RequestParam("status") int status,
                            @RequestParam("detail") String detail,
                            @RequestParam("money") String money)
    {
        punishService.addPunish(username,bar_code,status,detail,money);
        return Result.ok();
    }

    @PostMapping("/admin/updatePunish")
    public Result updatePunish(@RequestParam("punish_id") String punish_id,
                               @RequestParam("status") int status,
                               @RequestParam("detail") String detail,
                               @RequestParam("money") String money)
    {
        punishService.handlePunish(punish_id,status,detail,money);
        return Result.ok();
    }

    @DeleteMapping("/admin/deletePunish")
    public Result deletePunish(@RequestParam("punish_id") String punish_id){
        punishService.deletePunish(punish_id);
        return Result.ok();
    }



    /**
     * @param page_num 第几页
     * @param each_num 每页多少条数据
     * @param queryWhat 查询: 1:读者姓名、2:书籍名称、3:ISBN、
     * @param content 查询内容
     * @return
     */
    @RequestMapping("/admin/punish/fuzzyQuery")
    public Result adminFuzzyQuery(@RequestParam("page_num")int page_num,
                                  @RequestParam("each_num")int each_num,
                                  @RequestParam("queryWhat") int queryWhat,//可缺省
                                  @RequestParam("content") String content//可缺省
    ) {

        if(page_num<=0||each_num<=0){
            return Result.error(33,"数据必须为正");
        }
        int count=punishDao.adminFuzzyQueryCount(queryWhat,"%"+content+"%");
        int p_count=(count%each_num==0)?(count/each_num):(count/each_num+1);
        if(page_num>p_count&&p_count!=0){
            return Result.error(34,"页数超过范围");
        }
        List<Map<String,Object>> result=punishDao.adminFuzzyQuery(
                (page_num-1)*each_num,each_num,
                queryWhat,"%"+content+"%"
        );
        for(int i=0;i<result.size();i++){
            result.get(i).put("update_time",result.get(i).get("update_time").toString()
                    .replace('T',' '));
        }
        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("page_count",p_count).put("data",result);
    }

    /**
     * @param page_num 第几页
     * @param each_num 每页多少条数据
     * @return
     */
    @RequestMapping("/user/punish/query")
    public Result userQuery(@RequestParam("page_num")int page_num,
                                @RequestParam("each_num")int each_num,
                                ServletRequest request
    ) {
        String token=((HttpServletRequest)request).getHeader("token");
        String username= TokenUtils.parseToken(token).get("username").toString();
        if(page_num<=0||each_num<=0){
            return Result.error(33,"数据必须为正");
        }
        int count=punishDao.userQueryCount(username);
        int p_count=(count%each_num==0)?(count/each_num):(count/each_num+1);
        if(page_num>p_count&&p_count!=0){
            return Result.error(34,"页数超过范围");
        }
        List<Map<String,Object>> result=punishDao.userQuery(
                (page_num-1)*each_num,each_num,username
        );
        for(int i=0;i<result.size();i++){
            result.get(i).put("update_time",result.get(i).get("update_time").toString()
                    .replace('T',' '));
        }
        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("page_count",p_count).put("data",result);
    }


    /**
     * @param page_num 第几页
     * @param each_num 每页多少条数据
     * @return
     */
    @RequestMapping("/admin/punish/query")
    public Result adminQuery(@RequestParam("page_num")int page_num,
                        @RequestParam("each_num")int each_num
    ) {
        if(page_num<=0||each_num<=0){
            return Result.error(33,"数据必须为正");
        }
        int count=punishDao.adminQueryCount();
        int p_count=(count%each_num==0)?(count/each_num):(count/each_num+1);
        if(page_num>p_count&&p_count!=0){
            return Result.error(34,"页数超过范围");
        }
        List<Map<String,Object>> result=punishDao.adminQuery(
                (page_num-1)*each_num,each_num
        );
        for(int i=0;i<result.size();i++){
            result.get(i).put("update_time",result.get(i).get("update_time").toString()
                    .replace('T',' '));
        }
        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("page_count",p_count).put("data",result);
    }

}
