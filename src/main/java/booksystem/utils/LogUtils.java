package booksystem.utils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class LogUtils {
    public static String getNowTime(){
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z  ");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    public static Map<String,Object> getParams(HttpServletRequest request){
        Map<String,Object>map= new HashMap<>();
        Enumeration<String> params = request.getParameterNames();
        for (Enumeration<String> e = params; e.hasMoreElements();) {
            String key = e.nextElement();
            map.put(key, request.getParameter(key));
        }
        return map;
    }

//    public static void main(String[] args) {
//        String st="S1-100234";
//        String p="T2";
//        st=p+st.substring(2);
//        System.out.println(st);
//    }

}
