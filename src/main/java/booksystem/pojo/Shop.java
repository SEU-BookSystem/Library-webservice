package booksystem.pojo;

//   申请 审核 存在
//1.未审核1 无效-1 无效-1
//2.已审核2 通过1 未注销1
//3.已审核2 拒绝2 无效-1
//4.已审核2 通过1 注销2
//5.已取消3 无效-1 无效-1
//
public class Shop {
    private String id;
    private String user_id;//商家id
    private String username;
    private String avatar_b;
    private String avatar_s;
    private String shopper_name;//店主名
    private String shop_name;
    private double rate;//评分
    private int apply_status;//申请：-1.无效 1.已提交未审核 2.已审核 3.取消申请
    private String apply_reason;
    private int pass_status;//审核状态：-1.无效 1.通过 2.拒绝
    private String check_opinion;//审核意见
    private int exist_status;//店铺存在状态：-1.无效 1.未注销 2.已注销
    private String create_time;
    private String update_time;

    public Shop(){}

    public Shop(String id, String user_id, String username, String avatar_b, String avatar_s, String shopper_name, String shop_name, double rate, int apply_status, String apply_reason, int pass_status, String check_opinion, int exist_status, String create_time, String update_time) {
        this.id = id;
        this.user_id = user_id;
        this.username = username;
        this.avatar_b = avatar_b;
        this.avatar_s = avatar_s;
        this.shopper_name = shopper_name;
        this.shop_name = shop_name;
        this.rate = rate;
        this.apply_status = apply_status;
        this.apply_reason = apply_reason;
        this.pass_status = pass_status;
        this.check_opinion = check_opinion;
        this.exist_status = exist_status;
        this.create_time = create_time;
        this.update_time = update_time;
    }

    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getAvatar_b() {
        return avatar_b;
    }

    public String getAvatar_s() {
        return avatar_s;
    }

    public String getShopper_name() {
        return shopper_name;
    }

    public String getShop_name() {
        return shop_name;
    }

    public double getRate() {
        return rate;
    }

    public int getApply_status() {
        return apply_status;
    }

    public String getApply_reason() {
        return apply_reason;
    }

    public int getPass_status() {
        return pass_status;
    }

    public String getCheck_opinion() {
        return check_opinion;
    }

    public int getExist_status() {
        return exist_status;
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

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setAvatar_b(String avatar_b) {
        this.avatar_b = avatar_b;
    }

    public void setAvatar_s(String avatar_s) {
        this.avatar_s = avatar_s;
    }

    public void setShopper_name(String shopper_name) {
        this.shopper_name = shopper_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void setApply_status(int apply_status) {
        this.apply_status = apply_status;
    }

    public void setApply_reason(String apply_reason) {
        this.apply_reason = apply_reason;
    }

    public void setPass_status(int pass_status) {
        this.pass_status = pass_status;
    }

    public void setCheck_opinion(String check_opinion) {
        this.check_opinion = check_opinion;
    }

    public void setExist_status(int exist_status) {
        this.exist_status = exist_status;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
