package booksystem.controller;

import booksystem.dao.OrderDao;
import booksystem.service.OrderBookService;
import booksystem.service.OrderService;
import booksystem.utils.Result;
import booksystem.utils.ResultEnum;
import booksystem.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@RestController
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    OrderDao orderDao;
    @Autowired
    OrderBookService orderBookService;

    //直接购买
    @PostMapping("/order/addDirect")
    public Result addDirect(@RequestParam("book_id") String book_id,
                            @RequestParam("sum") int sum,
                            @RequestParam("address_id") String address_id,
                            @RequestParam("shop_id") String shop_id,
                            ServletRequest request){
        String token=((HttpServletRequest)request).getHeader("token");
        String username= TokenUtils.parseToken(token).get("username").toString();
        String order_id=String.valueOf(UUID.randomUUID()).replace("-","").toString().toLowerCase();
        int result=orderService.addDirectOrder(book_id,sum,address_id,shop_id,username,order_id);
        if(result==1)
            return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",order_id);
        else
            return Result.error(ResultEnum.NUM_IS_NOT_ENOUGH.getCode(),ResultEnum.NUM_IS_NOT_ENOUGH.getMsg());
    }

    //购物车购买
    @PostMapping("/order/addCatiItem")
    public Result addCatiItem(@RequestParam("CartItem_Ids") List<String> CartItem_Ids,
                            @RequestParam("address_id") String address_id,
                            ServletRequest request){
        String token=((HttpServletRequest)request).getHeader("token");
        String username= TokenUtils.parseToken(token).get("username").toString();
        String order_id=String.valueOf(UUID.randomUUID()).replace("-","").toString().toLowerCase();
        int result=orderService.addCartItemOrder(CartItem_Ids,address_id,username,order_id);
        if(result==1)
            return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",order_id);
        else
            return Result.error(ResultEnum.NUM_IS_NOT_ENOUGH.getCode(),ResultEnum.NUM_IS_NOT_ENOUGH.getMsg());
    }


    //取消订单
    @PostMapping("/order/cancel")
    public Result cancelOrder(@RequestParam("order_id") String order_id){
        int result=orderService.cancelOrder(order_id);
        if(result==1)
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        }else if(result==-1)
        {
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }else {
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(),ResultEnum.UNKNOWN_ERROR.getMsg());
        }
    }

    //取消订单
    @PostMapping("/order/batCancel")
    public Result batCancelOrder(@RequestParam("Order_Ids") List<String> Order_Ids){
        int result=orderService.batCancelOrder(Order_Ids);
        if(result==1)
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        }else if(result==-1)
        {
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }else {
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(),ResultEnum.UNKNOWN_ERROR.getMsg());
        }
    }

    //确认订单
    @PostMapping("/order/firm")
    public Result firmlOrder(@RequestParam("order_id") String order_id){
        int result=orderService.firmOrder(order_id);
        if(result==1)
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        }else if(result==-1)
        {
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }else {
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(),ResultEnum.UNKNOWN_ERROR.getMsg());
        }
    }

    //确认订单
    @PostMapping("/order/batFirm")
    public Result batFirmlOrder(@RequestParam("Order_Ids") List<String> Order_Ids){
        int result=orderService.batFirmOrder(Order_Ids);
        if(result==1)
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        }else if(result==-1)
        {
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }else {
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(),ResultEnum.UNKNOWN_ERROR.getMsg());
        }
    }

    //订单发货
    @PostMapping("/shop/sendOrder")
    public Result sendOrder(@RequestParam("order_id") String order_id){
        int result=orderService.sendOrder(order_id);
        if(result==1)
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        }else if(result==-1)
        {
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }else {
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(),ResultEnum.UNKNOWN_ERROR.getMsg());
        }
    }

    //批量订单发货
    @PostMapping("/shop/batSendOrder")
    public Result batSendOrder(@RequestParam("Order_Ids") List<String> Order_Ids){
        int result=orderService.batSendOrder(Order_Ids);
        if(result==1)
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        }else if(result==-1)
        {
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }else {
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(),ResultEnum.UNKNOWN_ERROR.getMsg());
        }
    }

    //1：全部，2：未付款2，3：未发货3,4:未确认4，5：未评价5
    //批量订单发货

    /**
     * @param page_num 第几页
     * @param order_num 一页多少订单
     * @param status 0:全部,1：已取消，2：未付款，3：未发货,4:未确认，5：未评价
     * @param identity 0:以用户身份，也就是自己买的所有订单  1：商铺身份，自己店铺的所有订单   2：管理员身份，所有的订单
     * @return
     */
    @RequestMapping("/getOrder")
    public Result getOrder(@RequestParam("page_num")String page_num,
                           @RequestParam("order_num")String order_num,
                           @RequestParam("status")String status,
                           @RequestParam("identity") String identity,
                           ServletRequest request
                           ){

        String token=((HttpServletRequest)request).getHeader("token");
        String username= TokenUtils.parseToken(token).get("username").toString();
        int iden= Integer.parseInt(TokenUtils.parseToken(token).get("identity").toString());
        if(iden<Integer.parseInt(identity)){
            return Result.error(ResultEnum.AUTHORITY_FAIL.getCode(),ResultEnum.AUTHORITY_FAIL.getMsg());
        }
        if(page_num.isEmpty()||order_num.isEmpty()){
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }

        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",orderBookService.getOrder(
                (Integer.parseInt(page_num)-1)*Integer.parseInt(order_num),Integer.parseInt(order_num),
                Integer.parseInt(status),Integer.parseInt(identity),username
        ));
    }

    /**
     * @param page_num 第几页
     * @param order_num 一页多少订单
     * @param status 0:全部,1：已取消，2：未付款，3：未发货,4:未确认，5：未评价
     * @param content
     * @return
     */
    @RequestMapping("/order/fuzzyQuery")
    public Result fuzzyQuery(@RequestParam("page_num")String page_num,
                           @RequestParam("order_num")String order_num,
                             @RequestParam("status")String status,
                           @RequestParam("content") String content
    ){

        if(page_num.isEmpty()||order_num.isEmpty()){
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }

        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",orderBookService.fuzzyOrder(
                (Integer.parseInt(page_num)-1)*Integer.parseInt(order_num),Integer.parseInt(order_num),
                Integer.parseInt(status),"%"+content+"%"
        ));
    }

    /**
     * @param status 0:全部,1：已取消，2：未付款，3：未发货,4:未确认，5：未评价
     * @param content
     * @return
     */
    @RequestMapping("/order/fuzzyQueryCount")
    public Result fuzzyQueryCount(@RequestParam("status")String status,
                                  @RequestParam("content") String content
    ){

        if(status.isEmpty()){
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }

        return Result.ok(ResultEnum.SUCCESS.getMsg())
                .put("data",orderDao.fuzzyOrderCount(Integer.parseInt(status),"%"+content+"%"));
    }

    /**
     * @param status 1：全部，2：未付款2，3：未发货3,4:未确认4，5：未评价5
     * @param identity 0:以用户身份，也就是自己买的所有订单  1：商铺身份，自己店铺的所有订单   2：管理员身份，所有的订单
     * @return
     */
    //获取不同状态下的订单总数
    @RequestMapping("/getOrderNum")
    public Result getOrderNum(@RequestParam("status")String status,
                           @RequestParam("identity") String identity,
                           ServletRequest request){
        String token=((HttpServletRequest)request).getHeader("token");
        String username= TokenUtils.parseToken(token).get("username").toString();
        int iden= Integer.parseInt(TokenUtils.parseToken(token).get("identity").toString());
        if(iden<Integer.parseInt(identity)){
            return Result.error(ResultEnum.AUTHORITY_FAIL.getCode(),ResultEnum.AUTHORITY_FAIL.getMsg());
        }
        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",orderService.getOrderNum(Integer.valueOf(status),Integer.valueOf(identity),username));
    }

    //比较时间
    @PostMapping("/order/compareTime")
    public Result compareTime(@RequestParam("firm_time")String firm_time)
    {
        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",orderService.CompareTime(firm_time));
    }

    //管理员无条件修改状态
    @PostMapping("/admin/changeOrderStatus")
    public Result changeOrderStatus(@RequestParam("order_id")String order_id,
                                    @RequestParam("status") int status)
    {
        orderService.updateStatus(status,order_id);
        return Result.ok(ResultEnum.SUCCESS.getMsg());
    }
}
