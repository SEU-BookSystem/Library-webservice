package booksystem.controller;

import booksystem.dao.MessageDao;
import booksystem.service.MessageService;
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
public class MessageController {
    @Autowired
    MessageService messageService;

    @Autowired
    MessageDao messageDao;

    //发送逾期消息
    @PostMapping("/admin/sendOvertimeMessage")
    public Result sendOvertimeMessage(@RequestParam("username") String username,
                                      @RequestParam("book_name") String book_name)
    {
        messageService.sendOvertimeMessage(username,book_name);
        return Result.ok();
    }

    //发送预约失败消息
    @PostMapping("/admin/sendReserveMessage")
    public Result sendReserveMessage(@RequestParam("username") String username,
                                     @RequestParam("book_name") String book_name)
    {
        messageService.sendReserveMessage(username,book_name);
        return Result.ok();
    }

    //已读所有消息
    @PostMapping("/message/readAll")
    public Result readAllMessage(@RequestParam("username") String username) {
        messageService.readAllMessage(username);
        return Result.ok();
    }

    //已读一条消息
    @PostMapping("/message/read")
    public Result readMessage(@RequestParam("message_id") String message_id)
    {
        messageService.readMessage(message_id);
        return Result.ok();
    }

    //删除用户所有消息
    @DeleteMapping("/message/deleteAll")
    public Result deleteAllMessage(@RequestParam("username") String username)
    {
        messageService.deleteAllMessage(username);
        return Result.ok();
    }

    //删除用户一条消息
    @DeleteMapping("/message/delete")
    public Result deleteMessage(@RequestParam("message_id") String message_id)
    {
        messageService.deleteMessage(message_id);
        return Result.ok();
    }

    //返回用户未读消息数量
    @RequestMapping("/message/nonReadNum")
    public Result nonReadMessageNum(@RequestParam("username") String username)
    {
        return Result.ok().put("data",messageService.nonReadMessage(username));
    }


    /**
     * @param page_num 第几页
     * @param each_num 每页多少条数据
     * @return
     */
    @RequestMapping("/user/message/query")
    public Result query(@RequestParam("page_num")int page_num,
                        @RequestParam("each_num")int each_num,
                        ServletRequest request
    ) {
        String token=((HttpServletRequest)request).getHeader("token");
        String username= TokenUtils.parseToken(token).get("username").toString();
        if(page_num<=0||each_num<=0){
            return Result.error(33,"数据必须为正");
        }
        int count=messageDao.myQueryCount(username);
        int p_count=(count%each_num==0)?(count/each_num):(count/each_num+1);
        if(page_num>p_count&&p_count!=0){
            return Result.error(34,"页数超过范围");
        }
        List<Map<String,Object>> result=messageDao.myQuery(
                (page_num-1)*each_num,each_num,username
        );
        for(int i=0;i<result.size();i++){
            result.get(i).put("send_time",result.get(i).get("send_time").toString()
                    .replace('T',' '));
        }
        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("page_count",p_count).put("data",result);
    }

}
