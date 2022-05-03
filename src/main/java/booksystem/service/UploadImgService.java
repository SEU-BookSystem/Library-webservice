package booksystem.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadImgService {
    String uploadUserImg(MultipartFile img,String username);
//    String uploadShopImg(MultipartFile img,String shop_id);
//    String uploadBookImg(MultipartFile img,String book_id);
//    String uploadReturnImg(MultipartFile img,String order_book_id);
//    void deleteUserImg(String user_id);
//    void deleteShopImg(String shop_id);
//    void deleteBookImg(String book_id);
//    void deleteReturnImg(String order_book_id);
}
