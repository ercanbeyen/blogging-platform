package com.ercanbeyen.bloggingplatform.document;

import com.ercanbeyen.bloggingplatform.constant.Location;
import com.ercanbeyen.bloggingplatform.constant.Gender;
import lombok.*;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
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


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Author implements UserDetails {
    @MongoId(FieldType.OBJECT_ID)
    //@Id
    private String id;
    private String firstName;
    private String lastName;
    @Indexed(unique = true)
    private String email;
    @Indexed(unique = true)
    private String username;
    private String password;
    @DocumentReference
    private Set<Role> roles;
    private String about;
    private Gender gender;
    private Location location;
    private List<String> favoriteTopics;
    private LocalDateTime createdAt;
    @DocumentReference
    private List<Author> followed;
    @DocumentReference
    private List<Author> followers;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        roles.forEach(
                role -> {
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getRoleName().name());
                    authorities.add(authority);
                }
        );

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
        return true;
    }

    @Override
    public String toString() {
        List<String> followerIds = followers.stream()
                .map(Author::getId)
                .toList();
        List<String> followedIds = followed.stream()
                .map(Author::getId)
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
                ", followed=" + followedIds +
                ", followers=" + followerIds +
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
