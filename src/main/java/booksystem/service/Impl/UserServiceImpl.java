package booksystem.service.Impl;

import booksystem.dao.*;
import booksystem.pojo.User;
import booksystem.service.UploadImgService;
import booksystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    UploadImgService uploadImgService;
    @Autowired
    BookDao bookDao;

    @Value("${spring.mail.username}")
    String from;
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);

    @Override
    public List<Map<String,Object>> getAllUser() {
        return userDao.getAllUser();
    }

    @Override
    public int getAllUserNum() {
        return userDao.getAllUserNum();
    }

    @Override
    public User getUserByName(String username) {
        return userDao.getUserByName(username);
    }

    @Override
    public void addUser(String username,String password,String id_card,int age,String gender,String name) {
        userDao.addUser(username,password,id_card,age,gender,name);
    }

    @Override
    public void deleteUser(String username) {
        userDao.deleteUser(username);
    }

    @Override
    public int updateUser(String username,String password,String name,int age,String gender,String id_card) {
        return userDao.updateUser(username,password,name,age,gender,id_card);
    }

}
