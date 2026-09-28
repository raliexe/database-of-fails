package com.backend.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "app_users")
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 24)
    private String nickname;

    private String email;

    private String password;

    private Boolean isAdmin;

    private Boolean isLocked = false;

    private int failedAttempts = 0;

    private String token;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "fail", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Like> likes;

    @OneToMany(mappedBy = "fail", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments;

    public AppUser(String nickname, String email, String password, Boolean isAdmin) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public AppUser(String nickname) {
        this.nickname = nickname;
    }

    public AppUser(Long id) {
        this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void incrementFailedAttempts() {
        this.failedAttempts++;
    }

    public List<String> getRoles() {
        List<String> roles = new LinkedList<>();
        if (isAdmin) {
            roles.add("ROLE_ADMIN");
        }
        roles.add("ROLE_USER");
        return roles;
    }
}
