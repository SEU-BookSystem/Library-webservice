package booksystem.controller;

import booksystem.service.BorrowService;
import booksystem.utils.Result;
import booksystem.utils.ResultEnum;
import booksystem.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@CrossOrigin(origins="*",maxAge = 3600)
@RestController
public class BorrowController {
    @Autowired
    BorrowService borrowService;

    @PostMapping("/book/addReserve")
    public Result addDirectReserve(@RequestParam("reference_num") String reference_num,
                                   @RequestParam("username") String username)
    {
        int result=borrowService.addDirectReserve(reference_num,username);
        if(result==1)
            return Result.ok();
        else if(result==0)
            return Result.error(ResultEnum.RESERVE_IS_NOT_ENOUGH.getCode(), ResultEnum.RESERVE_IS_NOT_ENOUGH.getMsg());
        else if(result==-1)
            return Result.error(ResultEnum.BORROW_IS_MAX.getCode(),ResultEnum.BORROW_IS_MAX.getMsg());
        else
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(),ResultEnum.UNKNOWN_ERROR.getMsg());
    }

    @PostMapping("/collection/addReserve")
    public Result addCollectionReserve(@RequestParam("reference_nums") List<String> reference_nums,
                                       @RequestParam("username") String username)
    {
        int result=borrowService.addCollectionReserve(reference_nums,username);

        if(result==1)
            return Result.ok();
        else if(result==-1)
            return Result.error(ResultEnum.BORROW_IS_MAX.getCode(),ResultEnum.BORROW_IS_MAX.getMsg());
        else
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(),ResultEnum.UNKNOWN_ERROR.getMsg());
    }

    @PostMapping("/reserve/cancel")
    public Result cancelReserve(@RequestParam("lend_id") String lend_id)
    {
        borrowService.cancelReserve(lend_id);
        return Result.ok();
    }

    @PostMapping("/reserve/batCancel")
    public Result batCancelReserve(@RequestParam("lend_ids") List<String> lend_ids)
    {
        borrowService.batCancelReserve(lend_ids);
        return Result.ok();
    }
}
