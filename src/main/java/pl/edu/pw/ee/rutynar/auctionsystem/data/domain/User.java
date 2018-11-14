package pl.edu.pw.ee.rutynar.auctionsystem.data.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Document
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements UserDetails {

    @EqualsAndHashCode.Include
    @Getter @Setter
    @ToString.Include
    @Id
    private ObjectId id;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Indexed(unique = true)
    @Setter
    private String username;

    @Getter @Setter
    private String firstName;

    @Getter @Setter

    private String lastName;

    @Getter @Setter
    private String email;

    @Setter
    private String password;

    @Getter @Setter
    private List<Role> roles;

    @Setter
    private boolean enabled;

    @Getter @Setter
    @DBRef
    private Library library;

    @Getter @Setter
    @DBRef
    private List<Auction> followedAuctions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>(this.roles.size());
        for(Role role : this.roles){
            authorities.add(new SimpleGrantedAuthority(role.toString()));
        }
        return authorities;
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

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }
}
