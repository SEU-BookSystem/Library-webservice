package booksystem.controller;

import booksystem.service.CartItemService;
import booksystem.utils.Result;
import booksystem.utils.ResultEnum;
import booksystem.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class CartitemController {
    @Autowired
    CartItemService cartItemService;


    @RequestMapping("/cartitem/getAll")
    public Result getAllByUser(ServletRequest request){
        String token=((HttpServletRequest)request).getHeader("token");
        String username= TokenUtils.parseToken(token).get("username").toString();

        return Result.ok().put("data",cartItemService.getCartItem(username));
    }

    @PostMapping("/cartitem/addCartItem")
    public Result addCartItem(@RequestParam("book_id") String book_id,
                              @RequestParam("sum")int sum,
                              ServletRequest request){

        String token=((HttpServletRequest)request).getHeader("token");
        String username= TokenUtils.parseToken(token).get("username").toString();
        int result=cartItemService.addCartItem(book_id,username,sum);
        if(result==1)
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        }else if(result==-1){
            return Result.error(ResultEnum.NUM_IS_NOT_ENOUGH.getCode(),ResultEnum.NUM_IS_NOT_ENOUGH.getMsg());
        }else{
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(),ResultEnum.UNKNOWN_ERROR.getMsg());
        }
    }


    @PostMapping("/cartitem/updateCartItem")
    public Result updateCartItem(@RequestParam("book_id") String book_id,
                                 @RequestParam("sum")int sum,
                                 ServletRequest request){
        if(sum<=0)
        {
            return Result.error(ResultEnum.NUM_FAIL.getCode(), ResultEnum.NUM_FAIL.getMsg());
        }
        String token=((HttpServletRequest)request).getHeader("token");
        String username= TokenUtils.parseToken(token).get("username").toString();
        int result=cartItemService.updateCartItem(book_id,username,sum);
        if(result==1)
        {
            return Result.ok(ResultEnum.SUCCESS.getMsg());
        }else {
            return Result.error(ResultEnum.NUM_IS_NOT_ENOUGH.getCode(), ResultEnum.NUM_IS_NOT_ENOUGH.getMsg());
        }
    }

    @DeleteMapping("/cartitem/multiDelete")
    public Result deleteCartItem(@RequestParam("CartItem_Ids") List<String> CartItem_Ids) {
        cartItemService.multiDeleteCartItem(CartItem_Ids);
        return Result.ok(ResultEnum.SUCCESS.getMsg());
    }

    @DeleteMapping("/cartitem/delete")
    public Result deleteCartItem(@RequestParam("cartItem_id") String cartItem_id) {
        cartItemService.deleteCartItem(cartItem_id);
        return Result.ok(ResultEnum.SUCCESS.getMsg());
    }

    @RequestMapping("/cartitem/getNum")
    public Result getCartItemNum(ServletRequest request) {
        String token = ((HttpServletRequest) request).getHeader("token");
        String username = TokenUtils.parseToken(token).get("username").toString();
        int result=cartItemService.getCartItemNum(username);
        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",result);
    }
}