package booksystem;

import booksystem.dao.DataDao;
import booksystem.pojo.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
class BooksystemApplicationTests {
    @Autowired
    DataDao dataDao;

    public String[] main={
            "2ee0b589ae4106ff7d6f9470e93a376b","青春文学",
            "6ccf8604ff7bf563caffc87ea1fb5051","小说",
            "7fe401d7c0f81306fe3f1f0ddc3b10ce","少儿文学",
            "974c4bc3021392a402b5fa104451de97","教育",
            "b7d0703c35fd0220d39c3dc8e3a8618b","网络文学",
            "db59caa5925e0e2840dc37ab4f08c6b7","文艺",
            "f9ca1a86674fc4d8c8ddaf81646d8bb8","其他"

    };

    public String[] second={
            "98ffbb4def4e74d180fe5446c73da665","童话",
            "8dee7af04d086aabd4c34d65aff0cddd","人文社科",
            "7e785167352c25447f1de155004da045","中国小说",
            "7cf38fa465c1f04af5b47d65b2eeed49","外国小说",
            "46203b7ad654419b21a041f64c2d38db","男频",
            "c9f5d3ac418721085f494cc40372862a","女频",
            "2d5e38d9b2b888add9c3c46f68196e9c","绘本",
            "2fa55b9fa7689eefca601865b100deda","文学",
            "e51b23c1dc2eca0e8bdcd0df0e0feff1","艺术",
            "38b934b242067f25c5f3ce038aea9387","教材",
            "0720939d1cb7a066e923e7d49d579e92","教辅资料",
            "e72b3caf24db4b01ad8fd2176bb61362","经管",
            "18a203dadb728d2b011b8b69c443e1e5","生活",
            "4c3ae1510cf4d4e6bd129d6bd0a5bf78","科技",
            "5c29911f2f23286c33282c6691d2cd9b","轻小说",
            "b68ad7910ae5186b4e7935a0e9cdc4d7","动漫",
            "7b1b12dd1e19375bf0b2cba43978212a","其他"
    };

    public String shop_id="15fde5133ce2450979487c9f274aee65";

    public static boolean isChineseChar(char c) {
        return String.valueOf(c).matches("[\u4e00-\u9fa5]");
    }
    public void addBook(String book_name,String author,double price,
                        int repertory,String press,String print_time,
                        String image_b,String image_s,String main_category_id,
                        String second_category_id,String shop_id,String detail){
        Book book=new Book();
        dataDao.addBook(book_name,author,price,0,repertory,press,"",print_time,image_b,image_s,main_category_id,second_category_id,shop_id,detail);
    }
    public static boolean isValid(String strLink) {
        URL url;
        try {
            url = new URL(strLink);
            HttpURLConnection connt = (HttpURLConnection)url.openConnection();
            connt.setRequestMethod("HEAD");
            String strMessage = connt.getResponseMessage();
            if (strMessage.compareTo("Not Found") == 0) {
                return false;
            }
            connt.disconnect();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Test
    void contextLoads() {
//        for(int tp=21;tp<21000;tp+=100){
//            System.out.println(tp);
//            List<Map<String,Object>>list=dataDao.getData(tp,100);
//            List<String>category=new ArrayList<>();
//            Set dateSet= new HashSet();
//            for(int i=0;i<list.size();i++){
//                String[] temp=list.get(i).get("info").toString().split(" ");
//                for (String item : temp) {
//                    if (dateSet.add(item)) {
//                        if (item.length()>10||item.length()<=1){
//                            continue;
//                        }
//                        boolean ppp=true;
//                        for(int j=0;j<item.length();j++){
//                            if(!isChineseChar(item.charAt(j))){
//                                ppp=false;
//                            }
//                        }
//                        if(ppp) {
//                            category.add(item);
//                        }
//
//                    }
//                }
//            }

//            Random random = new Random();
//
//            for(int i=0;i<list.size();i++){
//                double price=Double.parseDouble(list.get(i).get("price").toString());
//                if(price<20||price>80){
//                    price=random.nextInt(50)+25+Double.parseDouble(String.format("%.2f", random.nextDouble()));
//                }
//
//                if(!isValid(list.get(i).get("img").toString())){
//                    continue;
//                }
//                long print=Long.parseLong("1500000000000")+(long)(random.nextInt(   1273000))*100000;
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String date = format.format(print);
//
//                String main_id;
//                String second_id;
//                String info=list.get(i).get("info").toString();
//                if((info.contains("轻小说")||info.contains("奇幻")||info.contains("魔幻"))&&(info.contains("日本"))){
//                    main_id="2ee0b589ae4106ff7d6f9470e93a376b";
//                    second_id="5c29911f2f23286c33282c6691d2cd9b";
//                }else if(info.contains("童话")||info.contains("安徒生")||info.contains("格林")){
//                    main_id=main[4];
//                    second_id=second[0];
//                }else if(info.contains("网络小说")||info.contains("玄幻")||info.contains("男频")
//                        ||info.contains("都市")||info.contains("修仙")||info.contains("系统流")){
//                    main_id="b7d0703c35fd0220d39c3dc8e3a8618b";
//                    second_id="46203b7ad654419b21a041f64c2d38db";
//                }else if((info.contains("网络小说")||info.contains("女频")||info.contains("耽美")
//                        ||info.contains("穿越")||info.contains("修仙")||info.contains("系统"))&&info.contains("爱情")){
//                    main_id="b7d0703c35fd0220d39c3dc8e3a8618b";
//                    second_id="c9f5d3ac418721085f494cc40372862a";
//                }else if(info.contains("文学")&&(info.contains("爱")||info.contains("美")
//                        ||info.contains("情"))){
//                    main_id="db59caa5925e0e2840dc37ab4f08c6b7";
//                    second_id="2fa55b9fa7689eefca601865b100deda";
//                }else if(info.contains("教材")||info.contains("学习")||info.contains("课本")){
//                    main_id=main[6];
//                    second_id=second[18];
//                }else if(info.contains("教辅")||info.contains("资料")||info.contains("大学")||info.contains("高中")
//                        ||info.contains("初中")||info.contains("小学")){
//                    main_id=main[6];
//                    second_id="0720939d1cb7a066e923e7d49d579e92";
//                }
//                else if(info.contains("生活")||info.contains("生产")||info.contains("美食")){
//                    main_id="f9ca1a86674fc4d8c8ddaf81646d8bb8";
//                    second_id="18a203dadb728d2b011b8b69c443e1e5";
//                }else if(info.contains("科技")||info.contains("科学")){
//                    main_id="f9ca1a86674fc4d8c8ddaf81646d8bb8";
//                    second_id="4c3ae1510cf4d4e6bd129d6bd0a5bf78";
//                }else if(info.contains("经济")||info.contains("金融")||info.contains("管理")){
//                    main_id="f9ca1a86674fc4d8c8ddaf81646d8bb8";
//                    second_id="e72b3caf24db4b01ad8fd2176bb61362";
//                }else if(info.contains("人文")||info.contains("社会")||info.contains("文化")||info.contains("历史")
//                        ||info.contains("古代")||info.contains("儒家")){
//                    main_id="f9ca1a86674fc4d8c8ddaf81646d8bb8";
//                    second_id="8dee7af04d086aabd4c34d65aff0cddd";
//                }else if(info.contains("艺术")||info.contains("美术")||info.contains("音乐")){
//                    main_id="db59caa5925e0e2840dc37ab4f08c6b7";
//                    second_id="e51b23c1dc2eca0e8bdcd0df0e0feff1";
//                }else if(info.contains("漫画")||info.contains("动漫")){
//                    main_id=main[0];
//                    second_id="b68ad7910ae5186b4e7935a0e9cdc4d7";
//                }else if(info.contains("绘本")||info.contains("绘画")||info.contains("画画")
//                        ||info.contains("绘")){
//                    main_id=main[4];
//                    second_id="2d5e38d9b2b888add9c3c46f68196e9c";
//                }else if((info.contains("小说")||info.contains("奇幻")||info.contains("魔幻")||info.contains("文学"))
//                        &&(info.contains("英")||info.contains("外国")||info.contains("美国")||info.contains("外文")
//                        ||info.contains("日本"))){
//                    main_id=main[2];
//                    second_id=second[6];
//                }else if((info.contains("小说")||info.contains("奇幻")||info.contains("魔幻"))
//                        &&!(info.contains("英")||info.contains("外国")||info.contains("美国")||info.contains("外文"))
//                        &&info.contains("中国")){
//                    main_id=main[2];
//                    second_id=second[4];
//                }else{
//                    main_id="f9ca1a86674fc4d8c8ddaf81646d8bb8";
//                    second_id="7b1b12dd1e19375bf0b2cba43978212a";
//                }
//
//
//                addBook(list.get(i).get("book_name").toString(),
//                        list.get(i).get("author").toString(),
//                        price,
//                        random.nextInt(1000)+50,
//                        list.get(i).get("press").toString(),
//                        date,
//                        list.get(i).get("img").toString(),
//                        list.get(i).get("img").toString(),
//                        main_id,
//                        second_id,
//                        shop_id,
//                        list.get(i).get("detail").toString()
//                                .replace("在线阅读本书","")
//                                .replace("\n","")
//                );
//            }

//            for(int i=0;i<category.size();i++){
//                System.out.print(category.get(i)+"      ");
//                if(i%10==0){
//                    System.out.println();
//                }
//            }

//            System.out.println("\n"+category.size());
//        }


    }

}
