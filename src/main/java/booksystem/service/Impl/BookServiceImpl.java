package booksystem.service.Impl;

import booksystem.dao.BookDao;
import booksystem.dao.BookItemDao;
import booksystem.pojo.Book;
import booksystem.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookDao bookDao;
    @Autowired
    BookItemDao bookItemDao;

    @Override
    public List<Map<String,Object>> getAllBook() {
        return bookDao.getAllBook();
    }

    @Override
    public void deleteBook(String isbn) {
        bookDao.deleteBook(isbn);
    }

    @Override
    public void deleteBooks(List<String> isbns) {
        bookDao.deleteBooks(isbns);
    }

    @Override
    public int addBook(String reference_num, String book_name, String author, String page_num,
                       String price, String isbn, String detail, String publisher, String image,
                       String date, String category_id, int num) {
        if(bookDao.getBookByReferenceNum(reference_num)==null&&num<0)
            return 0;
        Book book=new Book(reference_num,book_name,author,page_num,price,isbn,detail,publisher,image,date,category_id,num,null);
        bookDao.addBook(book);
        return 1;
    }

    @Override
    public String getMaxBook() {

        return null;
    }


    //
//    @Override
//    public int addBook(String book_name, String author, double price, int repertory, String press, String edition, String print_time, String main_category_id, String second_category_id, String shop_id) {
//        Book book=new Book(null,book_name,author,price,0,repertory,press,edition,print_time,"","",main_category_id,second_category_id,shop_id,null,null);
//        return bookDao.addBook(book);
//    }
//
//    @Override
//    public String selectBook(String book_name, String author, double price, String press, String edition, String print_time, String main_category_id, String second_category_id, String shop_id) {
//        return bookDao.selectBook(book_name,author,price,press,edition,print_time,main_category_id,second_category_id,shop_id);
//    }
//
//    @Override
//    public int deleteBook(String book_id) {
//        return bookDao.deleteBook(book_id);
//    }
//
    @Override
    public List<Map<String, Object>> getPage(int page_num,int book_num,int style,String main_id,String second_id,String year,String year_before,String year_after,String shop_id) {
        List<Map<String,Object>> res= bookDao.getPage((page_num-1)*book_num,book_num,style,main_id,second_id,year,year_before,year_after,shop_id);
        for(int i=0;i<res.size();i++){
            res.get(i).put("create_time",res.get(i).get("create_time").toString()
                    .replace('T',' '));
            res.get(i).put("update_time",res.get(i).get("update_time").toString()
                    .replace('T',' '));
            res.get(i).put("print_time",res.get(i).get("print_time").toString()
                    .replace('T',' '));

        }
        return res;
    }
//
//    @Override
//    public List<Map<String, Object>> fuzzyQuery(int page_num, int book_num, int style, int queryWhat, String content) {
//        List<Map<String,Object>> result= bookDao.fuzzyQuery((page_num-1)*book_num,book_num,style,queryWhat,content);
//        for(int i=0;i<result.size();i++){
//            result.get(i).put("create_time",result.get(i).get("create_time").toString()
//                    .replace('T',' '));
//            result.get(i).put("update_time",result.get(i).get("update_time").toString()
//                    .replace('T',' '));
//            result.get(i).put("print_time",result.get(i).get("print_time").toString()
//                    .replace('T',' '));
//        }
//        return result;
//    }


}
