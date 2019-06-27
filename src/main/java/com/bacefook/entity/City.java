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
@Table(name = "cities")
public class City {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NonNull
	private String cityName;

	@OneToMany(
			mappedBy = "city",
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	private Set<UserInfo> usersFromCity = new HashSet<>();

	public void addUserInfo(UserInfo user) {
		usersFromCity.add(user);
		user.setCity(this);
	}

	public void removeUserInfo(UserInfo user) {
		usersFromCity.remove(user);
		user.setCity(null);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof City)) return false;
		City city = (City) o;
		return Objects.equals(id, city.id) &&
				Objects.equals(cityName, city.cityName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, cityName);
	}
}
