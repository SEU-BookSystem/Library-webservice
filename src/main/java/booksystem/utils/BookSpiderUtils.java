package booksystem.utils;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import ch.qos.logback.classic.Level;
import booksystem.dao.BookDao;
import org.springframework.beans.factory.annotation.Autowired;
import booksystem.pojo.Book;


public class BookSpiderUtils {
    @Autowired
    BookDao bookDao;

    public String getReference_num(ArrayList<String> data) {
        for (String str : data)
            if (str.contains("中图分类法"))
                return str.split(" ")[2];
        return null;
    }

    public String getIsbn(ArrayList<String> data) {
        for (String str : data)
            if (str.contains("ISBN")) {
                String array[] = str.replace("-", "").split(" ");
                if (array[1].contains("价格"))
                    return null;
                else return array[1];
            }
        return null;
    }

    public String getBook_name(String str) {
        return str;
    }

    public String getPage_num(String str) {
        return str.split(" ")[1];
    }

    public String getPrice(String str) {
        String array[] = str.split(" ");
        return array[array.length - 1];
    }

    //根据ISBN从豆瓣获取图片、书籍简介、出版社、出版年、作者
    public String[] ask(String isbn) {
        String image = "";
        String detail = "";
        String date = "";
        String publisher = "";
        String author = "";

        String baseurl = "https://book.douban.com/isbn/" + isbn + '/';
        Document document = getData(baseurl);

        //逐一解析数据
        Elements es = document.select("div.article");//获取相关节点元素
        for (Element e : es) {
            image = e.select("div[id='mainpic']>a").attr("href");
            detail = e.select("div[class=intro]>p").text();

            String strings[] = e.select("div[id='info']").text().split(" ");
            for (int i = 0; i < strings.length; i++) {
//                System.out.println(strings[i]);
                if (strings[i].equals("出版社:"))
                    publisher = strings[i + 1];
                if (strings[i].equals("出版年:"))
                    date = strings[i + 1].split("-")[0];
                if (strings[i].contains("作者"))
                    author = strings[i + 1];
            }
        }
        return new String[]{image, detail, publisher, date, author};
    }

    static {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        List<Logger> loggerList = loggerContext.getLoggerList();
        loggerList.forEach(logger -> {
            logger.setLevel(Level.OFF);
        });
    }

    public Document getData(String url) {
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);//新建一个模拟谷歌Chrome浏览器的浏览器客户端对象

        webClient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常, 这里选择不需要
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
        webClient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX
        webClient.getOptions().setTimeout(5000);

        HtmlPage page = null;
        try {
            page = webClient.getPage(url);//尝试加载上面图片例子给出的网页
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            webClient.close();
        }

        webClient.waitForBackgroundJavaScript(3000);//异步JS执行需要耗时,所以这里线程要阻塞30秒,等待异步JS执行结束

        String pageXml = page.asXml();//直接将加载完成的页面转换成xml格式的字符串
        Document document = Jsoup.parse(pageXml);//获取html文档
        return document;
    }


    @Test
    public void test() {
        String id = "2004198930";
        int count = 56;

        for (int i = 1056; i < 1200; i++) {
            ArrayList<String> data = new ArrayList<String>();
            //爬取网页
            Document document = getData("http://my1.hzlib.net/opac/book/" + String.valueOf(i));
            System.out.println(String.valueOf(count) + "-------------------------");
            count = count + 1;
            //逐一解析数据
            Elements es = document.select("tr[data-sort]");//获取相关节点元素
//            System.out.println(es.text());
            if (es.size() > 9) {
                for (Element e : es) {
                    data.add(e.text());
                }

                Book book = new Book();
                if (getIsbn(data) == null)
                    continue;

                book.setBook_name(getBook_name(data.get(0)));
                book.setIsbn(getIsbn(data));
                book.setPage_num(getPage_num(data.get(4)));
                book.setPrice(getPrice(data.get(2)));
                book.setReference_num(getReference_num(data));
//                book.setPublisher(getPublisher(data.get(5)));
//                book.setAuthor(getAuthor(data.get(1)));
//                book.setDate(getDate(data.get(5)));

                String array[] = ask(book.getIsbn());
                if (array[0].equals("") || !array[0].contains(".jpg"))
                    continue;
                book.setImage(array[0]);
                book.setDetail(array[1]);
                book.setPublisher(array[2]);
                book.setDate(array[3]);
                book.setAuthor(array[4]);
                book.setCategory_id(getReference_num(data).substring(0, 2));
//                bookDao.addBook(book);
                System.out.println(book);
            }
        }
    }
}
