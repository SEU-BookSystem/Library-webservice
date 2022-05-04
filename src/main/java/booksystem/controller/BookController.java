package booksystem.controller;

import booksystem.dao.BookDao;
import booksystem.pojo.Book;
import booksystem.service.BookItemService;
import booksystem.service.BookService;
import booksystem.utils.BookSpiderUtils;
import booksystem.utils.Result;
import booksystem.utils.ResultEnum;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins="*",maxAge = 3600)
@RestController
public class BookController {
    @Autowired
    BookService bookService;
    @Autowired
    BookItemService bookItemService;
    @Autowired
    BookDao bookDao;


    @RequestMapping("/book/getAllBook")
    public Result getAllBook(ServletRequest request)
    {
//        String token=((HttpServletRequest)request).getHeader("token");
        bookService.getAllBook();
        return Result.ok().put("data",bookService.getAllBook());
    }


    //单本删除
    @DeleteMapping("/admin/book/delete")
    public Result deleteBook(@RequestParam("isbn") String isbn) {
        if(isbn.isEmpty()) {
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(), ResultEnum.DATA_IS_NULL.getMsg());
        }
        bookService.deleteBook(isbn);
        return Result.ok(ResultEnum.SUCCESS.getMsg());
    }

    //批量删除
    @DeleteMapping("/admin/book/multiDelete")
    public Result multiDeleteBook(@RequestParam("isbns") List<String> isbns) {
        if(isbns.isEmpty()){
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }
//        bookDao.deleteBooks(isbns);
        return Result.ok(ResultEnum.SUCCESS.getMsg());
    }

    //添加图书
    @PostMapping("/admin/book/add")
    public Result addBook(@RequestParam("reference_num") String reference_num,
                          @RequestParam("book_name") String book_name,
                          @RequestParam("author") String author,
                          @RequestParam("page_num") String page_num,
                          @RequestParam("price") String price,
                          @RequestParam("isbn") String isbn,
                          @RequestParam("detail") String detail,
                          @RequestParam("publisher") String publisher,
                          @RequestParam("image") String image,
                          @RequestParam("date") String date,
                          @RequestParam("category_id") String category_id,
                          @RequestParam("num") int num)
    {
        int result1=bookService.addBook(reference_num,book_name,author,
                page_num,price,isbn,detail,publisher,image,date,category_id,num);
        if(result1==1)
        {
            int result2=bookItemService.addBookItem(num,reference_num);
            if (result2==1)
                return Result.ok();
        }
        return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(), ResultEnum.UNKNOWN_ERROR.getMsg());
    }

    /**
     *
     * @param request
     * @return 返回本周借阅最高的图书
     */
    @RequestMapping("/book/getMaxBook")
    public Result getMaxBook(ServletRequest request)
    {
//        String token=((HttpServletRequest)request).getHeader("token");
        String map=bookService.getMaxBook();
        if(map!=null)
        return Result.ok().put("data",map);
        else
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(), ResultEnum.UNKNOWN_ERROR.getMsg());
    }


//    @PostMapping("/spider")
//    public Result test(@RequestParam("id") int id,
//                       @RequestParam("count") int count,
//                       @RequestParam("limit") int limit)
//    {
//        BookSpiderUtils bookSpiderUtils=new BookSpiderUtils();
//        ArrayList<Book> bookList=bookSpiderUtils.getBooks(id,count,limit);
//        for(Book book:bookList)
//        {
//            bookDao.addBook(book);
//            bookItemService.addBookItem(book.getNum(),book.getReference_num());
//        }
//        return Result.ok();
//    }

//    /**
//     * @param page_num 第几页
//     * @param book_num 每页多少书
//     * @param style 排序方式 1:总销量,2:上架时间(所有),
//     * @param category_id 一级目录id
//     * @param year 年份筛选
//     * @return
//     */
//    @RequestMapping("/book/getPage")
//    public Result getPage(@RequestParam("page_num")String page_num,
//                          @RequestParam("book_num")String book_num,
//                          @RequestParam("style")String style,
//                          @RequestParam("main_category_id") String category_id,//可缺省
//                          @RequestParam("year") String year,   //可缺省
//                          @RequestParam("year_before") String year_before,   //可缺省
//                          @RequestParam("year_after") String year_after,   //可缺省
//                          ) {
//        if(page_num.isEmpty()||book_num.isEmpty()||style.isEmpty()){
//            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
//        }
//        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",bookService.getPage(
//                Integer.parseInt(page_num),Integer.parseInt(book_num),Integer.parseInt(style),
//                category_id,year,year_before,year_after
//        ));
//    }
//
//    /**
//     * @param main_category_id 一级目录id
//     * @param second_category_id 二级目录id
//     * @param year 年份筛选
//     * @param year_before
//     * @param year_after
//     * @param shop_id
//     * @return
//     */
//    @RequestMapping("/book/getPageCount")
//    public Result getPageCount(
//                          @RequestParam("main_category_id") String main_category_id,//可缺省
//                          @RequestParam("second_category_id") String second_category_id,//可缺省
//                          @RequestParam("year") String year,   //可缺省
//                          @RequestParam("year_before") String year_before,   //可缺省
//                          @RequestParam("year_after") String year_after,   //可缺省
//                          @RequestParam("shop_id") String shop_id   //可缺省
//    ) {
//        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("count",bookDao.getPageCount(
//                main_category_id,second_category_id,year,year_before,year_after,shop_id
//        ));
//    }
//
//    /**
//     * @param page_num 第几页
//     * @param book_num 每页多少书
//     * @param style 排序方式 1:总销量,2:上架时间(所有),
//     * @param queryWhat 查询: 1:按书名模糊查询,2:按作者模糊查询,3:按出版社模糊查询,4:按内容详情模糊查询
//     * @param content 查询内容
//     * @return
//     */
//    @RequestMapping("/book/fuzzyQuery")
//    public Result fuzzyQuery(@RequestParam("page_num")String page_num,
//                          @RequestParam("book_num")String book_num,
//                          @RequestParam("style")String style,
//                          @RequestParam("queryWhat") String queryWhat,//可缺省
//                          @RequestParam("content") String content//可缺省
//    ) {
//        if(page_num.isEmpty()||book_num.isEmpty()||style.isEmpty()||queryWhat.isEmpty()){
//            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
//        }
//        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",bookService.fuzzyQuery(
//                Integer.parseInt(page_num),Integer.parseInt(book_num),Integer.parseInt(style),
//                Integer.parseInt(queryWhat),"%"+content+"%"
//        ));
//    }
//
//
//    @RequestMapping("/book/fuzzyQueryCount")
//    public Result fuzzyQuery(
//                             @RequestParam("queryWhat") String queryWhat,
//                             @RequestParam("content") String content//可缺省
//    ) {
//        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("count",bookDao.fuzzyQueryCount(
//                Integer.parseInt(queryWhat),"%"+content+"%"
//        ));
//    }
//
//    @RequestMapping("/book/getDetail")
//    public Result getDetail(@RequestParam("book_id") String book_id){
//        if(book_id.isEmpty()){
//            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
//        }
//        Map<String,Object>map=bookDao.getDetail(book_id);
//        map.put("create_time",map.get("create_time").toString()
//                .replace('T',' '));
//        map.put("update_time",map.get("update_time").toString()
//                .replace('T',' '));
//        map.put("print_time",map.get("print_time").toString()
//                .replace('T',' '));
//        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",map);
//    }
//
//    @PostMapping("/shop/book/addBook")
//    public Result addBook(@RequestParam("book_name")String book_name,
//                          @RequestParam("author")String author,
//                          @RequestParam("price")String price,
//                          @RequestParam("repertory")String repertory,
//                          @RequestParam("press")String press,
//                          @RequestParam("edition")String edition,
//                          @RequestParam("print_time")String print_time,
//                          @RequestParam("main_category_id")String main_category_id,
//                          @RequestParam("second_category_id")String second_category_id,
//                          @RequestParam("shop_id")String shop_id,
//                          @RequestParam("img") MultipartFile img) {
//        String book_id=bookService.selectBook(book_name,author,Double.parseDouble(price),press,edition,print_time,main_category_id,second_category_id,shop_id);
//        if(!(book_id==null)){
//            return Result.error(ResultEnum.REPEAT_ADD.getCode(),ResultEnum.REPEAT_ADD.getMsg());
//        }
//        if(Double.parseDouble(price)<0||Integer.parseInt(repertory)<0){
//            return Result.error(ResultEnum.NUM_LOWER_ZERO.getCode(),ResultEnum.NUM_LOWER_ZERO.getMsg());
//        }
//        bookService.addBook(book_name,author,Double.parseDouble(price),Integer.parseInt(repertory),
//                press,edition,print_time,main_category_id,second_category_id,shop_id);
//        book_id=bookService.selectBook(book_name,author,Double.parseDouble(price),press,edition,
//                print_time,main_category_id,second_category_id,shop_id);
//        uploadImgService.uploadBookImg(img,book_id);
//
//        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("book_id",book_id);
//    }
//
//    @DeleteMapping("/shop/book/delete")
//    public Result deleteBook(@RequestParam("book_id") String book_id) {
//        if(book_id.isEmpty()){
//            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
//        }
//        uploadImgService.deleteBookImg(book_id);
//        bookService.deleteBook(book_id);
//        return Result.ok(ResultEnum.SUCCESS.getMsg());
//    }
//
//    @DeleteMapping("/shop/book/multiDelete")
//    public Result multiDeleteBook(@RequestParam("book_id") List<String> book_ids) {
//        if(book_ids.isEmpty()){
//            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
//        }
//        for(int i=0;i<book_ids.size();i++){
//            uploadImgService.deleteBookImg(book_ids.get(i));
//        }
//        bookDao.deleteBooks(book_ids);
//        return Result.ok(ResultEnum.SUCCESS.getMsg());
//    }
//
//    @PostMapping("/shop/book/updateBook")
//    public Result updateBook(@RequestParam("book_id")String book_id,
//                             @RequestParam("book_name")String book_name,
//                             @RequestParam("author")String author,
//                             @RequestParam("price")String price,
//                             @RequestParam("repertory")String repertory,
//                             @RequestParam("press")String press,
//                             @RequestParam("edition")String edition,
//                             @RequestParam("print_time")String print_time,
//                             @RequestParam("main_category_id")String main_category_id,
//                             @RequestParam("second_category_id")String second_category_id,
//                             @RequestParam("shop_id")String shop_id) {
//        if(book_id.isEmpty()){
//            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
//        }
//        if(Double.parseDouble(price)<0||Integer.parseInt(repertory)<0){
//            return Result.error(ResultEnum.NUM_LOWER_ZERO.getCode(),ResultEnum.NUM_LOWER_ZERO.getMsg());
//        }
//        bookDao.updateBook(book_id,book_name,author,Double.parseDouble(price),Integer.parseInt(repertory),
//                press,edition,print_time,main_category_id,second_category_id,shop_id);
//        return Result.ok(ResultEnum.SUCCESS.getMsg());
//    }
//    @PostMapping("/shop/book/updateImg")
//    public Result updateBook(@RequestParam("book_id")String book_id,
//                             @RequestParam("img") MultipartFile img) {
//        if(book_id.isEmpty()){
//            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
//        }
//
//        uploadImgService.uploadBookImg(img,book_id);
//        return Result.ok(ResultEnum.SUCCESS.getMsg());
//    }
//
//
//    //添加修改书籍详情
//    @PostMapping("/shop/book/updateDetail")
//    public Result updateDetail(@RequestParam("book_id")String book_id,
//                            @RequestParam("detail") String detail) {
//        if(book_id.isEmpty()){
//            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
//        }
//        bookDao.updateDetail(book_id,detail);
//        return Result.ok(ResultEnum.SUCCESS.getMsg());
//    }

}
