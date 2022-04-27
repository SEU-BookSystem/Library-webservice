package spider;


import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.ArrayList;


import java.util.Vector;

import javax.swing.JOptionPane;


/**
 * Created by xuyh at 2017/11/6 14:03.
 */
public class book_spider {
    public String getReference_num(ArrayList<String> data) {
        for(String str:data)
            if(str.contains("中图分类法"))
                return str.split(" ")[2];
        return null;
    }

    public String getBook_name(String str) {
        return str;
    }

    public String getAuthor(String str) {
        return str.split(" ")[3];
    }

    public String getPage_num(String str) {
        return str.split(" ")[1];
    }

    public String getPrice(String str) {
        String array[]=str.split(" ");
        return array[array.length-1];
    }

    public String getIsbn(String str) {
        String array[]=str.split(" ");
        if(array[1].contains("价格"))
            return " ";
        else return array[1];
    }

    public String getDetail(String str) {
        return str.split(" ")[3];
    }

    public String getPublisher(String str) {
        return str.split(" ")[3];
    }

    public String getImage(String str) {
        return str.split(" ")[3];
    }

    public String getDate(String str) {
        return str.split(" ")[5];
    }

    public String getCategory_id(String str) {
        return str.split(" ")[3];
    }

    //根据ISBN从豆瓣获取图片和书籍简介
    public String[] ask(String isbn)
    {
        ArrayList<String> array=new ArrayList<String>();
        String image="";
        String detail="";

        String baseurl="https://book.douban.com/isbn/"+isbn+'/';
        Document document=getData(baseurl);

        //逐一解析数据
        Elements es =document.select("div.article");//获取相关节点元素
        for(Element e:es) {
            image=e.select("div[id='mainpic']>a").attr("href");
            detail=e.select("div[class=intro]>p").text();
        }
        return new String[]{image,detail};
    }


    public Document getData(String url)
    {
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);//新建一个模拟谷歌Chrome浏览器的浏览器客户端对象

        webClient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常, 这里选择不需要
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
        webClient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX

        HtmlPage page = null;
        try {
            page = webClient.getPage(url);//尝试加载上面图片例子给出的网页
        } catch (Exception e) {
            //e.printStackTrace();
        }finally {
            webClient.close();
        }

        webClient.waitForBackgroundJavaScript(30000);//异步JS执行需要耗时,所以这里线程要阻塞30秒,等待异步JS执行结束

        String pageXml = page.asXml();//直接将加载完成的页面转换成xml格式的字符串
        Document document = Jsoup.parse(pageXml);//获取html文档
        return document;
    }


    @Test
    public void test() {

        ArrayList<Book> booklist = new ArrayList<>();
        String id = "2004198930";

        for (int i = 1000; i < 1010; i++) {
            ArrayList<String> data = new ArrayList<String>();
            //爬取网页
            Document document = getData("http://my1.hzlib.net/opac/book/" + String.valueOf(i));
            System.out.println("-------------------------");
            //逐一解析数据
            Elements es = document.select("tr[data-sort]");//获取相关节点元素
            System.out.println(es.text());
            if (es.size() > 9) {
                for (Element e : es) {
                    data.add(e.text().replace("-", ""));
                }

                Book book = new Book();
                book.setBook_name(getBook_name(data.get(0)));
                book.setAuthor(getAuthor(data.get(1)));
                book.setDate(getDate(data.get(5)));
                book.setIsbn(getIsbn(data.get(2)));
                if (getIsbn(data.get(2)).equals(" "))
                    continue;

                book.setPage_num(getPage_num(data.get(4)));
                book.setPrice(getPrice(data.get(2)));
                book.setReference_num(getReference_num(data));
                book.setPublisher(getPublisher(data.get(5)));

                String array[] = ask(book.getIsbn());
                book.setImage(array[0]);
                book.setDetail(array[1]);
                booklist.add(book);
                System.out.println(book);
            }
        }
    }
//        return booklist;
}
