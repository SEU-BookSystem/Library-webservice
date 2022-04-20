package booksystem.pojo;

public class CartItem {
    private String id;
    private int sum;//数量
    private String book_id;
    private String user_id;
    private String create_time;
    private String update_time;

    public CartItem(){}

    public CartItem(String id, int sum, String book_id, String user_id, String create_time, String update_time) {
        this.id = id;
        this.sum = sum;
        this.book_id = book_id;
        this.user_id = user_id;
        this.create_time = create_time;
        this.update_time = update_time;
    }

    public String getId() {
        return id;
    }

    public int getSum() {
        return sum;
    }

    public String getBook_id() {
        return book_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
