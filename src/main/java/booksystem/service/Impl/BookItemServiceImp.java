package booksystem.service.Impl;

import booksystem.dao.BookItemDao;
import booksystem.pojo.BookItem;
import booksystem.service.BookItemService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BookItemServiceImp implements BookItemService {
    @Autowired
    BookItemDao bookItemDao;

    @Override
    public int addBookItem(int num, String reference_num) {
        if(num>0)
        {
            for(int i=0;i<num;i++) {
                //默认为在库状态
                BookItem bookItem = new BookItem(0, reference_num, 1, "我爱读书一号馆");
                bookItemDao.addBookItem(bookItem);
            }
            return 1;
        }
        return 0;
    }

    @Override
    public void updateBookItem(int bar_code, String reference_num, int status, String address) {
        BookItem bookItem=new BookItem(bar_code,reference_num,status,address);
        bookItemDao.updateBookItem(bookItem);
    }

    @Override
    public void updateStatus(int bar_code, int status) {
        bookItemDao.updateStatus(bar_code,status);
    }

    @Override
    public List<Map<String, Object>> getBookItemByReferenceNum(String reference_num) {
        return bookItemDao.getBookItemByReferenceNum(reference_num);
    }

    @Override
    public int deleteBookItem(int bar_code) {
        if(bookItemDao.getBookItemByBarCode(bar_code)==null)
            return 0;
        bookItemDao.deleteBookItem(bar_code);
        return 1;
    }

    @Override
    public void deleteBookItems(List<Integer> bar_codes) {
        bookItemDao.deleteBookItems(bar_codes);
    }
}
