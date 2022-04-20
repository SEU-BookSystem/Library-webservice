package booksystem.service.Impl;

import booksystem.dao.UploadImgDao;
import booksystem.pojo.FtpServer;
import booksystem.service.UploadImgService;


import java.io.*;

import booksystem.utils.ImgUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadImgServiceImpl implements UploadImgService {

    @Autowired
    UploadImgDao uploadImgDao;

    //上传原图
    public String uploadOriginalImg(File imgFile,String fileUrl_b){
        fileUrl_b=fileUrl_b.replace("http://47.94.131.208:8888/img","");

        //先删除原来的图片
        if(!(fileUrl_b.isEmpty()||fileUrl_b.equals("/original/avatar.jpg"))) {
            ImgUtils.deleteImg(FtpServer.imgUrl + fileUrl_b);
        }

        fileUrl_b=ImgUtils.uploadImg(imgFile,"/original");        //上传原图

        return fileUrl_b;
    }

    //上传压缩图
    public String uploadCompressImg(File imgFile,String fileUrl_s){
        fileUrl_s=fileUrl_s.replace("http://47.94.131.208:8888/img","");

        //先删除原来的图片
        if(!(fileUrl_s.isEmpty()||fileUrl_s.equals("/compression/avatar.jpg"))) {
            ImgUtils.deleteImg(FtpServer.imgUrl + fileUrl_s);
        }

        String prefix=imgFile.getName().substring(imgFile.getName().lastIndexOf("."));
        File compressedFile= ImgUtils.compressPicture(imgFile,0.1,prefix);
        fileUrl_s=ImgUtils.uploadImg(compressedFile,"/compression");        //上传压缩图

        return fileUrl_s;
    }

    @Override
    public String uploadUserImg(MultipartFile img,String username) {
        String fileUrl_b=uploadImgDao.getUserImgUrl(username).get("avatar_b").toString();
        String fileUrl_s=uploadImgDao.getUserImgUrl(username).get("avatar_s").toString();

        File imgFile=ImgUtils.multipartFileToFile(img);

        fileUrl_b=uploadOriginalImg(imgFile,fileUrl_b);
        fileUrl_s=uploadCompressImg(imgFile,fileUrl_s);

        //上传到数据库
        uploadImgDao.updateUserImgUrl(username,fileUrl_b,fileUrl_s);

        return fileUrl_b;
    }

    @Override
    public String uploadShopImg(MultipartFile img, String shop_id) {
        String fileUrl_b=uploadImgDao.getShopImgUrlById(shop_id).get("avatar_b").toString();
        String fileUrl_s=uploadImgDao.getShopImgUrlById(shop_id).get("avatar_s").toString();

        File imgFile=ImgUtils.multipartFileToFile(img);

        fileUrl_b=uploadOriginalImg(imgFile,fileUrl_b);
        fileUrl_s=uploadCompressImg(imgFile,fileUrl_s);

        //上传到数据库
        uploadImgDao.updateShopImgUrl(shop_id,fileUrl_b,fileUrl_s);

        return fileUrl_b;
    }

    @Override
    public String uploadBookImg(MultipartFile img, String book_id) {
        String fileUrl_b=uploadImgDao.getBookImgUrlById(book_id).get("image_b").toString();
        String fileUrl_s=uploadImgDao.getBookImgUrlById(book_id).get("image_s").toString();

        File imgFile=ImgUtils.multipartFileToFile(img);

        fileUrl_b=uploadOriginalImg(imgFile,fileUrl_b);
        fileUrl_s=uploadCompressImg(imgFile,fileUrl_s);

        //上传到数据库
        uploadImgDao.updateBookImgUrl(book_id,fileUrl_b,fileUrl_s);

        return fileUrl_b;
    }

    @Override
    public String uploadReturnImg(MultipartFile img, String order_book_id) {
        String fileUrl_b = uploadImgDao.getReturnImgUrlById(order_book_id).get("image_b").toString();
        String fileUrl_s= uploadImgDao.getReturnImgUrlById(order_book_id).get("image_s").toString();

        File imgFile=ImgUtils.multipartFileToFile(img);

        fileUrl_b=uploadOriginalImg(imgFile,fileUrl_b);
        fileUrl_s=uploadCompressImg(imgFile,fileUrl_s);

        //上传到数据库
        uploadImgDao.updateReturnImgUrl(order_book_id,fileUrl_b,fileUrl_s);

        return fileUrl_b;
    }

    public void deleteImg(String fileUrl_b,String fileUrl_s){
        fileUrl_s=fileUrl_s.replace("http://47.94.131.208:8888/img","");
        fileUrl_b=fileUrl_b.replace("http://47.94.131.208:8888/img","");

        //删除图片
        if(!(fileUrl_b.isEmpty()||fileUrl_b.equals("/original/avatar.jpg"))) {
            ImgUtils.deleteImg(FtpServer.imgUrl + fileUrl_b);
        }
        if(!(fileUrl_s.isEmpty()||fileUrl_s.equals("/compression/avatar.jpg"))) {
            ImgUtils.deleteImg(FtpServer.imgUrl + fileUrl_s);
        }
    }

    @Override
    public void deleteUserImg(String user_id) {
        String fileUrl_b=uploadImgDao.getUserImgUrlById(user_id).get("avatar_b").toString();
        String fileUrl_s=uploadImgDao.getUserImgUrlById(user_id).get("avatar_s").toString();
        deleteImg(fileUrl_b,fileUrl_s);
    }

    @Override
    public void deleteShopImg(String shop_id) {
        String fileUrl_b=uploadImgDao.getShopImgUrlById(shop_id).get("avatar_b").toString();
        String fileUrl_s=uploadImgDao.getShopImgUrlById(shop_id).get("avatar_s").toString();
        deleteImg(fileUrl_b,fileUrl_s);
    }

    @Override
    public void deleteBookImg(String book_id) {
        String fileUrl_b=uploadImgDao.getBookImgUrlById(book_id).get("image_b").toString();
        String fileUrl_s=uploadImgDao.getBookImgUrlById(book_id).get("image_s").toString();
        deleteImg(fileUrl_b,fileUrl_s);
    }

    @Override
    public void deleteReturnImg(String order_book_id) {
        String fileUrl_b=uploadImgDao.getReturnImgUrlById(order_book_id).get("image_b").toString();
        String fileUrl_s=uploadImgDao.getReturnImgUrlById(order_book_id).get("image_s").toString();
        deleteImg(fileUrl_b,fileUrl_s);
    }

}
