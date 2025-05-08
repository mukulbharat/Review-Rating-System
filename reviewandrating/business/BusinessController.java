package com.krenai.reviewandrating.business;


import com.krenai.reviewandrating.constants.ApiResponse;
import com.krenai.reviewandrating.requestdto.BusinessRequestDto;
import com.krenai.reviewandrating.entities.Business;
import com.krenai.reviewandrating.responsedto.BusinessDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//import static com.krenai.ReviewAndRating.masterTable.ApiConstants.BASE_API_PATH;

@Slf4j
@RestController
@RequestMapping("/businesses")
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @GetMapping("/get-all-businesses")
    public ResponseEntity<ApiResponse<List<BusinessDto>>> getAllBusinesses() {

        log.info("Fetching all businesses");
        List<BusinessDto> businesses= businessService.getAllBusinesses();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Businesses fetched successfully",
                        businesses,
                        businesses.size()
                )
        );

    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponse<BusinessDto>> getBusinessByUUId(@PathVariable String uuid) {

        log.info("Fetching business with UUID: {}", uuid);
        BusinessDto businessDto=businessService.getBusinessDtoByUUId(uuid);

        if (businessDto == null) {
            log.warn("Business not found or deleted with UUID: {}", uuid);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Business not found or deleted", null));
        }

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Business fetched successfully", businessDto)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BusinessDto>> createBusiness(@Valid @RequestBody BusinessRequestDto businessRequestDto) {

        log.info("Creating a new business");
        BusinessDto responseDto = businessService.createBusiness(businessRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Business created successfully", responseDto));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ApiResponse<BusinessDto>> updateBusiness(@PathVariable String uuid, @RequestBody BusinessRequestDto businessRequestDto) {

        log.info("Updating business with UUID: {}", uuid);
        BusinessDto updatedBusiness = businessService.updateBusiness(uuid, businessRequestDto);

        if (updatedBusiness == null) {
            log.warn("Failed to update business with UUID: {}", uuid);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Business update failed. Business might not exist.", null));
        }

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Business updated successfully", updatedBusiness)
        );
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<ApiResponse<String>> deleteBusiness(@PathVariable String uuid) {

        log.info("Deleting business with UUID: {}", uuid);

        String message=businessService.deleteBusiness(uuid);
        return ResponseEntity.ok(
                new ApiResponse<>(true, message, message)
        );
    }
}
