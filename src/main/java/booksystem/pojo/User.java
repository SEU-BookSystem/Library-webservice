package booksystem.pojo;

public class User {
    String username;
    String password;
    String name;//昵称
    String create_time;//注册时间
    String update_time;//注册时间
    String access_time;
    String id_card;
    int status;
    int age;
    String gender;

    public User(){}

    public User(String username, String password, String name, String create_time, String update_time, String access_time, String id_card, int status, int age, String gender) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.create_time = create_time;
        this.update_time = update_time;
        this.access_time = access_time;
        this.id_card = id_card;
        this.status = status;
        this.age = age;
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getAccess_time() {
        return access_time;
    }

    public void setAccess_time(String access_time) {
        this.access_time = access_time;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", create_time='" + create_time + '\'' +
                ", update_time='" + update_time + '\'' +
                ", access_time='" + access_time + '\'' +
                ", id_card='" + id_card + '\'' +
                ", status=" + status +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                '}';
    }
}
