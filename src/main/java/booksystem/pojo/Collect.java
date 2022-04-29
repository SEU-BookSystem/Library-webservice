package booksystem.pojo;

public class Collect {
    String collection_id;
    String reference_num;
    String user_id;
    String create_time;

    public Collect(String collection_id, String reference_num, String user_id, String create_time) {
        this.collection_id = collection_id;
        this.reference_num = reference_num;
        this.user_id = user_id;
        this.create_time = create_time;
    }

    public String getCollection_id() {
        return collection_id;
    }

    public String getReference_num() {
        return reference_num;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCollection_id(String collection_id) {
        this.collection_id = collection_id;
    }

    public void setReference_num(String reference_num) {
        this.reference_num = reference_num;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "collection_id='" + collection_id + '\'' +
                ", reference_num='" + reference_num + '\'' +
                ", user_id='" + user_id + '\'' +
                ", create_time='" + create_time + '\'' +
                '}';
    }
}
