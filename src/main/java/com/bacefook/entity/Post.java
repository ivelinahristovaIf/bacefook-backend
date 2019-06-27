package com.bacefook.entity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")//user_id
    @NonNull
    private User poster;
    /**
     * post sharing
     **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shares_post_id")
    private Post sharesPost;
    @OneToMany(
            mappedBy = "sharesPost",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Post> postShares = new HashSet<>();
    /**
     * end post sharing
     **/
    @NonNull
    @Column(nullable = false)
    private String content;
    @NonNull
    @Column(nullable = false)
    private LocalDateTime postingTime;

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
//	@JsonBackReference
    private Set<Comment> comments = new HashSet<>();

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setPost(null);
    }

    @ManyToMany(mappedBy = "likedPosts")
    @JsonManagedReference
    private Set<User> likers = new HashSet<>();
    //TODO add likers here

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    private Photo photo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id) &&
                Objects.equals(poster, post.poster) &&
                Objects.equals(sharesPost, post.sharesPost) &&
                Objects.equals(content, post.content) &&
                Objects.equals(postingTime, post.postingTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, poster, sharesPost, content, postingTime);
    }
}
