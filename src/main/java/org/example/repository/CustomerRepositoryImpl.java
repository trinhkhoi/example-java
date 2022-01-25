package org.example.repository;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.example.dto.response.CustomerDto;
import org.example.dto.response.CustomerPage;
import org.common.jpa.NativeQueryMapper;
import org.common.jpa.SearchInput;
import org.example.dto.request.SearchCustomerFollowRequest;
import org.example.util.Constant;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

public class CustomerRepositoryImpl implements CustomerRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public CustomerPage getSuggestedCustomer(Long idCustomer, SearchInput searchInput) {

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT y.id_friend ");
        sb.append(" FROM customer_friend x ");
        sb.append(" INNER JOIN customer_friend y ");
        sb.append(" ON y.id_customer = x.id_friend ");
        sb.append(" AND y.id_friend <> x.id_customer ");
        sb.append(" AND y.deleted_at is null and x.deleted_at is null ");
        sb.append(" LEFT JOIN customer_friend z ");
        sb.append(" ON z.id_friend = y.id_friend ");
        sb.append(" AND z.id_customer = x.id_customer ");
        sb.append(" WHERE x.id_customer = :idCustomer) s");
        sb.append(" ON c.id = s.id_friend AND c.deleted_at is null ");

        StringBuilder sbCount = new StringBuilder();
        sbCount.append("SELECT count(1) from customer c INNER JOIN ( ");
        sbCount.append(sb.toString());

        Query counrQuery = em.createNativeQuery(sbCount.toString());
        counrQuery.setParameter("idCustomer", idCustomer);
        BigInteger totalElement = (BigInteger) counrQuery.getSingleResult();
        if (totalElement == null || totalElement.longValue() == 0L) {
            return new CustomerPage(0L, searchInput.getPage(), searchInput.getPageSize(), Lists.newArrayList());
        }

        StringBuilder querySql = new StringBuilder();
        querySql.append("SELECT c.* from customer c INNER JOIN ( ");
        querySql.append(sb.toString());
        querySql.append(" limit :limit offset :offset ");
        Query query = em.createNativeQuery(querySql.toString());

        Long offset = (searchInput.getPage() - 1) * searchInput.getPageSize();
        Long limit = searchInput.getPageSize();
        query.setParameter("idCustomer", idCustomer);
        query.setParameter("offset", offset);
        query.setParameter("limit", limit);
        List<CustomerDto> data = NativeQueryMapper.map(query.getResultList(), CustomerDto.class);
        return new CustomerPage(totalElement.longValue(), searchInput.getPage(), searchInput.getPageSize(), data);
    }

    @Override
    public CustomerPage getSuggestedCustomerByWatchlist(Long idCustomer, SearchInput searchInput) {
        StringBuilder sb = new StringBuilder(Constant.sqlNativeForCustomerDTO);
        sb.append(" inner join ");
        sb.append(" (select w1.customer_id, ws1.stock_code from watchlist_stock ws1 inner join watchlist w1 on ws1.watchlist_id = w1.id  and w1.customer_id  <> :idCustomer) rs1 ");
        sb.append(" on c.id = rs1.customer_id ");
        sb.append(" inner join ");
        sb.append(" (select ws2.stock_code from watchlist_stock ws2 inner join watchlist w2 on ws2.watchlist_id  = w2.id and w2.customer_id = :idCustomer) rs2 ");
        sb.append(" on rs1.stock_code = rs2.stock_code ");

        StringBuilder sbCount = new StringBuilder();
        sbCount.append("select count(1) from (");
        sbCount.append(sb.toString());
        sbCount.append(" ) s");

        Query counrQuery = em.createNativeQuery(sbCount.toString());
        counrQuery.setParameter("idCustomer", idCustomer);
        BigInteger totalElement = (BigInteger) counrQuery.getSingleResult();
        if (totalElement == null || totalElement.longValue() == 0L) {
            return new CustomerPage(0L, searchInput.getPage(), searchInput.getPageSize(), Lists.newArrayList());
        }

        StringBuilder querySql = new StringBuilder();
        querySql.append(sb.toString());
        querySql.append(" limit :limit offset :offset ");
        Query query = em.createNativeQuery(querySql.toString());

        Long offset = (searchInput.getPage() - 1) * searchInput.getPageSize();
        Long limit = searchInput.getPageSize();
        query.setParameter("idCustomer", idCustomer);
        query.setParameter("offset", offset);
        query.setParameter("limit", limit);
        List<CustomerDto> data = NativeQueryMapper.map(query.getResultList(), CustomerDto.class);
        return new CustomerPage(totalElement.longValue(), searchInput.getPage(), searchInput.getPageSize(), data);
    }

    @Override
    public CustomerPage getListFollowing(Long idCustomer, SearchCustomerFollowRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        sb.append(" INNER JOIN customer_friend cf ");
        sb.append(" ON c.id = cf.id_friend");
        sb.append(" WHERE cf.id_customer = :idCustomer ");
        if (StringUtils.isNotBlank(request.getFullName())) {
            sb.append(" AND c.name like '%" + request.getFullName() + "%' ");
        }
        sb.append(" AND c.deleted_at is null AND cf.deleted_at is null ");

        StringBuilder sbCount = new StringBuilder();
        sbCount.append("SELECT count(1) FROM customer c ");
        sbCount.append(sb.toString());

        Query counrQuery = em.createNativeQuery(sbCount.toString());
        counrQuery.setParameter("idCustomer", idCustomer);
        BigInteger totalElement = (BigInteger) counrQuery.getSingleResult();
        if (totalElement == null || totalElement.longValue() == 0L) {
            return new CustomerPage(0L, request.getPage(), request.getPageSize(), Lists.newArrayList());
        }

        StringBuilder querySql = new StringBuilder(Constant.sqlNativeForCustomerDTO);
        querySql.append(sb);
        querySql.append(" order by cf.created_at desc ");
        querySql.append(" limit :limit offset :offset ");
        Query query = em.createNativeQuery(querySql.toString());

        Long offset = (request.getPage() - 1) * request.getPageSize();
        Long limit = request.getPageSize();
        query.setParameter("idCustomer", idCustomer);
        query.setParameter("offset", offset);
        query.setParameter("limit", limit);
        List<CustomerDto> data = NativeQueryMapper.map(query.getResultList(), CustomerDto.class);
        return new CustomerPage(totalElement.longValue(), request.getPage(), request.getPageSize(), data);
    }

    @Override
    public CustomerPage getListFollower(Long idCustomer, SearchCustomerFollowRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        sb.append(" INNER JOIN customer_friend cf ");
        sb.append(" ON c.id = cf.id_customer ");
        sb.append(" WHERE cf.id_friend = :idCustomer ");
        if (StringUtils.isNotBlank(request.getFullName())) {
            sb.append(" AND c.name like '%" + request.getFullName() + "%' ");
        }
        sb.append(" AND c.deleted_at is null AND cf.deleted_at is null ");

        StringBuilder sbCount = new StringBuilder();
        sbCount.append("SELECT count(1) FROM customer c ");
        sbCount.append(sb.toString());

        Query counrQuery = em.createNativeQuery(sbCount.toString());
        counrQuery.setParameter("idCustomer", idCustomer);
        BigInteger totalElement = (BigInteger) counrQuery.getSingleResult();
        if (totalElement == null || totalElement.longValue() == 0L) {
            return new CustomerPage(0L, request.getPage(), request.getPageSize(), Lists.newArrayList());
        }

        StringBuilder querySql = new StringBuilder(Constant.sqlNativeForCustomerDTO);
        querySql.append(sb);
        querySql.append(" order by cf.created_at desc ");
        querySql.append(" limit :limit offset :offset ");
        Query query = em.createNativeQuery(querySql.toString());

        Long offset = (request.getPage() - 1) * request.getPageSize();
        Long limit = request.getPageSize();
        query.setParameter("idCustomer", idCustomer);
        query.setParameter("offset", offset);
        query.setParameter("limit", limit);
        List<CustomerDto> data = NativeQueryMapper.map(query.getResultList(), CustomerDto.class);
        return new CustomerPage(totalElement.longValue(), request.getPage(), request.getPageSize(), data);
    }

    @Override
    public CustomerPage getListFollowingFromOther(SearchCustomerFollowRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        sb.append(" INNER JOIN customer_friend cf ");
        sb.append(" ON c.id = cf.id_friend");
        sb.append(" WHERE cf.id_customer = :idCustomer ");
        if (StringUtils.isNotBlank(request.getFullName())) {
            sb.append(" AND c.name like '%" + request.getFullName() + "%' ");
        }
        sb.append(" AND c.deleted_at is null AND cf.deleted_at is null ");

        StringBuilder sbCount = new StringBuilder();
        sbCount.append("SELECT count(1) FROM customer c ");
        sbCount.append(sb.toString());

        Query counrQuery = em.createNativeQuery(sbCount.toString());
        counrQuery.setParameter("idCustomer", request.getIdCustomer());
        BigInteger totalElement = (BigInteger) counrQuery.getSingleResult();
        if (totalElement == null || totalElement.longValue() == 0L) {
            return new CustomerPage(0L, request.getPage(), request.getPageSize(), Lists.newArrayList());
        }

        StringBuilder querySql = new StringBuilder(Constant.sqlNativeForCustomerDTO);
        querySql.append(sb);
        querySql.append(" order by cf.created_at desc ");
        querySql.append(" limit :limit offset :offset ");
        Query query = em.createNativeQuery(querySql.toString());

        Long offset = (request.getPage() - 1) * request.getPageSize();
        Long limit = request.getPageSize();
        query.setParameter("idCustomer", request.getIdCustomer());
        query.setParameter("offset", offset);
        query.setParameter("limit", limit);
        List<CustomerDto> data = NativeQueryMapper.map(query.getResultList(), CustomerDto.class);
        return new CustomerPage(totalElement.longValue(), request.getPage(), request.getPageSize(), data);
    }

    @Override
    public CustomerPage getListFollowerFromOther(SearchCustomerFollowRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        sb.append(" INNER JOIN customer_friend cf ");
        sb.append(" ON c.id = cf.id_customer ");
        sb.append(" WHERE cf.id_friend = :idCustomer ");
        if (StringUtils.isNotBlank(request.getFullName())) {
            sb.append(" AND c.name like '%" + request.getFullName() + "%' ");
        }
        sb.append(" AND c.deleted_at is null AND cf.deleted_at is null ");

        StringBuilder sbCount = new StringBuilder();
        sbCount.append("SELECT count(1) FROM customer c ");
        sbCount.append(sb.toString());

        Query counrQuery = em.createNativeQuery(sbCount.toString());
        counrQuery.setParameter("idCustomer", request.getIdCustomer());
        BigInteger totalElement = (BigInteger) counrQuery.getSingleResult();
        if (totalElement == null || totalElement.longValue() == 0L) {
            return new CustomerPage(0L, request.getPage(), request.getPageSize(), Lists.newArrayList());
        }

        StringBuilder querySql = new StringBuilder(Constant.sqlNativeForCustomerDTO);
        querySql.append(sb);
        querySql.append(" order by cf.created_at desc ");
        querySql.append(" limit :limit offset :offset ");
        Query query = em.createNativeQuery(querySql.toString());

        Long offset = (request.getPage() - 1) * request.getPageSize();
        Long limit = request.getPageSize();
        query.setParameter("idCustomer", request.getIdCustomer());
        query.setParameter("offset", offset);
        query.setParameter("limit", limit);
        List<CustomerDto> data = NativeQueryMapper.map(query.getResultList(), CustomerDto.class);
        return new CustomerPage(totalElement.longValue(), request.getPage(), request.getPageSize(), data);
    }
}
