package hk.com.a4gtsstask;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Note implements Serializable {

    @SerializedName("title")
    private String title;
    @SerializedName("id")
    private String id;
    @SerializedName("completed")
    private boolean completed;

    public Note() {
    }

    public Note( String id,String title, boolean completed) {
        this.title = title;
        this.completed = completed;
        this.id = id;
    }

    public Note(String title, boolean completed) {
        this.title = title;
        this.completed= completed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
