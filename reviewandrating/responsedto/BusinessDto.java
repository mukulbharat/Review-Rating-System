package com.krenai.reviewandrating.responsedto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDto {

    private String uuid;
    private String businessName;
    private Double latitude;
    private Double longitude;
    private UserDto userDto;

}
