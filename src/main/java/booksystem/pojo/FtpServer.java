package booksystem.pojo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "remote")//一键配置
public class FtpServer {
    public final static String User="Destiny";
    public final static String Password="Destiny100419544";
    public final static String imgUrl="/usr/local/tomcat/img";
    public final static String hostname="47.94.131.208";
    public final static String accessUrl="http://47.94.131.208:8888/img";
}
