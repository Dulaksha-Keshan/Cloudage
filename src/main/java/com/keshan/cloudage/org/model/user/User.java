package com.keshan.cloudage.org.model.user;


import com.keshan.cloudage.org.model.image.Image;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Integer id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false , unique = true)
    private String email;

    @Column(nullable = false , unique = true)
    private String password;

    private LocalDate dateOfBirth;

    @OneToMany(mappedBy = "user",
    cascade = CascadeType.ALL,
    fetch = FetchType.LAZY,
    orphanRemoval = true)
    private List<Image> images;

    @Column(name = "is_accountEnabled")
    private boolean accountEnabled;

    @Column(name = "is_accountLocked")
    private boolean accountLocked;

    @Column(name = "is_accountExpired")
    private boolean accountExpired;

    @Column(name = "is_credentialsExpired")
    private boolean credentialsExpired;

    @CreatedDate
    @Column(updatable = false , nullable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.accountEnabled;
    }
}
