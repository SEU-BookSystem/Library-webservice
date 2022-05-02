package booksystem.utils;

//import booksystem.dao.BookDao;
//import booksystem.pojo.Book;
//import booksystem.pojo.Category;
//import booksystem.service.BookItemService;
//import booksystem.service.CategoryService;
//import ch.qos.logback.classic.Level;
//import ch.qos.logback.classic.Logger;
//import ch.qos.logback.classic.LoggerContext;
//import com.gargoylesoftware.htmlunit.BrowserVersion;
//import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
//import com.gargoylesoftware.htmlunit.WebClient;
//import com.gargoylesoftware.htmlunit.html.HtmlPage;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.junit.Test;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.*;
//
//@CrossOrigin(origins="*",maxAge = 3600)
//@RestController
public class CategorySpiderUtils {
//    @Autowired
//    CategoryService categoryService;
//
//    static {
//        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
//        List<Logger> loggerList = loggerContext.getLoggerList();
//        loggerList.forEach(logger -> {
//            logger.setLevel(Level.OFF);
//        });
//    }
//
//    public Document getData(String url) {
//        final WebClient webClient = new WebClient(BrowserVersion.CHROME);//新建一个模拟谷歌Chrome浏览器的浏览器客户端对象
//
//        webClient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常, 这里选择不需要
//        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
//        webClient.getOptions().setActiveXNative(false);
//        webClient.getOptions().setCssEnabled(false);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
//        webClient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
//        webClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX
//        webClient.getOptions().setTimeout(5000);
//
//        HtmlPage page = null;
//        try {
//            page = webClient.getPage(url);//尝试加载上面图片例子给出的网页
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            webClient.close();
//        }
//
//        webClient.waitForBackgroundJavaScript(60000);//异步JS执行需要耗时,所以这里线程要阻塞30秒,等待异步JS执行结束
//
//        String pageXml = page.asXml();//直接将加载完成的页面转换成xml格式的字符串
//        Document document = Jsoup.parse(pageXml);//获取html文档
//        return document;
//    }
//
//
//    @PostMapping("/spider/category")
////    @Test
//    public void test()
//    {
//        for (char i = 'L'; i <= 'Z'; i++) {
//            //爬取网页
//            Document document = getData("https://www.clcindex.com/category/" +String.valueOf(i));
//            //逐一解析数据
//            Elements es = document.select("tr[name='item-row']");//获取相关节点元素
//
//            ArrayList<Category> datalist=new ArrayList<>();
//
//            for(Element e:es) {
//                String strings[] = e.text().split(" ");
//                String category_id = strings[1].replace("[", "").substring(0, 2);
//                String category_name = strings[2];
//                Category category= new Category();
//                category.setCategory_id(category_id);
//                category.setCategory_name(category_name);
//                category.setPid(String.valueOf(i));
//                category.setNum(0);
//                datalist.add(category);
//                System.out.println(category);
//            }
//
//            for(int j=0;j<datalist.size();j++)
//            {
//                for(int k=j+1;k<datalist.size();k++)
//                {
//                    if(datalist.get(k).getCategory_id().equals(datalist.get(j).getCategory_id()))
//                    {
//                        datalist.get(j).setCategory_name(datalist.get(j).getCategory_name()+","+datalist.get(k).getCategory_name());
//                        datalist.remove(k);
//                        k--;
//                    }
//                }
//            }
//
//            for (Category data:datalist)
//            {
//                System.out.println(data);
//                categoryService.addCategory(data);
//            }
//
//            int size=datalist.size();
//            System.out.println(String.valueOf(i)+size);
//        }
//    }
}

