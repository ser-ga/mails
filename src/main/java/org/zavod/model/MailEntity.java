package org.zavod.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mail")
//https://stackoverflow.com/questions/24994440/no-serializer-found-for-class-org-hibernate-proxy-pojo-javassist-javassist
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MailEntity implements Serializable {

    @SerializedName("id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "create_date", nullable = false)
    private LocalDate createDate;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDateTime;

    @NotNull
    @Column(name = "number", nullable = false)
    private Integer mailNumber;

    @NotEmpty
    @Size(min = 3, max = 100)
    @Column(name = "recipient", nullable = false)
    private String mailRecipient;

    @NotEmpty
    @Column(name = "subject", nullable = false, length = 1000)
    private String mailSubject;

    @NotEmpty
    @Column(name = "title", nullable = false, length = 100)
    private String mailTitle;

    @NotEmpty
    @Column(name = "text", nullable = false, length = 10000)
    private String mailText;

    @Column(name = "accept", nullable = false)
    private boolean accept;

    @Column(name = "version", nullable = false)
    private int version = 1;

    @SerializedName("author")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private AuthorEntity author;
}
