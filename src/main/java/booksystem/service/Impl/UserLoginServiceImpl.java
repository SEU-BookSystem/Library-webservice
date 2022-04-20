package booksystem.service.Impl;

import booksystem.dao.UserLoginDao;
import booksystem.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLoginServiceImpl implements UserLoginService {
    @Autowired
    UserLoginDao userLoginDao;
    @Override
    public int userLogin(String username, String password) {
        return userLoginDao.userLogin(username,password);
    }

    @Override
    public int getIdentity(String username) {
        return userLoginDao.getIdentity(username);
    }

    @Override
    public int adminLogin(String username, String password) {
        return userLoginDao.adminLogin(username,password);
    }
}
