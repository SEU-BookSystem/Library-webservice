package booksystem.pojo;

public class Book {
    String reference_num;//索引号
    String book_name;//书名
    String author;//作者
    String page_num;//页码
    String price;//价格
    String isbn;//isbn
    String detail;//详情
    String publisher;//出版社
    String image;//图片
    String date;//出版时间
    String category_id;//分类id
    int num;//库存
    String update_time;//更新时间

    public Book(){}

    public Book(String reference_num, String book_name, String author, String page_num, String price, String isbn, String detail, String publisher, String image, String date, String category_id, int num, String update_time) {
        this.reference_num = reference_num;
        this.book_name = book_name;
        this.author = author;
        this.page_num = page_num;
        this.price = price;
        this.isbn = isbn;
        this.detail = detail;
        this.publisher = publisher;
        this.image = image;
        this.date = date;
        this.category_id = category_id;
        this.num = num;
        this.update_time = update_time;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public void setReference_num(String reference_num) {
        this.reference_num = reference_num;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPage_num(String page_num) {
        this.page_num = page_num;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }


    public int getNum() {
        return num;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public String getReference_num() {
        return reference_num;
    }

    public String getBook_name() {
        return book_name;
    }

    public String getAuthor() {
        return author;
    }

    public String getPage_num() {
        return page_num;
    }

    public String getPrice() {
        return price;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getDetail() {
        return detail;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getImage() {
        return image;
    }

    public String getDate() {
        return date;
    }

    public String getCategory_id() {
        return category_id;
    }

    @Override
    public String toString() {
        return "Book{" +
                "reference_num='" + reference_num + '\'' +
                ", book_name='" + book_name + '\'' +
                ", author='" + author + '\'' +
                ", page_num='" + page_num + '\'' +
                ", price='" + price + '\'' +
                ", isbn='" + isbn + '\'' +
                ", detail='" + detail + '\'' +
                ", publisher='" + publisher + '\'' +
                ", image='" + image + '\'' +
                ", date='" + date + '\'' +
                ", category_id='" + category_id + '\'' +
                ", num=" + num +
                ", update_time='" + update_time + '\'' +
                '}';
    }
}