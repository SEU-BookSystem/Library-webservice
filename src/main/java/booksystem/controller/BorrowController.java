package booksystem.controller;

import booksystem.dao.BorrowDao;
import booksystem.service.BorrowService;
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
public class BorrowController {
    @Autowired
    BorrowService borrowService;

    @Autowired
    BorrowDao borrowDao;

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

    /**
     * @param page_num 第几页
     * @param each_num 每页多少条数据
     * @param queryWhat 查询: 1:读者姓名、2:书籍名称、3:ISBN、
     * @param content 查询内容
     * @return
     */
    @RequestMapping("/admin/borrow/fuzzyQuery")
    public Result adminFuzzyQuery(@RequestParam("page_num")String page_num,
                                  @RequestParam("each_num")String each_num,
                                  @RequestParam("queryWhat") String queryWhat,//可缺省
                                  @RequestParam("content") String content//可缺省
    ) {
        if(page_num.isEmpty()||queryWhat.isEmpty()||each_num.isEmpty()){
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }
        List<Map<String,Object>> result=borrowDao.adminFuzzyQuery(
                (Integer.parseInt(page_num)-1)*Integer.parseInt(each_num),Integer.parseInt(each_num),
                Integer.parseInt(queryWhat),"%"+content+"%"
        );
        for(int i=0;i<result.size();i++){
            result.get(i).put("update_time",result.get(i).get("update_time").toString()
                    .replace('T',' '));
            result.get(i).put("start_time",result.get(i).get("start_time").toString()
                    .replace('T',' '));
            result.get(i).put("end_time",result.get(i).get("end_time").toString()
                    .replace('T',' '));
        }
        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",result);
    }

}
