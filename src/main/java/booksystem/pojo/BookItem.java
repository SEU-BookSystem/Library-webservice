package booksystem.pojo;

public class BookItem {

    int bar_code;
    String reference_num;
    int status;//1.在库 2.在馆 3.已借 4.预约
    String address;

    public BookItem() {
    }

    public BookItem(int bar_code, String reference_num, int status, String address) {
        this.bar_code = bar_code;
        this.reference_num = reference_num;
        this.status = status;
        this.address = address;
    }

    public int getBar_code() {
        return bar_code;
    }

    public String getReference_num() {
        return reference_num;
    }

    public int getStatus() {
        return status;
    }

    public String getAddress() {
        return address;
    }

    public void setBar_code(int bar_code) {
        this.bar_code = bar_code;
    }

    public void setReference_num(String reference_num) {
        this.reference_num = reference_num;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
