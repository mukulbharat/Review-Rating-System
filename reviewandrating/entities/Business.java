package com.krenai.reviewandrating.entities;


import com.krenai.reviewandrating.masterTable.Category.Category;
import com.krenai.reviewandrating.masterTable.Status.StatusCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "Business")
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String businessname;
    private Double latitude;
    private Double longitude;
    //private String logoUrl;
    private Boolean isFlag;

    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    private String uuid;

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdat;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

//    @OneToOne
//    @JoinColumn(name = "Category_ID")
//    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "User has to be present")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "status_id")
    @NotNull(message = "Status is mandatory")
    private StatusCode statusCode;

}



