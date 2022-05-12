package booksystem.controller;


import booksystem.service.PunishService;
import booksystem.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins="*",maxAge = 3600)
@RestController
public class PunishController {
    @Autowired
    PunishService punishService;


    @PostMapping("/punish/add")
    public Result addPunish(@RequestParam("username") String username,
                            @RequestParam("bar_code") int bar_code,
                            @RequestParam("status") int status,
                            @RequestParam("detail") String detail,
                            @RequestParam("money") String money)
    {
        punishService.addPunish(username,bar_code,status,detail,money);
        return Result.ok();
    }

    @PostMapping("/punish/update")
    public Result updatePunish(@RequestParam("punish_id") String punish_id,
                               @RequestParam("status") int status,
                               @RequestParam("detail") String detail,
                               @RequestParam("money") String money)
    {
        punishService.handlePunish(punish_id,status,detail,money);
        return Result.ok();
    }

    @DeleteMapping("/punish/delete")
    public Result deletePunish(@RequestParam("punish_id") String punish_id){
        punishService.deletePunish(punish_id);
        return Result.ok();
    }
}
