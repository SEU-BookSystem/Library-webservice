package booksystem.controller;

import booksystem.dao.OrderBookDao;
import booksystem.service.OrderBookService;
import booksystem.service.UploadImgService;
import booksystem.utils.Result;
import booksystem.utils.ResultEnum;
import booksystem.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class OrderBookController {
    @Autowired
    OrderBookService orderBookService;
    @Autowired
    UploadImgService uploadImgService;
    @Autowired
    OrderBookDao orderBookDao;

    //用户评价每本书
    @PostMapping("/order/remark")
    public Result updateRemark(@RequestParam("order_book_id") String order_book_id,
                               @RequestParam("remark") String remark,
                               @RequestParam("rate") double rate){
        int result=orderBookService.updateRemark(order_book_id,remark,rate);
        if(result==1)
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        }else if(result==-1)
        {
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }else {
            return Result.error(ResultEnum.UPDATE_FAIL.getCode(),ResultEnum.UPDATE_FAIL.getMsg());
        }
    }

    //用户退货
    @PostMapping("/order/return")
    public Result returnBook(@RequestParam("order_book_id") String order_book_id,
                               @RequestParam("return_reason") String return_reason,
                             @RequestParam("return_detail") String return_detail,
                             @RequestParam("transport_status") int transport_status){

        int result=orderBookService.returnBook(order_book_id,return_reason,return_detail,transport_status);
        if(result==1)
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        } else if(result==-2)
        {
            return Result.error(ResultEnum.APPLY_OUT_TIME.getCode(),ResultEnum.APPLY_OUT_TIME.getMsg());
        } else{
            return Result.error(ResultEnum.UPDATE_FAIL.getCode(),ResultEnum.UPDATE_FAIL.getMsg());
        }
    }

    //用户退货退款
    @PostMapping("/order/returnAll")
    public Result returnAll(@RequestParam("order_book_id") String order_book_id,
                             @RequestParam("return_reason") String return_reason,
                             @RequestParam("return_detail") String return_detail){

        int result=orderBookService.returnBook(order_book_id,return_reason,return_detail);
        if(result==1)
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        } else if(result==-2)
        {
            return Result.error(ResultEnum.APPLY_OUT_TIME.getCode(),ResultEnum.APPLY_OUT_TIME.getMsg());
        } else{
            return Result.error(ResultEnum.UPDATE_FAIL.getCode(),ResultEnum.UPDATE_FAIL.getMsg());
        }
    }


    //用户换货
    @PostMapping("/order/exchange")
    public Result exchangeBook(@RequestParam("order_book_id") String order_book_id,
                             @RequestParam("return_reason") String return_reason,
                             @RequestParam("return_detail") String return_detail,
                             @RequestParam("exchange_address_id") String exchange_address_id){

        int result=orderBookService.exchangeBook(order_book_id,return_reason,return_detail,2,exchange_address_id);
        if(result==1)
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        } else if(result==-2)
        {
            return Result.error(ResultEnum.APPLY_OUT_TIME.getCode(),ResultEnum.APPLY_OUT_TIME.getMsg());
        } else{
            return Result.error(ResultEnum.UPDATE_FAIL.getCode(),ResultEnum.UPDATE_FAIL.getMsg());
        }
    }

    //退还货凭证上传
    @PostMapping("/order/returnImg")
    public Result updateReturnImg(@RequestParam("order_book_id") String order_book_id,
                               @RequestParam("img") MultipartFile img) {
        uploadImgService.uploadReturnImg(img, order_book_id);
        return Result.ok(ResultEnum.SUCCESS.getMsg());
    }


    //获取所有订单
    @RequestMapping("/admin/getAllOrder")
    public Result getAllOrder()
    {
        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",orderBookService.getAllOrder());
    }

    //获取某用户的所有订单
    @RequestMapping("/order/getByUser")
    public Result getAllOrderByUser(ServletRequest request){
        String token=((HttpServletRequest)request).getHeader("token");
        String username= TokenUtils.parseToken(token).get("username").toString();

        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",orderBookService.getAllOrderByUser(username));
    }

    //根据shop_id获取所有订单
    @RequestMapping("/shop/order/getByShop")
    public Result getAllOrderByShop(@RequestParam("shop_id") String shop_id){
        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",orderBookService.getAllOrderByShop(shop_id));
    }

    //根据order_id获取某个订单
    @RequestMapping("/order/getByID")
    public Result getOrderByID(@RequestParam("order_id") String order_id){
        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",orderBookService.getOrderByOrderID(order_id));
    }

    //根据order_book_id获取某个订单
    @RequestMapping("/order/getBookByID")
    public Result getBookByID(@RequestParam("order_book_id") String order_book_id){
        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",orderBookService.getBookByID(order_book_id));
    }



    //根据order_book_id同意退货
    @PostMapping("/shop/returnPass")
    public Result returnPass(@RequestParam("order_book_id") String order_book_id){
        int result=orderBookService.refundOrder(order_book_id,2);
        if(result==1)
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        else
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(), ResultEnum.UNKNOWN_ERROR.getMsg());
    }
    //根据order_book_id批量同意退货
    @PostMapping("/shop/batReturnPass")
    public Result batReturnPass(@RequestParam("Order_Book_Ids") List<String> Order_Book_Ids){
        int result=orderBookService.batRefundOrder(Order_Book_Ids,2);
        if(result==1)
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        else
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(), ResultEnum.UNKNOWN_ERROR.getMsg());

    }
    //根据order_book_id拒绝退货
    @PostMapping("/shop/returnFail")
    public Result ReturnFail(@RequestParam("order_book_id") String order_book_id,
                                @RequestParam("check_opinion") String check_opinion){
        int result=orderBookService.failRefundOrder(order_book_id,check_opinion,3);
        if(result==1)
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        else
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(), ResultEnum.UNKNOWN_ERROR.getMsg());

    }
    //根据order_book_id批量拒绝退货
    @PostMapping("/shop/batReturnFail")
    public Result batReturnFail(@RequestParam("checkList") List<Map<String, Object>> checkList){
        int result=orderBookService.batFailRefundOrder(checkList,3);
        if(result==1)
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        else
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(), ResultEnum.UNKNOWN_ERROR.getMsg());
    }



    //根据order_book_id同意换货
    @PostMapping("/shop/exchangePass")
    public Result exchangePass(@RequestParam("order_book_id") String order_book_id){
        int result=orderBookService.refundOrder(order_book_id,5);
        if(result==1)
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        else
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(), ResultEnum.UNKNOWN_ERROR.getMsg());

    }
    //根据order_book_id批量同意换货
    @PostMapping("/shop/batExchangePass")
    public Result batExchangePass(@RequestParam("order_book_id") List<String> Order_Book_Ids){

        int result=orderBookService.batRefundOrder(Order_Book_Ids,5);
        if(result==1)
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        else
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(), ResultEnum.UNKNOWN_ERROR.getMsg());

    }
    //根据order_book_id拒绝换货
    @PostMapping("/shop/exchangeFail")
    public Result exchangeFail(@RequestParam("order_book_id") String order_book_id,
                                @RequestParam("check_opinion") String check_opinion){
        int result=orderBookService.failRefundOrder(order_book_id,check_opinion,6);
        if(result==1)
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        else
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(), ResultEnum.UNKNOWN_ERROR.getMsg());

    }
    //根据order_book_id批量拒绝换货
    @PostMapping("/shop/batExchangeFail")
    public Result batExchangeFail(@RequestParam("checkList") List<Map<String, Object>> checkList){
        int result=orderBookService.batFailRefundOrder(checkList,6);
        if(result==1)
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        else
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(), ResultEnum.UNKNOWN_ERROR.getMsg());
    }



    //根据order_book_id同意退货
    @PostMapping("/shop/returnAllPass")
    public Result returnAllPass(@RequestParam("order_book_id") String order_book_id){
        int result=orderBookService.refundOrder(order_book_id,8);
        if(result==1)
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        else
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(), ResultEnum.UNKNOWN_ERROR.getMsg());
    }
    //根据order_book_id批量同意退货
    @PostMapping("/shop/batReturnAllPass")
    public Result batReturnAllPass(@RequestParam("Order_Book_Ids") List<String> Order_Book_Ids){
        int result=orderBookService.batRefundOrder(Order_Book_Ids,8);
        if(result==1)
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        else
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(), ResultEnum.UNKNOWN_ERROR.getMsg());

    }
    //根据order_book_id拒绝退货
    @PostMapping("/shop/returnAllFail")
    public Result returnAllFail(@RequestParam("order_book_id") String order_book_id,
                                @RequestParam("check_opinion") String check_opinion){
        int result=orderBookService.failRefundOrder(order_book_id,check_opinion,9);
        if(result==1)
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        else
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(), ResultEnum.UNKNOWN_ERROR.getMsg());

    }
    //根据order_book_id批量拒绝退货
    @PostMapping("/shop/batReturnAllFail")
    public Result batReturnAllFail(@RequestParam("checkList") List<Map<String, Object>> checkList){
        int result=orderBookService.batFailRefundOrder(checkList,9);
        if(result==1)
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        else
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(), ResultEnum.UNKNOWN_ERROR.getMsg());
    }
    //获取书的评价
    @RequestMapping("/book/getRemark")
    public Result getBookRemark(@RequestParam("book_id")String book_id){
        List<Map<String,Object>> remarkList=orderBookService.getBookRemark(book_id);
        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",remarkList);
    }

    //获取书的评价
    @RequestMapping("/getRemarkPage")
    public Result getAllRemark(@RequestParam("page_num")String page_num,
                               @RequestParam("remark_num")String remark_num,
                               ServletRequest request){
        String token=((HttpServletRequest)request).getHeader("token");
        String username= TokenUtils.parseToken(token).get("username").toString();
        int identity= Integer.valueOf(TokenUtils.parseToken(token).get("identity").toString());
        if(identity==0)
        {
            return Result.error(ResultEnum.AUTHORITY_FAIL.getCode(),ResultEnum.AUTHORITY_FAIL.getMsg());
        }
        if(page_num.isEmpty()||remark_num.isEmpty()){
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }
        return Result.ok().put("data",orderBookService.getRemark(
                (Integer.parseInt(page_num)-1)*Integer.parseInt(remark_num),
                Integer.parseInt(remark_num),identity,username
        ));
    }
    //获取书的评价
    @RequestMapping("/getRemarkNum")
    public Result getRemarkNum(ServletRequest request){
        String token=((HttpServletRequest)request).getHeader("token");
        String username= TokenUtils.parseToken(token).get("username").toString();
        int identity= Integer.valueOf(TokenUtils.parseToken(token).get("identity").toString());
        if(identity==0)
        {
            return Result.error(ResultEnum.AUTHORITY_FAIL.getCode(),ResultEnum.AUTHORITY_FAIL.getMsg());
        }
        return Result.ok().put("data",orderBookService.getRemarkNum(identity,username));
    }

    /**
     *
     * @param request
     * @param page_num 第几页
     * @param order_num 每页多少书
     * @param status 0:全部, 1.申请退款未审核 2.同意 3.拒绝 4.换货申请未审核 5.换货同意 6.换货拒绝 7.退货退款申请未审核 8.退货退款通过 9.退货退款拒绝
     * @return
     */
    @RequestMapping("/order/reback")
    public Result reback(ServletRequest request,
                         @RequestParam("page_num")String page_num,
                         @RequestParam("order_num")String order_num,
                         @RequestParam("status")String status){
        String token=((HttpServletRequest)request).getHeader("token");
        String username= TokenUtils.parseToken(token).get("username").toString();
        int identity= Integer.valueOf(TokenUtils.parseToken(token).get("identity").toString());
        if(page_num.isEmpty()||order_num.isEmpty()||status.isEmpty()){
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }
        return Result.ok().put("data",orderBookService.getReback(
                (Integer.parseInt(page_num)-1)*Integer.parseInt(order_num),Integer.parseInt(order_num),
                Integer.parseInt(status),identity,username
        ));
    }

    /**
     *
     * @param request
     * @param status 0:全部, 1.申请退款未审核 2.同意 3.拒绝 4.换货申请未审核 5.换货同意 6.换货拒绝 7.退货退款申请未审核 8.退货退款通过 9.退货退款拒绝
     * @return
     */
    @RequestMapping("/order/rebackCount")
    public Result rebackCount(ServletRequest request,
                         @RequestParam("status")String status){
        String token=((HttpServletRequest)request).getHeader("token");
        String username= TokenUtils.parseToken(token).get("username").toString();
        int identity= Integer.valueOf(TokenUtils.parseToken(token).get("identity").toString());
        if(status.isEmpty()){
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }
        return Result.ok().put("data",orderBookDao.getRebackCount(
                Integer.parseInt(status),identity,username
        ));
    }

    //
    @PostMapping("/order/updateReturnStatus")
    public Result updateReturnStatus(@RequestParam("order_book_id") String order_book_id,
                                     @RequestParam("return_status") int return_status){
        orderBookDao.updateReturnStatus(order_book_id,"",return_status);
        return Result.ok();
    }
}
