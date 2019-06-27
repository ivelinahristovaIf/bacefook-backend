package com.bacefook.entity;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name = "genders")
public class Gender {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NonNull
	private String genderName;

	@OneToMany(
			mappedBy = "gender",
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	private Set<User> usersFromGender = new HashSet<>();

	public void addUser(User user) {
		usersFromGender.add(user);
		user.setGender(this);
	}

	public void removeUser(User user) {
		usersFromGender.remove(user);
		user.setGender(null);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Gender)) return false;
		Gender gender = (Gender) o;
		return Objects.equals(id, gender.id) &&
				Objects.equals(genderName, gender.genderName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, genderName);
	}
}
