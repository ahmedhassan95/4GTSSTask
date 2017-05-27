package hk.com.a4gtsstask;

import java.io.Serializable;

/**
 * Created by ahmed on 26 May 2017.
 */

public class Note implements Serializable {

    private String title;
    private String body;
    private int id;

    public Note() {
    }

    public Note( int id,String title, String body) {
        this.title = title;
        this.body = body;
        this.id = id;
    }

    public Note(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
