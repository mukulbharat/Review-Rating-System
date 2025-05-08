package com.krenai.reviewandrating.business;


//import com.krenai.ReviewAndRating.business.BusinessRequestDto;
import com.fasterxml.uuid.Generators;
import com.krenai.reviewandrating.masterTable.Category.Category;
import com.krenai.reviewandrating.masterTable.Category.CategoryRepository;
import com.krenai.reviewandrating.requestdto.BusinessRequestDto;
import com.krenai.reviewandrating.entities.Business;
import com.krenai.reviewandrating.masterTable.Status.StatusCode;
import com.krenai.reviewandrating.masterTable.Status.StatusRepository;
import com.krenai.reviewandrating.entities.User;
import com.krenai.reviewandrating.requestdto.UserRequestDto;
import com.krenai.reviewandrating.responsedto.BusinessDto;
import com.krenai.reviewandrating.responsedto.UserDto;
import com.krenai.reviewandrating.user.UserRepository;
import com.krenai.reviewandrating.user.UserService;
import org.apache.logging.log4j.core.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusinessService {

    //private static final Long DEFAULT_OWNER_ROLE_ID = 3L;
    private static final Long DEFAULT_STATUS_CODE_ID = 1L;
    private static final Long INACTIVE_STATUS_CODE_ID = 2L;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserService userService;


    public List<BusinessDto> getAllBusinesses() {
        List<Business> businesses = businessRepository.findAll()
                .stream()
                .filter(Business::getIsFlag)
                .collect(Collectors.toList());

        return businesses.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private BusinessDto convertToDto(Business business) {
        UserDto userDto = new UserDto();
        userDto.setUuid(business.getUser().getUuid());
        userDto.setUserName(business.getUser().getUsername());
        userDto.setPhoneNumber(business.getUser().getPhonenumber());

        BusinessDto dto = new BusinessDto();
        dto.setUuid(business.getUuid());
        dto.setBusinessName(business.getBusinessname());
        dto.setLatitude(business.getLatitude());
        dto.setLongitude(business.getLongitude());
        dto.setUserDto(userDto);

        return dto;
    }


    public BusinessDto getBusinessDtoByUUId(String uuid) {
        Business business=businessRepository.findByUuid(uuid)
                .orElseThrow(()->new RuntimeException("Business Not Found"));

        if (!business.getIsFlag()) {
            throw new RuntimeException("Business has been deleted");}

        return convertToDto(business);
    }

//    public Business createBusiness(Business business,Long userId, Long statusCodeID)
//    {
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
//        StatusCode statusCode = statusRepository.findById(statusCodeID)
//                .orElseThrow(() -> new RuntimeException("Status Code not found with ID: " + statusCodeID));
//
//        business.setUser(user);
//        business.setStatusCode(statusCode);
//        return businessRepository.save(business);
//    }

    public BusinessDto createBusiness(BusinessRequestDto dto) {

        User user;
        if (dto.isCreateNewUser()) {
            UserRequestDto userRequestDto = dto.getUserRequestDto();
            user = userService.createBusinessOwnerUser(userRequestDto);
        } else {
            String uuid = dto.getUserId();
            if (uuid == null || uuid.isBlank()) {
                throw new IllegalArgumentException("Existing user UUID must be provided if createNewUser is false");
            }

            user = userRepository.findByUuid(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserId()));
        }
        if(user==null)
        {
            throw new RuntimeException("user might already be present");
        }

//        StatusCode statusCode = statusRepository.findById(DEFAULT_STATUS_CODE_ID)
//                .orElseThrow(() -> new RuntimeException("StatusCode not found with ID: " + dto.getStatusCodeId()));

        Category category=categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(()-> new RuntimeException("Category not found with UUID: " + dto.getCategoryId()));

        StatusCode defaultStatus= new StatusCode();
        defaultStatus.setId(DEFAULT_STATUS_CODE_ID);

        Business business = new Business();
        business.setBusinessname(dto.getBusinessName());
        business.setLatitude(dto.getLatitude());
        business.setLongitude(dto.getLongitude());
        business.setIsFlag(true);
        business.setUser(user);
        business.setCategory(category);
        business.setStatusCode(defaultStatus);
        //business.setUuid(Generators.timeBasedGenerator().generate().toString());
        business.setUuid(UuidUtil.getTimeBasedUuid().toString());
        //business.setCreatedAt(java.time.LocalDateTime.now());

        Business savedBusiness = businessRepository.save(business);

        return convertToDto(savedBusiness);
    }

    public BusinessDto updateBusiness(String uuid, BusinessRequestDto businessRequestDto) {
        return businessRepository.findByUuid(uuid).map(business -> {
            business.setBusinessname(businessRequestDto.getBusinessName());
            business.setLatitude(businessRequestDto.getLatitude());
            business.setLongitude(businessRequestDto.getLongitude());
            //business.setIsFlag(businessDetails.getIsFlag());
            User user = userRepository.findByUuid(businessRequestDto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with UUID: " + businessRequestDto.getUserId()));
            business.setUser(user);
            //business.setUpdatedAt(LocalDateTime.now());
            return convertToDto(businessRepository.save(business));
        }).orElseThrow(() -> new RuntimeException("Business not found"));
    }

    public String deleteBusiness(String uuid) {
        Business business=businessRepository.findByUuid(uuid)
                        .orElseThrow(()->new RuntimeException("Business Not Found"));

        if (!business.getIsFlag()) {
            return "Business not found";
        }

        StatusCode inactiveStatus = new StatusCode();
        inactiveStatus.setId(INACTIVE_STATUS_CODE_ID);
        business.setStatusCode(inactiveStatus);
        business.setIsFlag(false);
        //business.setUpdatedAt(LocalDateTime.now());

        businessRepository.save(business);

        return "Delete Successful";
    }
}
