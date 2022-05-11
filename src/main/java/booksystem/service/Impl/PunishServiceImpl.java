package booksystem.service.Impl;

import booksystem.dao.BookDao;
import booksystem.dao.BookItemDao;
import booksystem.pojo.Book;
import booksystem.service.BookService;
import booksystem.service.PunishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PunishServiceImpl implements PunishService {

    @Autowired
    PunishService punishService;


    @Override
    public int addPunish(String username, int bar_code) {
        return 0;
    }

    @Override
    public int handlePunish(String punish_id) {
        return 0;
    }

    @Override
    public int deletePunish(String punish_id) {
        return 0;
    }
}
