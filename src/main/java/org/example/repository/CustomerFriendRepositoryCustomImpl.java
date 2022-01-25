package org.example.repository;

import com.google.common.collect.Lists;
import org.example.dto.response.CustomerDto;
import org.example.dto.response.CustomerPage;
import org.common.jpa.NativeQueryMapper;
import org.common.jpa.SearchInput;
import org.example.util.Constant;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

public class CustomerFriendRepositoryCustomImpl implements CustomerFriendRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public CustomerPage getSuggestedCustomer(List<Long> idFriends, Long idCustomer, SearchInput searchInput) {
        StringBuilder sqlBuilder = new StringBuilder(Constant.sqlNativeForCustomerDTO);
        sqlBuilder.append(" where c.id in (");
        sqlBuilder.append(" select distinct (id_customer) from customer_friend where id_customer" +
                " in (select distinct (id_friend) from customer_friend where id_customer in (:idFriends) and deleted_at is null)" +
                " and id_friend in (:idFriends)" +
                " and id_customer <> :idCustomer" +
                " and deleted_at is null");
        sqlBuilder.append(" )");

        StringBuilder countBuilder = new StringBuilder("select count(*) as count from ( ");
        countBuilder.append(sqlBuilder);
        countBuilder.append(" ) s");
        Query countQuery = em.createNativeQuery(countBuilder.toString());
        countQuery.setParameter("idFriends", idFriends);
        countQuery.setParameter("idCustomer", idCustomer);
        BigInteger totalElement = (BigInteger) countQuery.getSingleResult();
        if (totalElement == null || totalElement.longValue() == 0L) {
            return new CustomerPage(0L, searchInput.getPage(), searchInput.getPageSize(), Lists.newArrayList());
        }
        sqlBuilder.append(" limit :limit offset :offset ");
        Query query = em.createNativeQuery(sqlBuilder.toString());

        Long offset = (searchInput.getPage() - 1) * searchInput.getPageSize();
        Long limit = searchInput.getPageSize();
        query.setParameter("idFriends", idFriends);
        query.setParameter("idCustomer", idCustomer);
        query.setParameter("offset", offset);
        query.setParameter("limit", limit);
        List<CustomerDto> data = NativeQueryMapper.map(query.getResultList(), CustomerDto.class);
        return new CustomerPage(totalElement.longValue(), searchInput.getPage(), searchInput.getPageSize(), data);
    }

    @Override
    public CustomerPage getDefaultSuggestedFriends(Long idCustomer, SearchInput searchInput) {
        StringBuilder sqlBuilder = new StringBuilder(Constant.sqlNativeForCustomerDTO);
        sqlBuilder.append(" join customer_statistic cs on c.id = cs.id_customer where ")
                .append(" c.id not in (select id_friend from customer_friend where id_customer=:idCustomer) and c.id <> :idCustomer");
        StringBuilder countBuilder = new StringBuilder("select count(*) as count from ( ");
        countBuilder.append(sqlBuilder);
        countBuilder.append(" ) s");
        Query countQuery = em.createNativeQuery(countBuilder.toString());
        countQuery.setParameter("idCustomer", idCustomer);
        BigInteger totalElement = (BigInteger) countQuery.getSingleResult();
        if (totalElement == null || totalElement.longValue() == 0L) {
            return new CustomerPage(0L, searchInput.getPage(), searchInput.getPageSize(), Lists.newArrayList());
        }
        sqlBuilder.append(" order by total_follower desc" +
                " limit :limit offset :offset ");
        Query query = em.createNativeQuery(sqlBuilder.toString());

        Long offset = (searchInput.getPage() - 1) * searchInput.getPageSize();
        Long limit = searchInput.getPageSize();
        query.setParameter("idCustomer", idCustomer);
        query.setParameter("offset", offset);
        query.setParameter("limit", limit);
        List<CustomerDto> data = NativeQueryMapper.map(query.getResultList(), CustomerDto.class);
        return new CustomerPage(totalElement.longValue(), searchInput.getPage(), searchInput.getPageSize(), data);
    }
}
