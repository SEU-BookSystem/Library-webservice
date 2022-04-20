package booksystem.pojo;

public class Admin {
    String id;
    String username;
    String password;
    String avatar_b;
    String avatar_s;
    String name;
    String create_time;
    String update_time;
    String access_time;

    public Admin() {
    }

    public Admin(String id, String username, String password, String avatar_b, String avatar_s, String name, String create_time, String update_time, String access_time) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.avatar_b = avatar_b;
        this.avatar_s = avatar_s;
        this.name = name;
        this.create_time = create_time;
        this.update_time = update_time;
        this.access_time = access_time;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAvatar_b(String avatar_b) {
        this.avatar_b = avatar_b;
    }

    public void setAvatar_s(String avatar_s) {
        this.avatar_s = avatar_s;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public void setAccess_time(String access_time) {
        this.access_time = access_time;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAvatar_b() {
        return avatar_b;
    }

    public String getAvatar_s() {
        return avatar_s;
    }

    public String getName() {
        return name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public String getAccess_time() {
        return access_time;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", avatar_b='" + avatar_b + '\'' +
                ", avatar_s='" + avatar_s + '\'' +
                ", name='" + name + '\'' +
                ", create_time='" + create_time + '\'' +
                ", update_time='" + update_time + '\'' +
                ", access_time='" + access_time + '\'' +
                '}';
    }
}
