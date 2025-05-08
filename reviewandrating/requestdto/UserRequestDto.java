package com.krenai.reviewandrating.requestdto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @NotBlank(message = "Username can't be null")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Email is mandatory")
    @NotEmpty(message = "Email can't be empty")
    @Email(message = "Invalid Email")
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    @NotNull(message = "Phone Number is mandatory")
    @NotEmpty(message = "Phone Number can't be empty")
    private String phoneNumber;

    @NotNull(message = "Gender is required")
    @Pattern(regexp = "^(?i)(male|female|Male|Female|other|Other)$", message = "Gender must be Male, Female, or Other")
    private String gender;

    //@NotNull(message = "Role ID is required")
    private Long roleId;

    //@NotNull(message = "Role ID is required")
    private Long statusCodeId;
}
