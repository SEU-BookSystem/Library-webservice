package booksystem.pojo;

public class Borrow {
    String lend_id;
    String username;
    String start_time;//借阅开始时间
    String end_time;//借阅结束时间
    int bar_code;//条码号
    int is_lend_again;//是否续借
    int is_borrow;//0预约 1借阅
    int status;//1.借阅未逾期 2.逾期 3.已还 4.预约成功  5.预约失败（包含取消预约等状态）
    String update_time;

    public Borrow() {
    }

    public Borrow(String lend_id, String user_id, String start_time, String end_time, int bar_code, int is_lend_again, int is_borrow, int status, String update_time) {
        this.lend_id = lend_id;
        this.username = user_id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.bar_code = bar_code;
        this.is_lend_again = is_lend_again;
        this.is_borrow = is_borrow;
        this.status = status;
        this.update_time = update_time;
    }

    public String getLend_id() {
        return lend_id;
    }

    public void setLend_id(String lend_id) {
        this.lend_id = lend_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getBar_code() {
        return bar_code;
    }

    public void setBar_code(int bar_code) {
        this.bar_code = bar_code;
    }

    public int getIs_lend_again() {
        return is_lend_again;
    }

    public void setIs_lend_again(int is_lend_again) {
        this.is_lend_again = is_lend_again;
    }

    public int getIs_borrow() {
        return is_borrow;
    }

    public void setIs_borrow(int is_borrow) {
        this.is_borrow = is_borrow;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    @Override
    public String toString() {
        return "Borrow{" +
                "lend_id='" + lend_id + '\'' +
                ", username='" + username + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", bar_code=" + bar_code +
                ", is_lend_again=" + is_lend_again +
                ", is_borrow=" + is_borrow +
                ", status=" + status +
                ", update_time='" + update_time + '\'' +
                '}';
    }
}
