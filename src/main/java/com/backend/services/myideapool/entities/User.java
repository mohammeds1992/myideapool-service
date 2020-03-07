package com.backend.services.myideapool.entities;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.Transient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.backend.services.myideapool.utils.MD5Util;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class User implements UserDetails {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private Integer id;

	@Setter
	private String name;

	@Setter
	private String email;

	@Setter
	@JsonIgnore
	private String password;

	@Setter
	private Long created_at;

	@Setter
	@JsonIgnore
	private String refresh_token;
	
	@Transient
	private String avatar_url;

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new ArrayList<GrantedAuthority>();
	}

	@Override
	@JsonIgnore
	public String getUsername() {
		return this.email;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return true;
	}

	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}
	
	public String getAvatar_url() {
		return MD5Util.getAvatarURL(this.email);
	}
}