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

    @PostMapping("/user/book/addReserve")
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
        Map<String,Object> results=borrowService.addCollectionReserve(reference_nums,username);

        if(results.get("result").equals(1))
            return Result.ok();
        else if(results.get("result").equals(0))
            return Result.error(ResultEnum.BOOK_IS_NOT_ENOUGH.getCode(), ResultEnum.BOOK_IS_NOT_ENOUGH.getMsg())
                    .put("data",results.get("data").toString());
        else if(results.get("result").equals(-1))
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

    @PostMapping("/admin/setReserveOvertime")
    public Result setReserveOvertime(@RequestParam("username") String username,
                                     @RequestParam("lend_id") String lend_id)
    {
        borrowService.ReserveOvertime(username,lend_id);
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
     * @param borrow_reserve 0：预约  1：借阅
     * @return
     */
    @RequestMapping("/admin/borrow/query")
    public Result query(@RequestParam("page_num")int page_num,
                        @RequestParam("each_num")int each_num,
                        @RequestParam("borrow_reserve")int borrow_reserve
    ) {
        if(page_num<=0||each_num<=0){
            return Result.error(33,"数据必须为正");
        }
        int count=borrowDao.queryCount(borrow_reserve);
        int p_count=(count%each_num==0)?(count/each_num):(count/each_num+1);
        if(page_num>p_count&&p_count!=0){
            return Result.error(34,"页数超过范围");
        }
        List<Map<String,Object>> result=borrowDao.query(
                (page_num-1)*each_num,each_num,borrow_reserve
        );
        for(int i=0;i<result.size();i++){
            result.get(i).put("end_time",result.get(i).get("end_time").toString()
                    .replace('T',' '));
            result.get(i).put("update_time",result.get(i).get("update_time").toString()
                    .replace('T',' '));
            result.get(i).put("start_time",result.get(i).get("start_time").toString()
                    .replace('T',' '));
        }
        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("page_count",p_count).put("data",result);
    }

    /**
     * @param page_num 第几页
     * @param each_num 每页多少条数据
     * @param queryWhat 查询: 1:读者姓名、2:书籍名称、3:ISBN、4:条码号
     * @param content 查询内容
     * @param borrow_reserve 0：预约  1：借阅
     * @param is_history 0：正在借阅预约，1：借阅预约历史
     * @return
     */
    @RequestMapping("/admin/borrow/fuzzyQuery")
    public Result adminFuzzyQuery(@RequestParam("page_num")int page_num,
                                  @RequestParam("each_num")int each_num,
                                  @RequestParam("queryWhat") int queryWhat,//可缺省
                                  @RequestParam("content") String content,//可缺省
                                  @RequestParam("borrow_reserve")int borrow_reserve,
                                  @RequestParam("is_history") int is_history
    ) {

        if(page_num<=0||each_num<=0||is_history<0||borrow_reserve<0){
            return Result.error(33,"数据必须为正");
        }
        int count=borrowDao.adminFuzzyQueryCount(queryWhat,"%"+content+"%",borrow_reserve,is_history);
        int p_count=(count%each_num==0)?(count/each_num):(count/each_num+1);
        if(page_num>p_count&&p_count!=0||is_history>1||borrow_reserve>1){
            return Result.error(34,"页数超过范围");
        }
        List<Map<String,Object>> result=borrowDao.adminFuzzyQuery(
                (page_num-1)*each_num,each_num,
                queryWhat,"%"+content+"%",borrow_reserve,is_history
        );
        for(int i=0;i<result.size();i++){
            result.get(i).put("update_time",result.get(i).get("update_time").toString()
                    .replace('T',' '));
            result.get(i).put("start_time",result.get(i).get("start_time").toString()
                    .replace('T',' '));
            result.get(i).put("end_time",result.get(i).get("end_time").toString()
                    .replace('T',' '));
        }
        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("page_count",p_count).put("data",result);
    }


    /**
     *
     * @param borrow_reserve 0:预约，1：借阅 2：预约和借阅
     * @param request
     * @return
     */
    @RequestMapping("/user/borrow/queryBorrowing")
    public Result queryBorrowing(
                        @RequestParam("borrow_reserve")int borrow_reserve,
                        ServletRequest request
    ) {
        String token=((HttpServletRequest)request).getHeader("token");
        String username= TokenUtils.parseToken(token).get("username").toString();
        List<Map<String,Object>> result=borrowDao.queryBorrowing(
                borrow_reserve,username
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

    /**
     * @param page_num 第几页
     * @param each_num 每页多少条数据
     * @return
     */
    @RequestMapping("/user/borrow/queryBorrowed")
    public Result queryBorrowed(@RequestParam("page_num")int page_num,
                                @RequestParam("each_num")int each_num,
                                @RequestParam("borrow_reserve")int borrow_reserve,
                                ServletRequest request
    ) {
        String token=((HttpServletRequest)request).getHeader("token");
        String username= TokenUtils.parseToken(token).get("username").toString();
        if(page_num<=0||each_num<=0){
            return Result.error(33,"数据必须为正");
        }
        int count=borrowDao.queryBorrowedCount(borrow_reserve,username);
        int p_count=(count%each_num==0)?(count/each_num):(count/each_num+1);
        if(page_num>p_count&&p_count!=0){
            return Result.error(34,"页数超过范围");
        }
        List<Map<String,Object>> result=borrowDao.queryBorrowed(
                (page_num-1)*each_num,each_num,borrow_reserve,username
        );
        for(int i=0;i<result.size();i++){
            result.get(i).put("update_time",result.get(i).get("update_time").toString()
                    .replace('T',' '));
            result.get(i).put("start_time",result.get(i).get("start_time").toString()
                    .replace('T',' '));
            result.get(i).put("end_time",result.get(i).get("end_time").toString()
                    .replace('T',' '));
        }
        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("page_count",p_count).put("data",result);
    }

    @RequestMapping("/admin/getBorrowNumByStatus")
    public Result getBorrowNumByStatus(@RequestParam("number") int number)
    {
        return Result.ok().put("data",borrowService.getNumByStatus(number));
    }
}
