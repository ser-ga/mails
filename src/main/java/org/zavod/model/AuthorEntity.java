package org.zavod.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "author")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotEmpty
    @Size(min=2, max=100)
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @JsonIgnore
    @NotEmpty
    @Size(min=2, max=100)
    @Column(name = "username", nullable = false)
    private String username;

    @JsonIgnore
    @NotEmpty
    @Size(min=5, max=100)
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "active")
    private boolean active;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "author_id"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.LAZY)
    private Set<Role> roles;
}
