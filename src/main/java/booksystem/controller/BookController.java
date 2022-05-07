package booksystem.controller;

import booksystem.dao.BookDao;
import booksystem.dao.BookItemDao;
import booksystem.pojo.Book;
import booksystem.pojo.BookItem;
import booksystem.pojo.FtpServer;
import booksystem.service.BookItemService;
import booksystem.service.BookService;
import booksystem.utils.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import java.io.File;
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
    @Autowired
    BookItemDao bookItemDao;


    @RequestMapping("/book/getAllBook")
    public Result getAllBook(ServletRequest request)
    {
//        String token=((HttpServletRequest)request).getHeader("token");
        bookService.getAllBook();
        return Result.ok().put("data",bookService.getAllBook());
    }

    @RequestMapping("/book/getBookByReferenceNum")
    public Result getAllBook(@RequestParam("reference_num") String reference_num)
    {
        Map<String,Object>map=bookDao.getBookByReferenceNum(reference_num);
        if(map!=null){
            map.put("update_time",map.get("update_time").toString()
                    .replace('T',' '));
            return Result.ok().put("data",map);
        }else
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(), ResultEnum.UNKNOWN_ERROR.getMsg());

    }


    //单本删除
    @DeleteMapping("/admin/book/delete")
    public Result deleteBook(@RequestParam("reference_num") String reference_num) {
        if(reference_num.isEmpty()) {
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(), ResultEnum.DATA_IS_NULL.getMsg());
        }
        bookDao.deleteBookByReferenceNum(reference_num);
        return Result.ok(ResultEnum.SUCCESS.getMsg());
    }

    //单本删除
    @PostMapping("/admin/book/addBook")
    public Result addBook(@RequestParam("book_name") String book_name,
                          @RequestParam("author") String author,
                          @RequestParam("page_num") String page_num,
                          @RequestParam("price") String price,
                          @RequestParam("isbn") String isbn,
                          @RequestParam("detail") String detail,
                          @RequestParam("publisher") String publisher,
                          @RequestParam("image") MultipartFile img,
                          @RequestParam("date") String date,
                          @RequestParam("category_id") String category_id,
                          @RequestParam("num") int num) {
        if(isbn.isEmpty()||book_name.isEmpty()||category_id.isEmpty()) {
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(), ResultEnum.DATA_IS_NULL.getMsg());
        }
        if(num<=0) {
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(), "num必须大于0");
        }
        Map<String,Object>m1=bookDao.getBookByIsbn(isbn);
        if(m1==null){
            int n=bookDao.getBookNum();
            String ref=category_id+"-"+ (100000 + n);

            File imgFile= SFTP.multipartFileToFile(img);
            String image=SFTP.uploadImg(imgFile);

            Book book=new Book(ref,book_name,author,page_num,price,isbn,detail,publisher,image,date,category_id,num,"");
            bookDao.addBook(book);
            for(int i=0;i<num;i++){
                BookItem bookItem=new BookItem(0,ref,1,"我爱读书一号馆");
                bookItemDao.addBookItem(bookItem);
            }

            bookDao.updateBookNum(ref,-num);
        }else{
            for(int i=0;i<num;i++){
                BookItem bookItem=new BookItem(0,m1.get("reference_num").toString(),1,"我爱读书一号馆");
                bookItemDao.addBookItem(bookItem);
            }
            bookDao.updateBookNum(m1.get("reference_num").toString(),-num);
            return Result.error(201,"书籍已经存在，书目已入库，书籍信息未更改");
        }

        return Result.ok(ResultEnum.SUCCESS.getMsg());
    }


    @PostMapping("/admin/book/updateBook")
    public Result updateBook(@RequestParam("reference_num") String reference_num,
                             @RequestParam("book_name") String book_name,
                             @RequestParam("author") String author,
                             @RequestParam("page_num") String page_num,
                             @RequestParam("price") String price,
                             @RequestParam("isbn") String isbn,
                             @RequestParam("detail") String detail,
                             @RequestParam("publisher") String publisher,
                             @RequestParam("date") String date,
                             @RequestParam("category_id") String category_id) {
        if(reference_num.isEmpty()) {
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(), ResultEnum.DATA_IS_NULL.getMsg());
        }

        Map<String,Object>m1=bookDao.getBookByReferenceNum(reference_num);
        if(m1==null){
            return Result.error(404,"书籍不存在");
        }else{
            String image=bookDao.getBookImg(reference_num);
            String old=reference_num;
            int num=bookDao.getBookNumByRef(reference_num);
            if(category_id.equals(m1.get("category_id").toString())){

            }else{
                reference_num=category_id+reference_num.substring(2);
            }
            bookDao.updateBook(reference_num,book_name,author,page_num,price,isbn,detail,publisher,image,date,category_id,num,old);
            bookItemDao.updateRef(old,reference_num);
        }

        return Result.ok(ResultEnum.SUCCESS.getMsg());
    }


    @PostMapping("/admin/book/updateImg")
    public Result updateBookImg(@RequestParam("reference_num") String reference_num,
                             @RequestParam("image") MultipartFile img) {
        if(reference_num.isEmpty()){
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }
        String imagePre=bookDao.getBookImg(reference_num);
        if(imagePre==null){
            return Result.error(404,"书籍不存在");
        }
        if(imagePre.contains("http://47.100.78.158:8080/img/library")){
            imagePre=imagePre.replace("http://47.100.78.158:8080/img/library","");
            SFTP.deleteImg(FtpServer.imgUrl + imagePre);

        }

        File imgFile= SFTP.multipartFileToFile(img);
        String image=SFTP.uploadImg(imgFile);
        bookDao.updateBookImg(reference_num,image);
        return Result.ok(ResultEnum.SUCCESS.getMsg());
    }
    //批量删除
    @DeleteMapping("/admin/book/multiDelete")
    public Result multiDeleteBook(@RequestParam("reference_nums") List<String> reference_nums) {
        if(reference_nums.isEmpty()){
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
        List<Map<String,Object>> map=bookDao.getBorrowBook(Integer.MAX_VALUE);

        if(map!=null){
            Map<String,Object>map1=bookDao.getBookByReferenceNum(map.get(0).get("reference_num").toString());
            map1.put("update_time",map1.get("update_time").toString()
                    .replace('T',' '));
            return Result.ok().put("data",map1);
        }
        else
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(), ResultEnum.UNKNOWN_ERROR.getMsg());
    }

    /**
     *
     * @param num 最热多少本
     * @param category 种类：0：图书，1：期刊
     * @return
     */
    @RequestMapping("/book/getHotBook")
    public Result getHotBook(@RequestParam("num")String num,
                             @RequestParam("category")String category)
    {
        List<Map<String,Object>> map=bookDao.getHotBook(Integer.parseInt(num),Integer.parseInt(category));

        if(map!=null){
            for(int i=0;i<map.size();i++){
                map.get(i).put("update_time",map.get(i).get("update_time").toString()
                        .replace('T',' '));
            }
            return Result.ok().put("data",map);
        }
        else
            return Result.error(ResultEnum.UNKNOWN_ERROR.getCode(), ResultEnum.UNKNOWN_ERROR.getMsg());
    }

    /**
     * @param page_num 第几页
     * @param book_num 每页多少书
     * @return
     */
    @RequestMapping("/book/getPageByYear")
    public Result getPageByYear(@RequestParam("page_num")String page_num,
                          @RequestParam("book_num")String book_num,
                          @RequestParam("year_before") String year_before,
                          @RequestParam("year_after") String year_after
                          ) {
        if(page_num.isEmpty()||book_num.isEmpty()){
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }
        List<Map<String,Object>> result=bookDao.getPageByYear(
                (Integer.parseInt(page_num)-1)*Integer.parseInt(book_num),Integer.parseInt(book_num),year_before,year_after
        );
        for(int i=0;i<result.size();i++){
            result.get(i).put("update_time",result.get(i).get("update_time").toString()
                    .replace('T',' '));
        }
        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",result);
    }

    /**
     *
     * @param page_num 第几页
     * @param book_num 每页多少书
     * @param category 种类：0：社科，1：文学，2：自然科学，3，其他
     * @return
     */
    @RequestMapping("/book/getMainPage")
    public Result getPageByCategory(@RequestParam("page_num")String page_num,
                          @RequestParam("book_num")String book_num,
                          @RequestParam("category") String category
    ) {
        if(page_num.isEmpty()||book_num.isEmpty()){
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }
        List<Map<String,Object>> result=bookDao.getMainPage(
                (Integer.parseInt(page_num)-1)*Integer.parseInt(book_num),Integer.parseInt(book_num),Integer.parseInt(category)
        );
        for(int i=0;i<result.size();i++){
            result.get(i).put("update_time",result.get(i).get("update_time").toString()
                    .replace('T',' '));
        }
        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",result);
    }

    @RequestMapping("/book/getNewBook")
    public Result getNewBook(@RequestParam("num") String num) {
        if(num.isEmpty()){
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }
        List<Map<String,Object>> result=bookDao.getNewBook(
                Integer.parseInt(num)
        );
        for(int i=0;i<result.size();i++){
            result.get(i).put("update_time",result.get(i).get("update_time").toString()
                    .replace('T',' '));
        }
        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",result);
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
    /**
     * @param page_num 第几页
     * @param book_num 每页多少书
     * @param queryWhat 查询: 1:按书名模糊查询,2:按作者模糊查询,3:按出版社模糊查询,4:按内容详情模糊查询
     * @param content 查询内容
     * @return
     */
    @RequestMapping("/book/fuzzyQuery")
    public Result fuzzyQuery(@RequestParam("page_num")String page_num,
                          @RequestParam("book_num")String book_num,
                          @RequestParam("queryWhat") String queryWhat,//可缺省
                          @RequestParam("content") String content//可缺省
    ) {
        if(page_num.isEmpty()||book_num.isEmpty()||queryWhat.isEmpty()){
            return Result.error(ResultEnum.DATA_IS_NULL.getCode(),ResultEnum.DATA_IS_NULL.getMsg());
        }
        List<Map<String,Object>> result=bookDao.fuzzyQuery(
                (Integer.parseInt(page_num)-1)*Integer.parseInt(book_num),Integer.parseInt(book_num),
                Integer.parseInt(queryWhat),"%"+content+"%"
        );
        for(int i=0;i<result.size();i++){
            result.get(i).put("update_time",result.get(i).get("update_time").toString()
                    .replace('T',' '));
        }
        return Result.ok(ResultEnum.SUCCESS.getMsg()).put("data",result);
    }
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
