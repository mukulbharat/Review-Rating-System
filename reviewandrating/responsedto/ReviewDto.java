package com.krenai.reviewandrating.responsedto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private String uuid;
    private String reviewText;
    private Double rating;

    private UserDto user;
    private BusinessDto business;

}
