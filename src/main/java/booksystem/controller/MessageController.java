package booksystem.controller;

import booksystem.service.MessageService;
import booksystem.utils.Result;
import booksystem.utils.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.util.List;

@CrossOrigin(origins="*",maxAge = 3600)
@RestController
public class MessageController {
    @Autowired
    MessageService messageService;

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

}
