package booksystem.pojo;

public class OrderBook {
    private String id;
    private String book_id;
    private String order_id;
    private int number;//图书数量
    private String remark;
    private double rate;
    private int remark_status;//0.未评价 1.已评价
    private int return_status;//退款状态：-1.无效 0.超过七天 1.申请退款未审核 2.同意 3.拒绝 4.换货申请未审核 5.换货同意 6.换货拒绝 7.退货退款申请未审核 8.退货退款通过 9.退货退款拒绝
    private String return_reason;//退货理由
    private String return_time;//退换货时间
    private String check_opinion;//审核意见
    private String remark_time;
    private double book_price;
    private String image_s;//退货凭证
    private String image_b;
    private String return_detail;
    private int transport_status;
    private String exchange_address_id;


    public OrderBook(){}

    public OrderBook(String id, String book_id, String order_id, int number, String remark, double rate, int remark_status, int return_status, String return_reason,  String check_opinion, String remark_time, double book_price, String image_s, String image_b,String return_time,String return_detail,int transport_status,String exchange_address_id) {
        this.id = id;
        this.book_id = book_id;
        this.order_id = order_id;
        this.number = number;
        this.remark = remark;
        this.rate = rate;
        this.remark_status = remark_status;
        this.return_status = return_status;
        this.return_reason = return_reason;
        this.return_time = return_time;
        this.check_opinion = check_opinion;
        this.remark_time = remark_time;
        this.book_price = book_price;
        this.image_s = image_s;
        this.image_b = image_b;
        this.return_detail=return_detail;
        this.transport_status=transport_status;
        this.exchange_address_id=exchange_address_id;
    }

    public String getReturn_time() {
        return return_time;
    }

    public void setReturn_time(String return_time) {
        this.return_time = return_time;
    }

    public String getImage_s() {
        return image_s;
    }

    public String getImage_b() {
        return image_b;
    }

    public int getRemark_status() {
        return remark_status;
    }

    public void setImage_s(String image_s) {
        this.image_s = image_s;
    }

    public void setImage_b(String image_b) {
        this.image_b = image_b;
    }

    public void setRemark_status(int remark_status) {
        this.remark_status = remark_status;
    }

    public double getBook_price() {
        return book_price;
    }

    public void setBook_price(double book_price) {
        this.book_price = book_price;
    }

    public String getRemark_time() {
        return remark_time;
    }

    public void setRemark_time(String remark_time) {
        this.remark_time = remark_time;
    }

    public double getRate() {
        return rate;
    }

    public int getReturn_status() {
        return return_status;
    }

    public String getReturn_reason() {
        return return_reason;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void setReturn_status(int return_status) {
        this.return_status = return_status;
    }

    public void setReturn_reason(String return_reason) {
        this.return_reason = return_reason;
    }

    public void setCheck_opinion(String check_opinion) {
        this.check_opinion = check_opinion;
    }

    public String getCheck_opinion() {
        return check_opinion;
    }

    public String getId() {
        return id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public int getNumber() {
        return number;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReturn_detail() {
        return return_detail;
    }

    public int getTransport_status() {
        return transport_status;
    }

    public String getExchange_address_id() {
        return exchange_address_id;
    }

    public void setReturn_detail(String return_detail) {
        this.return_detail = return_detail;
    }

    public void setTransport_status(int transport_status) {
        this.transport_status = transport_status;
    }

    public void setExchange_address_id(String exchange_address_id) {
        this.exchange_address_id = exchange_address_id;
    }
}
