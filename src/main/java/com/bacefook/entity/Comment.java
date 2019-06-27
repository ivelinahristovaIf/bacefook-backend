package com.bacefook.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NonNull
    @Column(nullable = false)
    private Integer posterId;//TODO User
    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commented_on_id")
    private Comment commentedOn;
    @OneToMany(
            mappedBy = "commentedOn",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Comment> commentReplies = new HashSet<>();
    @NonNull
    @Column(nullable = false)
    private String content;
    @NonNull
    @Column(nullable = false)
    private LocalDateTime postingTime;

    @ManyToMany(mappedBy = "likedComments")
    @JsonManagedReference
    private Set<User> users = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) &&
                Objects.equals(posterId, comment.posterId) &&
                Objects.equals(post, comment.post) &&
                Objects.equals(commentedOn, comment.commentedOn) &&
                Objects.equals(content, comment.content) &&
                Objects.equals(postingTime, comment.postingTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, posterId, post, commentedOn, content, postingTime);
    }

}
