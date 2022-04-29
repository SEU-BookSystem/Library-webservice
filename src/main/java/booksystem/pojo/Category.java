package booksystem.pojo;

import java.util.List;

public class Category {
    String category_id;
    String category_name;
    String pid;
    int num;

    public  Category(){}

    public Category(String category_id, String category_name, String pid, int num) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.pid = pid;
        this.num = num;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Category{" +
                "category_id='" + category_id + '\'' +
                ", category_name='" + category_name + '\'' +
                ", pid='" + pid + '\'' +
                ", num='" + num + '\'' +
                '}';
    }
}
