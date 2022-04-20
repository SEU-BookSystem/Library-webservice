package booksystem.pojo;

import java.util.List;

public class Category {
    private String id;
    private String category_name;
    private String pid;
    private boolean status;
    private String create_time;
    private String update_time;
    private List<Category> children;


    public Category(){}

    public Category(String id, String category_name, String pid, boolean status, String create_time, String update_time, List<Category> children) {
        this.id = id;
        this.category_name = category_name;
        this.pid = pid;
        this.status = status;
        this.create_time = create_time;
        this.update_time = update_time;
        this.children = children;
    }

    public String getId() {
        return id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getPid() {
        return pid;
    }

    public boolean isStatus() {
        return status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }
}
