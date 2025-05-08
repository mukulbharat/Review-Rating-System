package com.krenai.reviewandrating.user;

import com.krenai.reviewandrating.constants.ApiResponse;
import com.krenai.reviewandrating.entities.User;
import com.krenai.reviewandrating.requestdto.UserRequestDto;
import com.krenai.reviewandrating.responsedto.UserDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

//import static com.krenai.ReviewAndRating.masterTable.ApiConstants.BASE_API_PATH;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> getAll()
    {
        logger.info("Retrieve Users");
//        return new ResponseEntity<List<UserDto>>(userService.getAll(), HttpStatus.OK);

        List<UserDto> users= userService.getAll();
        ApiResponse<List<UserDto>> response = new ApiResponse<>(
                true,
                "Users fetched successfully",
                users,
                users.size()
        );
        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<ApiResponse<UserDto>> createUser(@Valid @RequestBody UserRequestDto userRequestDto)
    {



        logger.info("User Created Successfully");
        User user =userService.createUser(userRequestDto);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(false, "Email or phone number already exists", null));
        }


        UserDto userDto=userService.convertToDto(user);
        ApiResponse<UserDto> response = new ApiResponse<>(
                true,
                "User created successfully",
                userDto
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/create-admin")
    public ResponseEntity<ApiResponse<UserDto>> createAdminUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        log.info("Creating a new admin user");

        User adminUser = userService.createAdminUser(userRequestDto);




        if (adminUser == null) {
            log.warn("Failed to create admin user");
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(false, "Admin user creation failed", null));
        }
        UserDto userDto=userService.convertToDto(adminUser);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Admin user created successfully", userDto));
    }

    @PostMapping("/create-owner")
    public ResponseEntity<ApiResponse<UserDto>> createBusinessOwner(@Valid @RequestBody UserRequestDto userRequestDto) {
        log.info("Creating business owner user...");

        User ownerUser = userService.createBusinessOwnerUser(userRequestDto);

        if (ownerUser == null) {
            log.warn("Failed to create business owner user: Role or StatusCode missing");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, "Business owner creation failed", null));
        }

        UserDto userDto = userService.convertToDto(ownerUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Business owner created successfully", userDto));
    }


    @GetMapping("/{uuid}")
    public ResponseEntity<?> getByUUID(@PathVariable String uuid)
    {
            logger.info("Fetching user with UUID: {}", uuid);
            Optional<UserDto> userDto = userService.getUserByUUID(uuid);
        if (userDto.isPresent()) {
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "User fetched successfully", userDto.get())
            );
        }else {
            logger.warn("User not found with UUID: {}", uuid);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "User not found or is already deleted", null));
        }

    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(
            @PathVariable String uuid,
            @RequestBody UserRequestDto userRequestDto,
            @RequestParam Long roleID,
            @RequestParam Long statusCodeID) {



        logger.info("Updating user with UUID: {}", uuid);
        Optional<UserDto> updatedUserDto = userService.updateUser(uuid, userRequestDto, roleID, statusCodeID);

        if (updatedUserDto.isPresent()) {
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "User updated successfully", updatedUserDto.get())
            );
        } else {
            logger.warn("User update failed for UUID: {}", uuid);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "User update failed. User might not exist.", null));
        }


    }


    @DeleteMapping("/{uuid}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable String uuid) {
        logger.info("Deleting user with UUID: {}", uuid);
        String result = userService.deleteUser(uuid);

        boolean isDeleted = result.toLowerCase().contains("success");

        return ResponseEntity.ok(
                new ApiResponse<>(isDeleted, result, null));
    }
}
