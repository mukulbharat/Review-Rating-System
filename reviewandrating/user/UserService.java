package com.krenai.reviewandrating.user;

import com.fasterxml.uuid.Generators;
import com.krenai.reviewandrating.entities.User;
import com.krenai.reviewandrating.firebase.FirebaseUserService;
import com.krenai.reviewandrating.masterTable.Role.RoleRepository;
import com.krenai.reviewandrating.masterTable.Role.Role;
import com.krenai.reviewandrating.masterTable.Status.StatusCode;
import com.krenai.reviewandrating.masterTable.Status.StatusRepository;
import com.krenai.reviewandrating.requestdto.UserRequestDto;
import com.krenai.reviewandrating.responsedto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private FirebaseUserService firebaseUserService;

    private static final Long DEFAULT_USER_ROLE_ID = 3L;
    private static final Long DEFAULT_ADMIN_ROLE_ID = 1L;
    private static final Long DEFAULT_OWNER_ROLE_ID = 2L;
    private static final Long DEFAULT_STATUS_CODE_ID = 1L;
    private static final Long INACTIVE_STATUS_CODE_ID = 2L;




    //getAllUser as DTO
    public List<UserDto> getAll(){
        try {
            return userRepository.findAllByIsFlagTrue()
                    .stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toUnmodifiableList());
        } catch (Exception e) {
            throw new RuntimeException(e+" error occured in fetching User's");
        }
    }

    public UserDto convertToDto(User user)
    {
        UserDto userDto=new UserDto();
        userDto.setUuid(user.getUuid());
        userDto.setUserName(user.getUsername());
        userDto.setPhoneNumber(user.getPhonenumber());

        return userDto;
    }


    public User createUser(UserRequestDto dto) {


            /*
            here we are trying to find if a user with same email and isFlag=true exists
             */
            //Optional<User> existingActiveUser = userRepository.findByEmailAndIsFlagTrue(dto.getEmail());
            Optional<User> existingActiveUser=userRepository.findByEmailOrPhonenumberAndIsFlagTrue(dto.getEmail(),dto.getPhoneNumber());

            //TODO use JPA Query existsBY
            if (existingActiveUser.isPresent()) {
                return null;
            }

//            Role role = roleRepository.findById(DEFAULT_USER_ROLE_ID)
//             .orElseThrow(() -> new RuntimeException("Role not found"));
            Role defaultRole = new Role();
            defaultRole.setId(DEFAULT_USER_ROLE_ID);


//            StatusCode statusCode = statusRepository.findById(DEFAULT_STATUS_CODE_ID)
//                    .orElseThrow(() -> new RuntimeException("Status Code not found"));
            StatusCode defaultStatus= new StatusCode();
            defaultStatus.setId(DEFAULT_STATUS_CODE_ID);
//            defaultStatus.setStatus(statusRepository.findById(DEFAULT_STATUS_CODE_ID));

        String uuid=UuidUtil.getTimeBasedUuid().toString();

        try{
        firebaseUserService.createUserWithCustomUid(
                uuid,
                dto.getEmail(),
                dto.getPassword()
        );} catch (Exception e) {
            throw new RuntimeException(e);
        }

            User user = new User();
            //System.out.println("jsbajb");
            user.setUsername(dto.getUsername());
            user.setPassword(dto.getPassword());
            user.setEmail(dto.getEmail());
            user.setPhonenumber(dto.getPhoneNumber());
            user.setGender(dto.getGender());
            user.setRole(defaultRole);
            //System.out.println("bacjk");
            user.setStatusCode(defaultStatus);
            user.setIsFlag(true);
            user.setUuid(uuid);
            //user.setUuid(Generators.timeBasedGenerator().generate().toString());
            //user.setCreatedAt(LocalDateTime.now());

            return userRepository.save(user);

    }

    public User createAdminUser(UserRequestDto userRequestDto) {

        Optional<User> existingActiveUser=userRepository.findByEmailOrPhonenumberAndIsFlagTrue(userRequestDto.getEmail(),userRequestDto.getPhoneNumber());

        if (existingActiveUser.isPresent()) {
            log.warn("Admin might already be present ");
            return null;
        }

        Role adminRole = roleRepository.findById(DEFAULT_ADMIN_ROLE_ID).orElse(null);
        if (adminRole == null) {
            log.warn("Admin role not found with ID: {}", DEFAULT_ADMIN_ROLE_ID);
            return null;
        }

        StatusCode defaultStatus = statusRepository.findById(1L).orElse(null); // Assuming 1L is "active"
        if (defaultStatus == null) {
            log.warn("Default status code not found");
            return null;
        }

        String uuid=UuidUtil.getTimeBasedUuid().toString();

        try{
            firebaseUserService.createUserWithCustomUid(
                    uuid,
                    userRequestDto.getEmail(),
                    userRequestDto.getPassword()
            );} catch (Exception e) {
            throw new RuntimeException(e);
        }

        User user = new User();
        user.setUuid(uuid);
        user.setUsername(userRequestDto.getUsername());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(userRequestDto.getPassword());
        user.setPhonenumber(userRequestDto.getPhoneNumber());
        user.setGender(userRequestDto.getGender());
        user.setRole(adminRole);
        user.setStatusCode(defaultStatus);
        user.setIsFlag(true);
        //user.setUuid(Generators.timeBasedGenerator().generate().toString());

        User savedUser = userRepository.save(user);
        log.info("Admin user created successfully with UUID: {}", savedUser.getUuid());

        return savedUser;
    }

    public User createBusinessOwnerUser(UserRequestDto userRequestDto) {

        Optional<User> existingActiveUser=userRepository.findByEmailOrPhonenumberAndIsFlagTrue(userRequestDto.getEmail(),userRequestDto.getPhoneNumber());

        if (existingActiveUser.isPresent()) {
            log.warn("Owner might already be present ");
            return null;
        }

        Role ownerRole = roleRepository.findById(DEFAULT_OWNER_ROLE_ID).orElse(null);
        if (ownerRole == null) {
            log.warn("Business Owner role not found with ID: {}", DEFAULT_OWNER_ROLE_ID);
            return null;
        }

        StatusCode defaultStatus = statusRepository.findById(1L).orElse(null); // Assuming 1L = Active
        if (defaultStatus == null) {
            log.warn("Default status code not found");
            return null;
        }

        String uuid=UuidUtil.getTimeBasedUuid().toString();

        try{
            firebaseUserService.createUserWithCustomUid(
                    uuid,
                    userRequestDto.getEmail(),
                    userRequestDto.getPassword()
            );} catch (Exception e) {
            throw new RuntimeException(e);
        }

        User user = new User();
        user.setUuid(uuid);
        user.setUsername(userRequestDto.getUsername());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(userRequestDto.getPassword());
        user.setPhonenumber(userRequestDto.getPhoneNumber());
        user.setGender(userRequestDto.getGender());
        user.setRole(ownerRole);
        user.setStatusCode(defaultStatus);
        user.setIsFlag(true);
        //user.setUuid(Generators.timeBasedGenerator().generate().toString());
        //user.setUuid(UuidUtil.getTimeBasedUuid().toString());

        User savedUser = userRepository.save(user);
        log.info("Business Owner user created with UUID: {}", savedUser.getUuid());

        return savedUser;
    }


    public Optional<UserDto> getUserByUUID(String uuid) {

        //TODO before fetching thrugh findByUUID check if isFlag is true or false
//        try {
//            User user=userRepository.findByUuid(uuid)
//                    .filter(User::getIsFlag)
//                    .orElseThrow(() -> new RuntimeException("User not found with UUID: " + uuid));
//            return convertToDto(user);
//        } catch (Exception e) {
//            throw new RuntimeException("Couldn't get the User"+e.getMessage());
//        }

        return userRepository.findByUuidAndIsFlagTrue(uuid)
                .map(this::convertToDto);

    }

    public Optional<UserDto> updateUser(String uuid, UserRequestDto userRequestDto, Long roleId, Long statusCodeID) {

//        User updatedUser = userRepository.findByUuid(uuid).map(user -> {
//            Role role = roleRepository.findById(roleId)
//                    .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));
//
//            StatusCode statusCode = statusRepository.findById(statusCodeID)
//                    .orElseThrow(() -> new RuntimeException("Status Code not found with ID: " + statusCodeID));
//
//            user.setUsername(userRequestDto.getUsername());
//            user.setEmail(userRequestDto.getEmail());
//            user.setPhonenumber(userRequestDto.getPhoneNumber());
//            user.setGender(userRequestDto.getGender());
//            user.setRole(role);
//            user.setStatusCode(statusCode);
//            //user.setIsFlag(userRequestDto.getIsFlag());
//
//            return userRepository.save(user);
//        }).orElseThrow(() -> new RuntimeException("User not found with UUID: " + uuid));


        Optional<User> optionalUser=userRepository.findByUuidAndIsFlagTrue(uuid);

        if(optionalUser.isEmpty())
        {
            return Optional.empty();
        }

        Optional<Role> optionalRole = roleRepository.findById(roleId);
        Optional<StatusCode> optionalStatus = statusRepository.findById(statusCodeID);

        if (optionalRole.isEmpty() || optionalStatus.isEmpty()) {
            return Optional.empty(); // either role or status doesn't exist
        }

        User user = optionalUser.get();
        user.setUsername(userRequestDto.getUsername());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(userRequestDto.getPassword());
        user.setPhonenumber(userRequestDto.getPhoneNumber());
        user.setGender(userRequestDto.getGender());
        user.setRole(optionalRole.get());
        user.setStatusCode(optionalStatus.get());

        User updatedUser=userRepository.save(user);
        return Optional.of(convertToDto(updatedUser)); // Assuming this method exists
    }
    public String deleteUser(String uuid) {
//        try {
//            User user = userRepository.findByUuid(uuid).orElseThrow(() -> new RuntimeException("User not exists"));
//            if (!user.getIsFlag()) {
//                throw new RuntimeException("User does not exist or is already deleted");
//            }
//
//            user.setIsFlag(false);
//            StatusCode inactiveStatus = new StatusCode();
//            inactiveStatus.setId(INACTIVE_STATUS_CODE_ID);
//            user.setStatusCode(inactiveStatus);
//            //user.setUpdatedAt(LocalDateTime.now());
//            userRepository.save(user);
//        } catch (RuntimeException e) {
//            System.out.println("Exception : {}"+ e.getMessage());
//            throw new RuntimeException(e);
//        }

        Optional<User> userOptional=userRepository.findByUuid(uuid);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!user.getIsFlag()) {
                return "User is already deleted";
            }
            user.setIsFlag(false);
            StatusCode inactiveStatus = new StatusCode();
            inactiveStatus.setId(INACTIVE_STATUS_CODE_ID);
            user.setStatusCode(inactiveStatus);
            userRepository.save(user);
            return "success";
        }
        return "User not found";
    }

}
