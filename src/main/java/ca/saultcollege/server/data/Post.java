package ca.saultcollege.server.data;

import jakarta.persistence.*;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "text")
    private String content = "test";
    @Column(length = 255)
    private int authorId = 1;

    public Post(int id, String content, int author) {
        this.id = id;
        this.content = content;
        this.authorId = author;
    }

    public Post() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int author) {
        this.authorId = author;
    }


}
