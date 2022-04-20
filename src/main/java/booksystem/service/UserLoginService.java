package booksystem.service;

public interface UserLoginService {
    int userLogin(String username,String password);
    int getIdentity(String username);
    int adminLogin(String username,String password);

}
