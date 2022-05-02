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
    public List<User> getAllUser() {
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
    public User getUserByID(String user_id) {
        return userDao.getUserByID(user_id);
    }

    @Override
    public void addUser(String username,String password,String id_card,int age,String gender) {
        userDao.addUser(username,password,id_card,age,gender);
    }

    @Override
    public void deleteUser(String user_id) {

    }

    @Override
    public int updateUser(String username,String password,String name,int age,String gender) {
        return userDao.updateUser(username,password,name,age,gender);
    }


    @Override
    public int updateCode(String username, String activationCode) {
        return userDao.updateCode(username,activationCode);
    }


    @Override
    public int sendMimeMail(String password, String email, String name) {
        try {
            if (email == null||email.isEmpty()) {
                return 2;
            }
            User user = userDao.getUserByName(email);
            if (user != null) {
                if (user.getStatus() == 0) {
                    userDao.deleteUser(user.getId());
                }
                else {
                    return -1;//用户已存在
                }
            }
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject("【教我编程图书商城】验证码邮件");//主题
            userDao.addUser(email, password, name, 0, "");
            //生成随机数
            String Code = randomCode();
            //更新验证码
            userDao.updateCode(email, Code);

            mailMessage.setText("亲爱的用户：\n" + "     您好！您正在使用邮箱验证，本次请求的验证码为：" + Code + "，本验证码5分钟内有效，请在5分钟内完成验证。" +
                    "（请勿泄露此验证码）如非本人操作，请忽略该邮件。（这是一封自动发送的邮件，请不要直接回复)\n" + "                                                            " +
                    "                                                     教我编程图书商城");//内容
            mailMessage.setTo(email);//发给谁

            mailMessage.setFrom(from);//你自己的邮箱

            mailSender.send(mailMessage);//发送
            userDao.updateTime(email);
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public String randomCode() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

    @Override
    public int register(String password, String email, String name, String activationCode) {
        User user = userDao.getUserByName(email);
        String Code = user.getActivationCode();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = user.getUpdate_time();
            Date date = format.parse(time);
            Date nowDate=new Date(System.currentTimeMillis());
            if((nowDate.getTime()-date.getTime())>=300000)//毫秒数 5分钟内有效
            {
                userDao.deleteUser(user.getId());
                return -1;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
            //System.out.print("Format To times:" + date.getTime());
            //如果email数据为空，或者不一致，注册失败
            if (email == null || email.isEmpty()) {
                //return "error,邮箱错误请重新注册";
                userDao.deleteUser(user.getUsername());
                return 0;
            } else if (!Code.equals(activationCode)) {
                //return "error,激活码错误请重新注册"
                userDao.deleteUser(user.getUsername());
                return 1;
            }
            //将数据写入数据库
            userDao.updateStatus(email, 1);
            userDao.updateTime(email);
            //跳转成功页面
            return 2;
    }

    @Override
    public void updateEmail(String user_id, String email) {
    }
}
