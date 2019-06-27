package com.bacefook.entity;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "additional_users_info")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gender_id")
    private City city;
    @OneToOne
    @MapsId
    private Photo profilePhoto;
    @OneToOne
    @MapsId
    private Photo coverPhoto;
    private String phone;

    @OneToOne
    @MapsId
    private User user;

}
