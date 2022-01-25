package org.example.service.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.constant.RegisterStep;
import org.example.dto.response.CustomerInfo;
import org.example.dto.response.CustomerPage;
import org.example.dto.response.CustomerProfileDto;
import org.example.dto.response.CustomerProfileOtherDto;
import org.example.entity.CustomerFriend;
import org.example.repository.CustomerFriendRepository;
import org.example.repository.CustomerRegisterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.common.exception.BusinessException;
import org.common.jpa.SearchInput;
import org.common.utils.DataUtil;
import org.example.dto.request.CustomerProfileUpdateRequest;
import org.example.dto.request.RegisterCustomerPistRequest;
import org.example.dto.request.SearchCustomerFollowRequest;
import org.pist.dto.response.*;
import org.example.entity.Customer;
import org.example.entity.CustomerRegister;
import org.example.repository.CustomerRepository;
import org.example.service.CustomerFriendService;
import org.example.service.CustomerService;
import org.example.util.CustomerUtil;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static org.common.utils.DateUtil.*;
import static org.example.util.Constant.SourceFollow.NORMAL;
import static org.example.util.Message.Error.*;

@Slf4j
@Service
@Validated
public class CustomerServiceImpl extends BaseServiceImpl implements CustomerService {
    public static final int MINIMUM_FRIENDS_SUGGESTED = 3;
    private static Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerRegisterRepository customerRegisterRepository;
    @Autowired
    private CustomerFriendService customerFriendService;
    @Autowired
    private CustomerFriendRepository customerFriendRepository;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private CustomerUtil customerUtil;

    @Override
    public Customer getCurrentCustomer() throws BusinessException {
        return getCustomerById(getAuthentication().getUserId());
    }

    @Override
    public Customer getCustomerById(Long idCustomer) throws BusinessException {
        Optional<Customer> optionalCustomer = customerRepository.findById(idCustomer);
        if (!optionalCustomer.isPresent()) {
            throw new BusinessException("Customer was not found. Please contact administrator to be supported");
        }
        return optionalCustomer.get();
    }

    @Override
    public List<Customer> findAllByIdIsIn(List<Long> ids) {
        return customerRepository.findAllByIdIsIn(ids);
    }

    @Override
    public List<Customer> getAllByIdIsIn(List<String> idList) {
        if (idList == null || idList.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> ids = new ArrayList<>();
        for (String id : idList) {
            try {
                ids.add(Long.valueOf(id));
            } catch (Exception e) {
                log.error("getAllByIdIsIn: unable to cast from String to Long. strId: {}. Ignore it.", id);
            }
        }
        return findAllByIdIsIn(ids);
    }

    @Override
    public List<CustomerInfo> getCustomerInfoByIds(List<String> customerIds) {
        List<Long> ids = new ArrayList<>();
        for (String id : customerIds) {
            try {
                ids.add(Long.valueOf(id));
            } catch (Exception e) {
                log.error("getCustomerInfoByIds: unable to cast from String to Long. strId: {}. Ignore it.", id);
            }
        }
        List<Customer> customerList = customerRepository.findAllByIdIsIn(ids);
        customerList = customerList != null ? customerList : new ArrayList<>();
        return customerList.stream().map(customer -> CustomerInfo.builder()
                .avatar(customer.getAvatar())
                .customerId(customer.getId())
                .name(customer.getName())
                .displayName(customer.getDisplayName())
                .phoneNumber(customer.getPhone())
                .email(customer.getEmail())
                .build()).collect(Collectors.toList());
    }

    @Override
    public List<Customer> getLatestCustomerSubscribeTheme(Long idCustomer, String themeCode) throws BusinessException {
        return customerRepository.getLatestSubscribeThemeWithLimit(idCustomer, themeCode);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void createNewPistCustomer(RegisterCustomerPistRequest request) throws BusinessException {
        logger.info("RegisterCustomerPistRequest: " + request.toString());
        validateRegisterPistCustomer(request);
        CustomerRegister existedRegisterCustomer = customerRegisterRepository.findByPhone(request.getPhone());
        if (existedRegisterCustomer == null) {
            existedRegisterCustomer = CustomerRegister.builder()
                    .phone(request.getPhone())
                    .step(RegisterStep.VALID_PHONE_NUMBER.getStep())
                    .createdAt(now())
                    .updatedAt(now())
                    .build();
        } else if (request.getStep() == RegisterStep.VERIFY_OTP.getStep()) {
            existedRegisterCustomer.setEmail(request.getEmail());
            existedRegisterCustomer.setStep(RegisterStep.VERIFY_OTP.getStep());
        } else if (request.getStep() == RegisterStep.VALID_INFORMATION_USER.getStep()) {
            if (StringUtils.isBlank(existedRegisterCustomer.getDisplayName())) {
                existedRegisterCustomer.setDisplayName(StringUtils.isNotBlank(request.getDisplayName()) ? request.getDisplayName() : request.getFullName());
            }
            existedRegisterCustomer.setDisplayName(request.getDisplayName());
            existedRegisterCustomer.setRefCode(request.getRefCode());
            existedRegisterCustomer.setStep(RegisterStep.VALID_INFORMATION_USER.getStep());
            if (StringUtils.isBlank(existedRegisterCustomer.getAvatar())) {
                String defaultAvatar = "";
                if (!StringUtils.isBlank(request.getDisplayName())) {
                    defaultAvatar = request.getDisplayName();
                } else if (!StringUtils.isBlank(request.getEmail())) {
                    defaultAvatar = request.getEmail();
                } else if (!StringUtils.isBlank(existedRegisterCustomer.getDisplayName())) {
                    defaultAvatar = existedRegisterCustomer.getDisplayName();
                } else if (!StringUtils.isBlank(existedRegisterCustomer.getEmail())) {
                    defaultAvatar = existedRegisterCustomer.getEmail();
                }
                existedRegisterCustomer.setAvatar(customerUtil.setDefaultAvatar(defaultAvatar));
            }
        } else if (request.getStep() == RegisterStep.VALID_USERNAME.getStep()) {
            existedRegisterCustomer.setUserName(request.getUsername());
            existedRegisterCustomer.setStep(RegisterStep.VALID_USERNAME.getStep());
        }
        existedRegisterCustomer.setUpdatedAt(now());
        customerRegisterRepository.save(existedRegisterCustomer);
    }

    @Override
    public CustomerPage getSuggestedFriends(SearchInput searchInput) throws BusinessException {
        List<CustomerFriend> cfs = customerFriendService.findFriendListByCustomerId(getAuthentication().getUserId());
        CustomerPage customerPage;
        if (cfs.isEmpty()) {
            customerPage = customerRepository.getSuggestedCustomerByWatchlist(getAuthentication().getUserId(), searchInput);
        } else {
            List<Long> idFriends = cfs.stream().map(CustomerFriend::getIdCustomer).collect(Collectors.toList());
            customerPage = customerFriendRepository.getSuggestedCustomer(idFriends, getAuthentication().getUserId(), searchInput);
        }
        if (customerPage.getTotalElements() < MINIMUM_FRIENDS_SUGGESTED) {
            customerPage = getDefaultSuggestedFriends(getAuthentication().getUserId(), searchInput);
        }
        return customerPage;
    }

    private CustomerPage getDefaultSuggestedFriends(Long idCustomer, SearchInput searchInput) {
        return customerFriendRepository.getDefaultSuggestedFriends(idCustomer, searchInput);
    }

    @Override
    public CustomerPage getFollowing(SearchCustomerFollowRequest request) throws BusinessException {
        try {
            return customerRepository.getListFollowing(getAuthentication().getUserId(), request);

        } catch (Exception ex) {
            logger.error("Error during get following customer id: " + getAuthentication().getUserId() + " \n " + ex);
            return new CustomerPage(0L, 0L, 0L, Lists.newArrayList());
        }
    }

    @Override
    public CustomerPage getFollower(SearchCustomerFollowRequest request) throws BusinessException {
        try {
            Long customerId = getAuthentication().getUserId();
            List<Long> followings = customerFriendService.getListIdCustomerFollowing(customerId);
            CustomerPage followers = customerRepository.getListFollower(customerId, request);

            followers.getData().forEach(follower -> {
                if (!followings.contains(follower.getId())) {
                    follower.setIsFollowed(false);
                }
            });
            return followers;
        } catch (Exception ex) {
            logger.error("Error during get followers customer id: " + getAuthentication().getUserId() + " \n " + ex);
            return new CustomerPage(0L, 0L, 0L, Lists.newArrayList());
        }
    }

    @Override
    public CustomerPage getFollowingFromOther(SearchCustomerFollowRequest request) throws BusinessException {
        logger.info("getFollowingFromOther: " + request.toString());
        try {
            List<Long> followings = customerFriendService.getListIdCustomerFollowing(getAuthentication().getUserId());
            CustomerPage followingsOther = customerRepository.getListFollowingFromOther(request);

            followingsOther.getData().forEach(follower -> {
                if (!followings.contains(follower.getId())) {
                    follower.setIsFollowed(false);
                }
            });
            return followingsOther;
        } catch (Exception ex) {
            logger.error("Error during get following customer id: " + request.getIdCustomer() + " \n " + ex);
            return new CustomerPage(0L, 0L, 0L, Lists.newArrayList());
        }
    }

    @Override
    public CustomerPage getFollowerFromOther(SearchCustomerFollowRequest request) throws BusinessException {
        logger.info("getFollowerFromOther: " + request.toString());
        try {
            List<Long> followings = customerFriendService.getListIdCustomerFollowing(getAuthentication().getUserId());
            CustomerPage followers = customerRepository.getListFollowerFromOther(request);

            followers.getData().forEach(follower -> {
                if (!followings.contains(follower.getId())) {
                    follower.setIsFollowed(false);
                }
            });
            return followers;
        } catch (Exception ex) {
            logger.error("Error during get followers customer id: " + request.getIdCustomer() + " \n " + ex);
            return new CustomerPage(0L, 0L, 0L, Lists.newArrayList());
        }
    }

    @Override
    public void saveCustomerFollow(Long idFriend) throws BusinessException {

        Customer friend = getCustomerById(idFriend);
        Long customerId = getAuthentication().getUserId();
        // check follower status between 2 given user
        int count = customerFriendService.countFriendRelationShip(customerId, idFriend);

        if (count >= 1) {
            log.info("Customer (customerId: {}) already followed this person: {}", customerId, friend.getId());
        } else {
            if (!customerId.equals(friend.getId())) {
                customerFriendService.saveCustomerFollow(customerId, friend.getId(), NORMAL.name());
            } else {
                throw new BusinessException(HttpStatus.BAD_REQUEST, ERROR_FOLLOW_ITSELF);
            }
        }
    }

    @Override
    public void unfollowFriend(Long idFriend) throws BusinessException {
        Customer friend = getCustomerById(idFriend);
        Long customerId = getAuthentication().getUserId();
        if (!customerId.equals(friend.getId())) {
            customerFriendService.unfollowFriend(customerId, friend.getId());
        } else {
            throw new BusinessException(HttpStatus.BAD_REQUEST, ERROR_FOLLOW_ITSELF);
        }
    }

    private void validateRegisterPistCustomer(RegisterCustomerPistRequest request) throws BusinessException {
        verifyPhoneNumber(request.getPhone());

        if (request.getStep() == RegisterStep.VALID_INFORMATION_USER.getStep() && StringUtils.isBlank(request.getDisplayName())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, ERROR_DISPLAY_NAME_IS_BLANK);
        } else if (request.getStep() == RegisterStep.VALID_USERNAME.getStep() && StringUtils.isBlank(request.getUsername())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, ERROR_USER_NAME_IS_BLANK);
        }
    }

    private void verifyPhoneNumber(String phone) throws BusinessException {
        if (phone.length() != 10 || !DataUtil.checkPhone(phone)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, ERROR_FORMAT_PHONE_INVALID);
        }
    }

    @Override
    public CustomerProfileOtherDto getCustomerProfileById(Long idCustomer) throws BusinessException {
        Optional<Customer> optionalCustomer = customerRepository.findById(idCustomer);
        if (!optionalCustomer.isPresent()) {
            throw new BusinessException("Customer was not found. Please contact administrator to be supported");
        }
        Customer customer = optionalCustomer.get();
        CustomerFriend existedCustomerFriend = customerFriendRepository.findByIdCustomerAndIdFriend(getAuthentication().getUserId(), idCustomer);
        return CustomerProfileOtherDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .displayName(StringUtils.isBlank(customer.getDisplayName()) ? customer.getName() : customer.getDisplayName())
                .avatar(customer.getAvatar())
                .coverImage(customer.getCoverImage())
                .caption(customer.getCaption())
                .isFeatureProfile(customer.getIsFeatureProfile())
                .fullDes(customer.getFullDes())
                .hasProAccount(customer.getCustStat() == org.common.utils.Constant.CustStat.PRO)
                .hasSyncContact(customer.getHasSyncContact())
                .isKol(customer.getIsKol())
                .createdAt(Timestamp.valueOf(customer.getCreatedAt()))
                .isFollowed(existedCustomerFriend != null)
                .position(customer.getPosition())
                .build();
    }

    @Override
    public CustomerProfileDto getCustomerProfile() throws BusinessException {
        Customer customer = getCustomerById(getAuthentication().getUserId());
        return convertToCustomerProfileDto(customer);
    }

    public Boolean updateCustomerProfile(@Valid CustomerProfileUpdateRequest request) throws BusinessException {
        Customer customer = getCurrentCustomer();

        if (StringUtils.isNotBlank(request.getFullName())) {
            customer.setName(request.getFullName().trim());
        }
        if (StringUtils.isNotBlank(request.getDisplayName())) {
            customer.setDisplayName(request.getDisplayName().trim());
        }
        if (StringUtils.isNotBlank(request.getPosition())) {
            customer.setPosition(request.getPosition().trim());
        }
        if (StringUtils.isNotBlank(request.getEmail())) {
            customer.setEmail(request.getEmail().trim());
        }
        if (StringUtils.isNotBlank(request.getAddress())) {
            verifyAddress(request.getAddress());
            customer.setAddress(request.getAddress().trim());
        }
        if (StringUtils.isNotBlank(request.getBio())) {
            customer.setCaption(request.getBio().trim());
        }
        if (StringUtils.isNotBlank(request.getAvatar())) {
            verifyAvatar(request.getAvatar());
            customer.setAvatar(request.getAvatar());
        }
        customerRepository.save(customer);
        return true;
    }

    private void verifyAvatar(String avatar) throws BusinessException {
        if (avatar.length() > 255) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, ERROR_AVATAR_MAX);
        }
    }

    private void verifyAddress(String address) throws BusinessException {
        if (address.length() > 255) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, ERROR_ADDRESS_MAX);
        }
        if (address.length() < 15) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, ERROR_ADDRESS_MIN);
        }
    }

    private CustomerProfileDto convertToCustomerProfileDto(Customer customer) throws BusinessException {
        return CustomerProfileDto.builder()
                .email(customer.getEmail())
                .address(customer.getAddress())
                .avatar(customer.getAvatar())
                .caption(customer.getCaption())
                .cif(customer.getCif())
                .displayName(customer.getDisplayName())
                .hasProAccount(customer.getCustStat() == org.common.utils.Constant.CustStat.PRO)
                .hasSyncContact(customer.getHasSyncContact())
                .id(customer.getId())
                .isKol(customer.getIsKol())
                .isFeatureProfile(customer.getIsFeatureProfile())
                .fullDes(customer.getFullDes())
                .name(customer.getName())
                .phone(customer.getPhone())
                .position(customer.getPosition())
                .username(customer.getUsername())
                .accountNo(customer.getAccountNo())
                .authDef(customer.getAuthDef())
                .contactAddress(customer.getContactAddress())
                .custStat(customer.getCustStat() != null ? customer.getCustStat().name() : null)
                .acntStat(customer.getAcntStat() != null ? customer.getAcntStat().name() : null)
                .deviceType(customer.getDeviceType())
                .dob(localDateToStringT24(customer.getDob()))
                .fcmToken(customer.getFcmToken())
                .firstLogin(customer.getFirstLogin())
                .gender(customer.getGender())
                .guid(customer.getGuid())
                .identityCardNo(customer.getIdentityCardNo())
                .kolPoint(customer.getKolPoint())
                .openDate(localDateToStringT24(customer.getOpenDate()))
                .build();
    }
}
