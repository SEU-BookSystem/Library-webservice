package booksystem.interceptor;


import booksystem.dao.AdminDao;
import booksystem.dao.UserDao;
import booksystem.utils.ResultEnum;
import booksystem.utils.LogUtils;
import booksystem.utils.TokenUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginFilter implements Filter{
    final String[] allowUrl={
            "login","registerUser","test","sendEmail","book","category","favicon.ico","alipay"
    };
    @Autowired
    UserDao userDao;
    @Autowired
    AdminDao adminDao;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Headers", "token");
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        //跨域允许的header

        System.out.println("\n--------------------------------欢迎访问“教我编程图书商城“------------------------------------");
        System.out.println(LogUtils.getNowTime()+"访问地址: "+request.getRequestURI());
        System.out.println(LogUtils.getNowTime()+"请求方式: "+request.getMethod());
        System.out.println(LogUtils.getNowTime()+"访问参数: "+ LogUtils.getParams(request));
        Map<String,Object> map = new HashMap<>();
        String url =  ((HttpServletRequest)servletRequest).getRequestURI();

        if(url != null){
            String[] str=url.split("/");
            //登录请求直接放行
            boolean isAllowUrl=false;
            for(int i=0;i<allowUrl.length;i++){
                if(str.length>=2){
                    if(allowUrl[i].equals(str[1]))
                        isAllowUrl=true;
                }
                if(str.length>=3){
                    if(str[2].equals("public"))
                        isAllowUrl=true;
                }

            }
            if(isAllowUrl){
                filterChain.doFilter(servletRequest,servletResponse);
                return;
            }else{
                //其他请求验证token
                String token = ((HttpServletRequest)servletRequest).getHeader("token");
                if(token!=null){
                    //token验证结果
                    boolean verify  = TokenUtils.verify(token);
                    if(!verify){
                        //验证失败
                        map.put("msg",ResultEnum.TOKEN_FAIL.getMsg());
                        map.put("code", ResultEnum.TOKEN_FAIL.getCode());
                    }else if(verify){
                        //token验证结果
                        int identity=Integer.parseInt(TokenUtils.parseToken(token).get("identity").toString());
                        boolean authority=true;
                        if (identity==0){
                            if(str[1].equals("shop")||str[1].equals("admin")){
                                map.put("msg",ResultEnum.AUTHORITY_FAIL.getMsg());
                                map.put("code",ResultEnum.AUTHORITY_FAIL.getCode());
                                authority=false;
                            }

                        }else if(identity==1){
                            if(str[1].equals("admin")){
                                map.put("msg",ResultEnum.AUTHORITY_FAIL.getMsg());
                                map.put("code",ResultEnum.AUTHORITY_FAIL.getCode());
                                authority=false;
                            }
                        }else if(identity==2){

                        }else {
                            authority=false;
                        }
                        //验证成功，且有权限
                        if(authority){
                            token=TokenUtils.refresh(token);
                            response.setHeader("token",token);
                            String username=TokenUtils.parseToken(token).get("username").toString();
                            System.out.println(LogUtils.getNowTime()+"访问用户: "+username);

                            filterChain.doFilter(servletRequest,servletResponse);
                            if(identity==0||identity==1){
                                userDao.accessTime(username);
                            }else if(identity==2){
                                adminDao.accessTime(username);
                            }
                            return;
                        }
                    }
                }else{
                    //token为空的返回
                    map.put("msg",ResultEnum.TOKEN_IS_NULL.getMsg());
                    map.put("code",ResultEnum.TOKEN_IS_NULL.getCode());
                }
            }
            JSONObject jsonObject = new JSONObject(map);
            servletResponse.setContentType("application/json");
            //设置响应的编码
            servletResponse.setCharacterEncoding("utf-8");
            //响应
            PrintWriter pw=servletResponse.getWriter();
            pw.write(jsonObject.toString());
            pw.flush();
            pw.close();
            System.out.println(LogUtils.getNowTime()+request.getRequestURI()+" 访问出错");
        }

        //执行
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
