package com.krenai.reviewandrating.entities;


import com.krenai.reviewandrating.masterTable.Role.Role;
import com.krenai.reviewandrating.masterTable.Status.StatusCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //TODO use time based UUID
    private Long id;
    @Column(name="username", unique = true)
    private String username;

    private String password;

    //TODO: make constraint combination with isFlag
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phonenumber;

    private String gender;
    //private String interests;
    //private String profilePicUrl;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isFlag;

    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    private String uuid;


//    @PrePersist
//    protected void whenCreate()
//    {
//        if(this.isFlag==null)
//            this.isFlag=true;
//    }

    //TODO: use predefined annotations and util date
    @CreatedDate
    @Column(name="created_at",updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    //TODO: use predefined annotations and util date

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;


    @ManyToOne
    @JoinColumn(name = "status_id")
    private StatusCode statusCode;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;


}

