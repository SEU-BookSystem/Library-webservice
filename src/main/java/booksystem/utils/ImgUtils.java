package booksystem.utils;

import booksystem.pojo.FtpServer;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTP;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Locale;
import java.util.UUID;

public class ImgUtils {

    public static File compressPicture(
            File imgFile, Double quality,String prefix)
    {
        byte[] imageBytes=fileToByte(imgFile);
        if (imageBytes == null || imageBytes.length <= 0) {
            return byteToFile(imageBytes,prefix);
        }
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            ByteArrayOutputStream outputStream= new ByteArrayOutputStream(imageBytes.length);
            Thumbnails.of(inputStream).scale(1f).outputQuality(quality).toOutputStream(outputStream);
            imageBytes = outputStream.toByteArray();
            //" 图片原大小={}kb | 压缩后大小={}kb",
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteToFile(imageBytes,prefix);
    }

    public static byte[] fileToByte(File file) {
        byte[] data = null;

        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int len;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }

            data = baos.toByteArray();

            fis.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public static File byteToFile(byte[] buf, String prefix){
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file=null;
        try{

            file = File.createTempFile(String.valueOf(UUID.randomUUID()).
                    replace("-","").toUpperCase(Locale.ROOT), prefix);

            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally{
            if (bos != null){
                try{
                    bos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if (fos != null){
                try{
                    fos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    public static File multipartFileToFile(MultipartFile multipartFile){
        File file=null;
        try{
            String fileName = multipartFile.getOriginalFilename();//原文件名
            String prefix=fileName.substring(fileName.lastIndexOf("."));// 获取文件后缀
            file = File.createTempFile(String.valueOf(UUID.randomUUID()).
                    replace("-","").toUpperCase(Locale.ROOT), prefix);

            multipartFile.transferTo(file);
        }catch (IOException e){
            e.printStackTrace();
        }
        return file;
    }
    public static void deleteImg(String url){
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(FtpServer.hostname, 21);// 创建ftp连接，默认21端口
            ftpClient.login(FtpServer.User, FtpServer.Password);
            ftpClient.deleteFile(url);

            ftpClient.logout();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static String uploadImg(File imgFile,String url){
        String fileUrl="";
        try {

            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(FtpServer.hostname, 21);// 创建ftp连接，默认21端口
            ftpClient.login(FtpServer.User, FtpServer.Password);

            //开始传图片
            FileInputStream inputStream = new FileInputStream(imgFile);
            ftpClient.enterLocalPassiveMode();
            ftpClient.changeWorkingDirectory(FtpServer.imgUrl+url);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.storeFile(imgFile.getName(), inputStream);
            fileUrl=FtpServer.accessUrl+url+"/"+imgFile.getName();//地址

            ftpClient.logout();
        }catch (IOException e){
            e.printStackTrace();
        }
        return fileUrl;
    }

    public static void main(String[] args) {
        //测试用
        try {

            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(FtpServer.hostname, 21);// 创建ftp连接，默认21端口
            ftpClient.login(FtpServer.User, FtpServer.Password);

            ftpClient.deleteFile("/usr/local/tomcat/img/sb.jpg");

            ftpClient.logout();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
