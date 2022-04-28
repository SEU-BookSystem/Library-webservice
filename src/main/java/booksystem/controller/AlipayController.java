package booksystem.controller;

import booksystem.dao.BookDao;
import booksystem.dao.OrderBookDao;
import booksystem.dao.OrderDao;
import booksystem.pojo.AliPay;
import booksystem.service.PayService;
import com.alipay.api.AlipayApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class AlipayController {
    @Resource
    private PayService payService;//调用支付服务
    @Autowired
    OrderDao orderDao;
    @Autowired
    BookDao bookDao;
    @Autowired
    OrderBookDao orderBookDao;

    /*阿里支付*/
//    https://opendocs.alipay.com/apis/api_1/alipay.trade.precreate/      api文档
    @RequestMapping(value = "/alipay", produces = {"text/html;charset=UTF-8"})
    public String alipay(@RequestParam("order_id") String order_id,//订单id
                         @RequestParam("subject") String subject,//订单名称
                         @RequestParam("total_amount") String total_amount,//付款金额
                         @RequestParam("body") String body//商品描述，（可缺省
                         ) throws AlipayApiException {


        return  payService.aliPay(new AliPay(order_id,subject,total_amount,body));
    }

    @RequestMapping("alipay/return/ok")
    public String returnOk(HttpServletRequest request,
                           @RequestParam("out_trade_no") String order_id){
        orderDao.updateStatus(3,order_id);
        //获得订单中所有书的目录项
        List<Map<String,Object>> mapList=orderBookDao.getOrderBookByID(order_id);
        //遍历所有目录项
        for(Map<String,Object> map:mapList)
        {
            int number=Integer.valueOf(map.get("number").toString());
            int volume=Integer.valueOf(map.get("volume").toString());
            String book_id=map.get("book_id").toString();
//            bookDao.updateVolume(book_id,volume+number);
        }
        return "<h1>Pay Success</h1>";
    }

    @RequestMapping("alipay/return/error")
    public String returnError(HttpServletRequest request,
                              @RequestParam("out_trade_no") String order_id){
        return "<h1>Pay Error</h1>";
    }
}
