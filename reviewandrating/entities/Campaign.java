package com.krenai.reviewandrating.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="Campaign")
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long campaignId;
    private String name;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private Double budget;
    private Boolean isFlag;
    private LocalDateTime createdAt=LocalDateTime.now();
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "BusinessID")
    private Business business;

//    @ManyToOne
//    @JoinColumn(name = "Status_Code_ID")
//    private StatusCode statusCode;

}
