package booksystem.controller;

import booksystem.dao.BookItemDao;
import booksystem.service.BookItemService;
import booksystem.utils.Result;
import booksystem.utils.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins="*",maxAge = 3600)
@RestController
public class BookItemController {
    @Autowired
    BookItemService bookItemService;
    @Autowired
    BookItemDao bookItemDao;

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

    /**
     *
     * @param page_num
     * @param each_num
     * @param style 1: 所有 2：按照reference查询  3：按照bar_code查询
     * @param content
     * @return
     */
    @RequestMapping("/admin/book/getBookItem")
    public Result getBookItem(@RequestParam("page_num")int page_num,
                              @RequestParam("each_num")int each_num,
                              @RequestParam("style") int style,
                              @RequestParam("content") String content
                              )
    {
        if(page_num<=0||each_num<=0){
            return Result.error(33,"数据必须为正");
        }
        int count=bookItemDao.getBookItemCount(style,content);
        int p_count=(count%each_num==0)?(count/each_num):(count/each_num+1);
        if(page_num>p_count&&p_count!=0){
            return Result.error(34,"页数超过范围");
        }
        return Result.ok().put("data",bookItemDao.getBookItem((page_num-1)*each_num,each_num,style,content)).put("page_count",p_count);
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
