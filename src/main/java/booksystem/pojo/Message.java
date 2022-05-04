package booksystem.pojo;

public class Message {
    String message_id;
    String username;
    String theme;
    String content;
    String send_time;
    int is_read;

    public Message() {
    }

    public Message(String message_id, String username, String theme, String content, String send_time, int is_read) {
        this.message_id = message_id;
        this.username = username;
        this.theme = theme;
        this.content = content;
        this.send_time = send_time;
        this.is_read = is_read;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message_id='" + message_id + '\'' +
                ", username='" + username + '\'' +
                ", theme='" + theme + '\'' +
                ", content='" + content + '\'' +
                ", send_time='" + send_time + '\'' +
                ", is_read=" + is_read +
                '}';
    }
}
