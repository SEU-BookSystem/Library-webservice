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
            return Result.error(ResultEnum.BOOK_IS_NOT_ENOUGH.getCode(), ResultEnum.BOOK_IS_NOT_ENOUGH.getMsg());
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

    //借书
    @PostMapping("/admin/borrowBook")
    public Result borrowBook(@RequestParam("bar_code") int bar_code,
                             @RequestParam("username") String username)
    {
        int result=borrowService.borrowBook(bar_code,username);
        if(result==2) {
            //成功
            return Result.ok();
        }else if(result==1) {
            //有逾期记录 不能借书
            return Result.error(ResultEnum.OVERTIME_RECODE.getCode(), ResultEnum.OVERTIME_RECODE.getMsg());
        }else if(result==0) {
            //超过最大借阅数
            return Result.error(ResultEnum.BORROW_IS_MAX.getCode(),ResultEnum.BORROW_IS_MAX.getMsg());
        }else if(result==-1) {
            //书籍不可借
            return Result.error(ResultEnum.BOOK_IS_NOT_ENOUGH.getCode(),ResultEnum.BOOK_IS_NOT_ENOUGH.getMsg());
        }
        return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(),ResultEnum.UNKNOWN_ERROR.getMsg());
    }

    //还书
    @PostMapping("/admin/lendBook")

    public Result lendBook(@RequestParam("bar_code") int bar_code,
                           @RequestParam("username") String username)
    {
        int result=borrowService.lendBook(bar_code,username);
        if(result==1) {
            return Result.ok();
        }else if(result==0) {
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(), ResultEnum.DATA_IS_NULL.getMsg());
        }
        return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(),ResultEnum.UNKNOWN_ERROR.getMsg());
    }

    //续借
    @PostMapping("/renewBook")
    public Result renew(@RequestParam("bar_code") int bar_code,
                        @RequestParam("username") String username)
    {
        int result=borrowService.renew(bar_code,username);
        if(result==1) {
            return Result.ok();
        }else if(result==0){
            return Result.error(ResultEnum.OVERTIME_RECODE.getCode(), ResultEnum.OVERTIME_RECODE.getMsg());
        }else if(result==-1){
            return Result.error(ResultEnum.RENEW_REPEAT.getCode(),ResultEnum.RENEW_REPEAT.getMsg());
        }
        return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(),ResultEnum.UNKNOWN_ERROR.getMsg());
    }

    //删除借阅记录
    @DeleteMapping("/admin/deleteBorrow")
    public Result deleteBorrow(@RequestParam("lend_id") String lend_id)
    {
        borrowService.deleteBorrow(lend_id);
        return Result.ok();
    }

    @PostMapping("/admin/setBorrowOvertime")
    public Result setBookOvertime(@RequestParam("username") String username,
                                  @RequestParam("lend_id") String lend_id)
    {
        borrowService.setBookOvertime(username,lend_id);
        return Result.ok();
    }

    @PostMapping("/admin/handleBorrowOvertime")
    public Result handleBookOvertime(@RequestParam("username") String username,
                                     @RequestParam("lend_id") String lend_id)
    {
        borrowService.handleBookOvertime(username,lend_id);
        return Result.ok();
    }
}
