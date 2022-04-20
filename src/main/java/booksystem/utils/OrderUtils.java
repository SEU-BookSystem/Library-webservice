package booksystem.utils;

import java.util.*;

public class OrderUtils {

    //按照店铺显示
    //书名 出版社 create_time 价格 total 状态 评价 评分
    public static List<Map<String,Object>> OrderOutput(List<Map<String,Object>> mapList){
        class Group{
            public String shop_id;
            public String shop_name;
            public String create_time;
            public String total;
            public String order_id;
            public String username;
            public String status;
            public String firm_time;
            public String send_time;
            public String address_id;
            public String name;
            public Group(String id,String Shop_name,String oid,String total_money,String time,
                         String uname,String sta,String ftime,String stime,String address,String Name){
                shop_id=id;
                shop_name=Shop_name;
                create_time=time;
                order_id=oid;
                total=total_money;
                username=uname;
                status=sta;
                firm_time=ftime;
                send_time=stime;
                address_id=address;
                name=Name;
            }
        }
        List<Map<String,Object>> res=new ArrayList<>();
        List<Group> allShop=new ArrayList<>();
        if(mapList.isEmpty())
            return null;
        
        for(Map<String,Object> map:mapList) {
            allShop.add(new Group(map.get("shop_id").toString(),
                    map.get("shop_name").toString(),
                    map.get("order_id").toString(),
                    map.get("total").toString(),
                    map.get("create_time").toString().replace('T',' '),
                    map.get("username").toString(),
                    map.get("status").toString(),
                    map.get("send_time").toString().replace('T',' '),
                    map.get("firm_time").toString().replace('T',' '),
                    map.get("address_id").toString(),
                    map.get("name").toString()));
        }
        //去重
        Set shopIdSet= new HashSet();
        List<Group> shopList=new ArrayList<>();
        for (Group element : allShop) {
            //set能添加进去就代表不是重复的元素
            if (shopIdSet.add(element.order_id)) shopList.add(element);
        }
        allShop.clear();

        for(int i=0;i<shopList.size();i++){
            List<Map<String,Object>> books=new ArrayList<>();
            Map<String,Object>shopMap=new HashMap<>();
            shopMap.put("order_id",shopList.get(i).order_id);
            shopMap.put("username",shopList.get(i).username);
            shopMap.put("shop_id",shopList.get(i).shop_id);
            shopMap.put("shop_name",shopList.get(i).shop_name);
            shopMap.put("create_time",shopList.get(i).create_time);
            shopMap.put("total",shopList.get(i).total);
            shopMap.put("status",shopList.get(i).status);
            shopMap.put("firm_time",shopList.get(i).firm_time);
            shopMap.put("send_time",shopList.get(i).send_time);
            shopMap.put("address_id",shopList.get(i).address_id);
            shopMap.put("name",shopList.get(i).name);

            for(int j=0;j<mapList.size();j++){
                //同一家店铺

                mapList.get(j).put("remark_time",mapList.get(j).get("remark_time").toString()
                        .replace('T',' '));
                mapList.get(j).put("return_time",mapList.get(j).get("return_time").toString()
                        .replace('T',' '));
                if(mapList.get(j).get("order_id").toString().equals(shopList.get(i).order_id)){
                    mapList.get(j).put("update_time",mapList.get(j).get("update_time").toString()
                            .replace('T',' '));

                    mapList.get(j).remove("create_time");
                    mapList.get(j).remove("total");
                    mapList.get(j).remove("shop_id");
                    mapList.get(j).remove("shop_name");
                    mapList.get(j).remove("username");
                    mapList.get(j).remove("status");
                    mapList.get(j).remove("firm_time");
                    mapList.get(j).remove("send_time");
                    mapList.get(j).remove("address_id");
                    mapList.get(j).remove("name");
                    books.add(mapList.get(j));
                }
            }
            shopMap.put("books",books);
            res.add(shopMap);
        }
        return res;

    }
}
