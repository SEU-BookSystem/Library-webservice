package booksystem.service.Impl;

import booksystem.dao.*;
import booksystem.pojo.Shop;
import booksystem.pojo.User;
import booksystem.service.ShopService;
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
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    UploadImgService uploadImgService;
    @Autowired
    ShopDao shopDao;
    @Autowired
    OrderDao orderDao;
    @Autowired
    OrderBookDao orderBookDao;
    @Autowired
    BookDao bookDao;
    @Autowired
    AddressDao addressDao;

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
    public void addUser(String email, String password, String name, int status, String activationCode) {
        userDao.addUser(email,password,name,status,activationCode);
    }

    @Override
    public void deleteUser(String user_id) {
        //删除用户的订单
        String username=userDao.getUserByID(user_id).getUsername();
        List<String> Order_Ids=orderDao.getAllOrderIDByUser(username);
        orderBookDao.deleteOrderBook(Order_Ids);
        orderDao.deleteOrder(Order_Ids);

        //删除地址
        addressDao.deleteAllAddress(user_id);

        //如果是商家 要注销店铺
        //获取要注销的店铺信息 此使为未注销状态
        List<Shop> shopList=shopDao.getShopByUserAndStatus(username,2,1,1);
        if(!shopList.isEmpty()) {
            //因为只允许一个未注销的存在  所以未注销的店铺信息存储在shop中
            Shop shop = shopList.get(0);

            //将所有店铺的书的库存置为-1 无法购买
            bookDao.updateStatus(shop.getId());
            //更改为注销状态 已审核2 通过1 注销2
            shop.setApply_status(2);
            shop.setPass_status(1);
            shop.setExist_status(2);
            shopDao.updateShop(shop);
        }
        uploadImgService.deleteUserImg(user_id);
        userDao.deleteUser(user_id);
    }

    @Override
    public int updateUser(String username,String password,String name) {
        return userDao.updateUser(username,password,name);
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
        User user=userDao.getUserByID(user_id);
        List<Shop> shopList=shopDao.getShopByUser(user.getUsername());
        for (Shop shop:shopList)
        {
            shop.setUsername(email);
            shopDao.updateShop(shop);
        }
        userDao.updateEmail(user_id,email);
    }
}
