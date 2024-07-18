package com.ercanbeyen.bloggingplatform.entity;

import com.ercanbeyen.bloggingplatform.embeddable.Location;
import com.ercanbeyen.bloggingplatform.constant.enums.Gender;
import lombok.*;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Author implements UserDetails {
    @Getter
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    @Getter
    @Setter
    private String firstName;
    @Getter
    @Setter
    private String lastName;
    @Getter
    @Setter
    private String email;
    @Setter
    @Indexed(unique = true)
    private String username;
    @Setter
    private String password;
    @Getter
    @Setter
    @DocumentReference
    private Set<Role> roles;
    @Getter
    @Setter
    private String about;
    @Getter
    @Setter
    private Gender gender;
    @Getter
    @Setter
    private Location location;
    @Getter
    @Setter
    private List<String> favoriteTopics;
    @Getter
    @Setter
    private LocalDateTime createdAt;
    @Getter
    @DocumentReference
    private List<Author> followed;
    @Getter
    @DocumentReference
    private List<Author> followers;
    @Getter
    @Setter
    private Boolean enabled = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        roles.forEach(role -> {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getRoleName().name());
            authorities.add(authority);
        });

        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return enabled;
    }

    @Override
    public String toString() {
        List<String> followerUsernames = followers.stream()
                .map(Author::getUsername)
                .toList();
        List<String> followedUsernames = followed.stream()
                .map(Author::getUsername)
                .toList();

        return "Author{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", about='" + about + '\'' +
                ", gender=" + gender +
                ", location=" + location +
                ", favoriteTopics=" + favoriteTopics +
                ", createdAt=" + createdAt +
                ", followed=" + followedUsernames +
                ", followers=" + followerUsernames +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return id.equals(author.id) && username.equals(author.getUsername()) && email.equals(author.getEmail());
    }
}
