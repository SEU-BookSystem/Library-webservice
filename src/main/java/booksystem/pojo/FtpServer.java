package booksystem.pojo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "remote")//一键配置
public class FtpServer {
//    public final static String User="Destiny";
//    public final static String Password="Destiny100419544";
//    public final static String imgUrl="/usr/local/tomcat/img";
//    public final static String hostname="47.94.131.208";
//    public final static String accessUrl="http://47.94.131.208:8888/img";

    public final static String User="root";
    public final static String Password="#Aly100419544#";
    public final static String imgUrl="/usr/local/tomcat/img/library";
    public final static String hostname="47.100.78.158";
    public final static String accessUrl="http://47.100.78.158:8080/img/library";
    public static final int REMOTE_PORT = 22;   //ssh协议默认端口
    public static final int SESSION_TIMEOUT = 10000; //session超时时间
    public static final int CHANNEL_TIMEOUT = 5000; //管道流超时时间
}
