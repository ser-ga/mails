package org.zavod.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "author")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AuthorEntity implements Serializable {

    public static final int GLOBAL_SEQ = 10_000;

    @Id
    @SequenceGenerator(name = "GLOBAL_SEQ", sequenceName = "GLOBAL_SEQ", initialValue = GLOBAL_SEQ, allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, columnDefinition = "bigint default GLOBAL_SEQ.nextval")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GLOBAL_SEQ")
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

    @Column(name = "phone")
    private String phone;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "author_id"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.LAZY)
    private Set<Role> roles;

    @Override
    public String toString() {
        return fullName;
    }
}
