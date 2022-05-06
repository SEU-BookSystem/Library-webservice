package booksystem.controller;

import booksystem.service.BookItemService;
import booksystem.utils.Result;
import booksystem.utils.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.util.List;

@CrossOrigin(origins="*",maxAge = 3600)
@RestController
public class BookItemController {
    @Autowired
    BookItemService bookItemService;

    @PostMapping("/admin/book/bookitem/add")
    public Result addBookItem(@RequestParam("num") int num,
                              @RequestParam("reference_num") String reference_num)
    {
        int result=bookItemService.addBookItem(num,reference_num);
        if(result==1)
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        else
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(),ResultEnum.UNKNOWN_ERROR.getMsg());
    }

    @RequestMapping("/book/getBookItemByReferenceNum")
    public Result getBookItemByReferenceNum(@RequestParam("reference_num") String reference_num)
    {
        return Result.ok().put("data",bookItemService.getBookItemByReferenceNum(reference_num));
    }

    @DeleteMapping("/admin/book/deleteBookItem")
    public Result deleteBookItem(@RequestParam("bar_code") int bar_code)
    {
        int result=bookItemService.deleteBookItem(bar_code);
        if(result==1)
            return Result.ok();
        else
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
    }

    @DeleteMapping("/admin/book/deleteMultiBookItem")
    public Result deleteMultiBookItem(@RequestParam("bar_codes") List<Integer> bar_codes)
    {
        bookItemService.deleteBookItems(bar_codes);
        return Result.ok();
    }

    @RequestMapping("/admin/getBookNumByStatus")
    public Result getBookNumByStatus(@RequestParam("status") int status)
    {
        return Result.ok().put("data",bookItemService.getBookNumByStatus(status));
    }

    @PostMapping("/admin/bookShelf")
    public Result bookShelf(@RequestParam("bar_codes") List<Integer> bar_codes)
    {
        bookItemService.bookShelf(bar_codes);
        return Result.ok();
    }
}
