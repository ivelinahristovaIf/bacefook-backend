
package com.bacefook.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "gender_id")
    private Gender gender;
    @NonNull
    @Column(unique = true, nullable = false)
    private String email;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String password;
    @NonNull
    private LocalDate birthday;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "comment_likes",
            joinColumns = { @JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "comment_id")}
    )
    Set<Comment> comments = new HashSet<>();

//    @ManyToMany(cascade = {CascadeType.ALL})
//    @JoinTable(
//            name = "comment_likes",
//            joinColumns = { @JoinColumn(name = "user_id") },
//            inverseJoinColumns = {@JoinColumn(name = "comment_id")}
//    )
//    Set<Comment> comments = new HashSet<>();

    public String getFullName() {

        return getFirstName() + " " + getLastName();
    }


}
