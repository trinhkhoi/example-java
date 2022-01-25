package org.example.repository;

import org.example.dto.request.SearchCustomerFollowRequest;
import org.example.dto.response.CustomerPage;
import org.springframework.stereotype.Repository;
import org.common.jpa.SearchInput;

@Repository
public interface CustomerRepositoryCustom {

    CustomerPage getSuggestedCustomer(Long idCustomer, SearchInput searchInput);

    CustomerPage getListFollowing(Long idCustomer, SearchCustomerFollowRequest request);

    CustomerPage getListFollower(Long idCustomer, SearchCustomerFollowRequest request);

    CustomerPage getListFollowingFromOther(SearchCustomerFollowRequest request);

    CustomerPage getListFollowerFromOther(SearchCustomerFollowRequest request);

    CustomerPage getSuggestedCustomerByWatchlist(Long idCustomer, SearchInput searchInput);

}
