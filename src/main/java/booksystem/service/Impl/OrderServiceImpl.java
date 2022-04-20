package booksystem.service.Impl;

import booksystem.dao.*;
import booksystem.pojo.Order;
import booksystem.pojo.OrderBook;
import booksystem.service.OrderService;
import booksystem.utils.OrderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDao orderDao;
    @Autowired
    BookDao bookDao;
    @Autowired
    UserDao userDao;
    @Autowired
    OrderBookDao orderBookDao;
    @Autowired
    CartItemDao cartItemDao;


    @Override
    public int cancelOrder(String order_id) {
        List<Map<String,Object>> order=orderBookDao.getOrderBookByID(order_id);
        if(order.isEmpty())
            return -1;//订单不存在

        //判断取消订单是在 未付款的时候
        if(2==Integer.valueOf(order.get(0).get("status").toString())||3==Integer.valueOf(order.get(0).get("status").toString()))
        {
            orderDao.updateStatus(1,order_id);//将状态改为取消订单
            List<Map<String,Object>> order_book_list=orderBookDao.getOrderBookByID(order_id);
            //更改库存
            for(Map<String,Object> book:order_book_list) {
                bookDao.updateRepertory(book.get("book_id").toString(),
                        Integer.valueOf(book.get("repertory").toString())+Integer.valueOf(book.get("number").toString()));
            }
            return 1;
        }
        return 0;//处于其他状态不可取消
    }

    @Override
    public int batCancelOrder(List<String> Order_Ids) {
        for(String order_id:Order_Ids)
        {
            List<Map<String,Object>> order=orderBookDao.getOrderBookByID(order_id);
            if(order.isEmpty())
                return -1;//订单不存在

            //判断取消订单是在 未付款的时候
            if(Integer.valueOf(order.get(0).get("status").toString())!=2&&3!=Integer.valueOf(order.get(0).get("status").toString()))
            {
                return 0;
            }
        }
        for(String order_id:Order_Ids) {
            orderDao.updateStatus(1, order_id);//将状态改为取消订单
            //更改库存
            List<Map<String,Object>> order_book_list=orderBookDao.getOrderBookByID(order_id);
            for(Map<String,Object> book:order_book_list) {
                bookDao.updateRepertory(book.get("book_id").toString(),
                        Integer.valueOf(book.get("repertory").toString())+Integer.valueOf(book.get("number").toString()));
            }
        }
        return 1;
    }

    @Override
    public int firmOrder(String order_id) {
        List<Map<String,Object>> order=orderBookDao.getOrderBookByID(order_id);
        if(order.isEmpty())
            return -1;//订单不存在

        //判断取消订单是在 已发货之后的时候
        if(4==Integer.valueOf(order.get(0).get("status").toString()))
        {
            orderDao.updateStatus(5,order_id);//将状态改为确认订单
            orderDao.updateFirmTime(order_id);
            return 1;
        }
        return 0;//处于其他状态不可取消
    }

    @Override
    public int batFirmOrder(List<String> Order_Ids) {
        for(String order_id:Order_Ids)
        {
            List<Map<String,Object>> order=orderBookDao.getOrderBookByID(order_id);
            if(order.isEmpty())
                return -1;//订单不存在

            //判断取消订单是在 已发货的时候
            if(Integer.valueOf(order.get(0).get("status").toString())!=4)
                return 0;
        }
        for(String order_id:Order_Ids) {
            orderDao.updateStatus(5, order_id);//将状态改为确认订单
        }
        orderDao.batUpdateFirmTime(Order_Ids);
        return 1;
    }

    @Override
    public int sendOrder(String order_id) {
        List<Map<String,Object>> order=orderBookDao.getOrderBookByID(order_id);
        if(order.isEmpty())
            return -1;//订单不存在

        //判断取消订单是在 未发货之后的时候
        if(3==Integer.valueOf(order.get(0).get("status").toString()))
        {
            orderDao.updateStatus(4,order_id);//将状态改为已发货订单
            orderDao.updateSendTime(order_id);
            return 1;
        }
        return 0;//处于其他状态不可取消
    }

    @Override
    public int batSendOrder(List<String> Order_Ids) {
        for(String order_id:Order_Ids)
        {
            List<Map<String,Object>> order=orderBookDao.getOrderBookByID(order_id);
            if(order.isEmpty())
                return -1;//订单不存在

            //判断取消订单是在 已发货的时候
            if(Integer.valueOf(order.get(0).get("status").toString())!=3)
                return 0;
        }
        for(String order_id:Order_Ids) {
            orderDao.updateStatus(4, order_id);//将状态改为确认订单
        }
        orderDao.batUpdateSendTime(Order_Ids);
        return 1;
    }

    @Override
    public int getOrderNum(int status, int identity, String username) {
        return orderDao.getOrderNum(status,identity,username);
    }


    @Override
    public int addCartItemOrder(List<String> CartItem_Ids, String address_id, String username,String order_id) {
        List<Map<String,Object>> CartItemList=cartItemDao.getCartItemByID(CartItem_Ids);

        int length=CartItemList.size();

        String[] shop_ids=new String[length];
        int[] sums=new int[length];
        double[] price=new double[length];
        int[] repertory=new int[length];
        List<OrderBook> orderBookList=new ArrayList<OrderBook>();
        OrderBook orderBook=null;

        //切割 分成order_book项
        for( int j=0;j<length;j++) {
            String book_id=CartItemList.get(j).get("book_id").toString();
            sums[j]=Integer.valueOf(CartItemList.get(j).get("sum").toString());

            HashMap<String,Object> book=bookDao.getBookByID(book_id);
            shop_ids[j]=book.get("shop_id").toString();
            price[j]=Double.valueOf(book.get("price").toString());

            //检查库存
            int repertory1=Integer.valueOf(book.get("repertory").toString());
            if(sums[j]>repertory1&&repertory1<=0)
                return -1;//库存不足

            repertory[j]=repertory1;
            orderBook=new OrderBook(null,book_id,"",sums[j],"",5.0,0,-1,"","",null,(double)(price[j]*sums[j]),"","",null,"",-1,"");
            orderBookList.add(j,orderBook);
        }

        //去重 得到不重复的shop_id
        Set shopIdsSet=new HashSet();
        List<String> shop_id_temp=new ArrayList<String>();
        for(String shop_id:shop_ids)
        {
            if(shopIdsSet.add(shop_id))
                shop_id_temp.add(shop_id);
        }

        //根据shop_id形成订单
        for(int k=0;k<shop_id_temp.size();k++) //订单的数量
        {
            //计算总价
            double total=0;

            //查找统一订单的图书
            for (int i = 0; i < length; i++)
            {
                //是同一家店
                if (shop_id_temp.get(k).equals(shop_ids[i])) {
                    orderBookList.get(i).setOrder_id(order_id);
                    orderBookDao.addOrderBook(orderBookList.get(i));
                    //更改库存
                    bookDao.updateRepertory(CartItemList.get(i).get("book_id").toString(),repertory[i]-sums[i]);
                    total+=sums[i]*price[i];
                    //删除购物车的东西
                    cartItemDao.deleteCartItem(CartItem_Ids.get(i));
                }
            }
            Order order=new Order(order_id,total,2,address_id,shop_id_temp.get(k),userDao.getUserByName(username).getId(),null,null,null,null);
            orderDao.addOrder(order);
        }
        return 1;
    }


    @Override
    public int addDirectOrder(String book_id, int sum,String address_id,String shop_id,String username,String order_id) {
        HashMap<String,Object> book=bookDao.getBookByID(book_id);
        int repertory=Integer.valueOf(book.get("repertory").toString());
        if(sum>repertory&&repertory<=0)
            return -1;//库存不足
        //更改库存
        bookDao.updateRepertory(book_id,repertory-sum);
        double total=sum*Double.valueOf(book.get("price").toString());
        //订单状态为2未付款 退款状态为-1 无效
        Order order=new Order(order_id,total,2,address_id,shop_id,userDao.getUserByName(username).getId(),null,null,null,null);
        orderDao.addOrder(order);
        OrderBook orderBook=new OrderBook("",book_id,order_id,sum,"",5.0,0,-1,"","",null,total,"","",null,"",-1,"");
        orderBookDao.addOrderBook(orderBook);
        return 1;
    }

    @Override
    public int CompareTime(String firm_time) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
            Date firm = simpleDateFormat.parse(firm_time);
            Date nowDate = new Date(System.currentTimeMillis());
            long difference = nowDate.getTime() - firm.getTime();
            if (difference <= (1000 * 60 * 60 * 24 * 7))
                return 0;//超时了
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    @Override
    public void updateStatus(int status, String order_id) {
        orderDao.updateStatus(status,order_id);
    }

}
