package org.example.repository;

import org.example.dto.response.CustomerPage;
import org.springframework.stereotype.Repository;
import org.common.jpa.SearchInput;

import java.util.List;

@Repository
public interface CustomerFriendRepositoryCustom {
    CustomerPage getSuggestedCustomer(List<Long> idFriends, Long idCustomer, SearchInput searchInput);

    CustomerPage getDefaultSuggestedFriends(Long idCustomer, SearchInput searchInput);

}
