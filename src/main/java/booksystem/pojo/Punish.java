package booksystem.pojo;

public class Punish {
    String punish_id;
    String username;
    int bar_code;
    String update_time;
    int status;//1.逾期 2.书籍损坏 3.书籍丢失
    int is_handle;
    String detail;

    public Punish() {
    }

    public Punish(String punish_id, String username, int bar_code, String update_time, int status, int is_handle, String detail) {
        this.punish_id = punish_id;
        this.username = username;
        this.bar_code = bar_code;
        this.update_time = update_time;
        this.status = status;
        this.is_handle = is_handle;
        this.detail = detail;
    }

    public String getPunish_id() {
        return punish_id;
    }

    public void setPunish_id(String punish_id) {
        this.punish_id = punish_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getBar_code() {
        return bar_code;
    }

    public void setBar_code(int bar_code) {
        this.bar_code = bar_code;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIs_handle() {
        return is_handle;
    }

    public void setIs_handle(int is_handle) {
        this.is_handle = is_handle;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "Punish{" +
                "punish_id='" + punish_id + '\'' +
                ", username='" + username + '\'' +
                ", bar_code=" + bar_code +
                ", update_time='" + update_time + '\'' +
                ", status=" + status +
                ", is_handle=" + is_handle +
                ", detail='" + detail + '\'' +
                '}';
    }
}