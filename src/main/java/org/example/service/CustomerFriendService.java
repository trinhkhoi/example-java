package org.example.service;

import org.example.dto.response.CustomerPage;
import org.example.entity.CustomerFriend;
import org.common.exception.BusinessException;
import org.common.jpa.SearchInput;

import java.util.List;

public interface CustomerFriendService {
    List<Long> getListIdCustomerFollowing(Long idCustomer) throws BusinessException;

    List<Long> getListFollowingByListFriends(Long idCustomer, List<Long> idFriends) throws BusinessException;

    void saveCustomerFollow(Long idCustomer, Long idFriend, String source) throws BusinessException;

    boolean unfollowFriend(Long idCustomer, Long idFriend) throws BusinessException;

    List<CustomerFriend> findFriendListByCustomerId(Long idCustomer);

    CustomerPage getSuggestedCustomer(List<Long> idFriends, Long idCustomer, SearchInput searchInput);

    int countFriendRelationShip(Long idCustomer, Long idFriend);
}
