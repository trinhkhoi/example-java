package org.example.service;

import org.example.dto.response.CustomerInfo;
import org.example.dto.response.CustomerPage;
import org.example.dto.response.CustomerProfileDto;
import org.example.dto.response.CustomerProfileOtherDto;
import org.example.entity.Customer;
import org.common.exception.BusinessException;
import org.common.jpa.SearchInput;
import org.example.dto.request.CustomerProfileUpdateRequest;
import org.example.dto.request.RegisterCustomerPistRequest;
import org.example.dto.request.SearchCustomerFollowRequest;
import org.pist.dto.response.*;

import javax.validation.Valid;
import java.util.List;

public interface CustomerService {

    Customer getCurrentCustomer() throws BusinessException;

    Customer getCustomerById(Long idCustomer) throws BusinessException;

    List<Customer> findAllByIdIsIn(List<Long> ids);

    List<Customer> getAllByIdIsIn(List<String> ids);

    List<CustomerInfo> getCustomerInfoByIds(List<String> ids);

    void createNewPistCustomer(RegisterCustomerPistRequest request) throws BusinessException;

    List<Customer> getLatestCustomerSubscribeTheme(Long idCustomer, String themeCode) throws BusinessException;

    CustomerPage getSuggestedFriends(SearchInput searchInput) throws BusinessException;

    CustomerPage getFollowing(SearchCustomerFollowRequest request) throws BusinessException;

    CustomerPage getFollower(SearchCustomerFollowRequest request) throws BusinessException;

    CustomerPage getFollowingFromOther(SearchCustomerFollowRequest request) throws BusinessException;

    CustomerPage getFollowerFromOther(SearchCustomerFollowRequest request) throws BusinessException;

    void saveCustomerFollow(Long idFriend) throws BusinessException;

    void unfollowFriend(Long idFriend) throws BusinessException;

    CustomerProfileOtherDto getCustomerProfileById(Long idCustomer) throws BusinessException;

    CustomerProfileDto getCustomerProfile() throws BusinessException;

    Boolean updateCustomerProfile(@Valid CustomerProfileUpdateRequest request) throws BusinessException;
}
