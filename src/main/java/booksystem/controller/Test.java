package booksystem.controller;

import booksystem.dao.BookDao;
import booksystem.service.UploadImgService;
import booksystem.utils.TokenUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

@RestController
public class Test {
    @Autowired
    UploadImgService uploadImgService;
    @Autowired
    BookDao bookDao;
//    @Autowired
//    DataDao dataDao;
//    @Resource
//    private PayService payService;//调用支付服务




    @PostMapping("/testBook")
    public String testBook(@RequestParam("book_id") String book_id,
                           @RequestParam("volume") String volume,
                           @RequestParam("repertory") String repertory){
//        bookDao.updateRepertory(book_id,Integer.parseInt(repertory));
//        bookDao.updateVolume(book_id,Integer.parseInt(volume));
        return "OK";
    }

    @RequestMapping(value="/test/upload",method=RequestMethod.POST)
    @ResponseBody
    public String uploadImg(@RequestParam("img") MultipartFile img, ServletRequest request) {
        String token=((HttpServletRequest)request).getHeader("token");
        String username= TokenUtils.parseToken(token).get("username").toString();
        // 判断文件是否为空
        if(!img.isEmpty()) {
            uploadImgService.uploadUserImg(img,username);
        }
            return "SUCCESS";
    }




    @RequestMapping(value = "/favicon.ico")
    public String fav(){
        return  "laalalalalal)";
    }



    @RequestMapping("alipay/callback/return_sult")
    public String toIndex(){
        return "index";
    }

    @RequestMapping("/getOrderId")
    public String get(){
        return "index";
    }


    @GetMapping("/test")
    public String test(){
//        List<Map<String,Object>>list=dataDao.getData(1,20);
//        System.out.println(list);
        return "ok";
    }

    @GetMapping("/test/return")
    public String orderReturn(HttpServletRequest request,
                              @RequestParam("out_trade_no") String order_id){
        System.out.println(order_id);
        return order_id;
    }




}
