package org.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.example.entity.CustomerFriend;

import java.util.List;

@Repository
public interface CustomerFriendRepository extends JpaRepository<CustomerFriend, Long>, CustomerFriendRepositoryCustom {

    @Query(nativeQuery = true, value = "SELECT DISTINCT id_friend FROM customer_friend cf WHERE cf.id_customer = ?1 AND cf.deleted_at IS NULL")
    List<Long> findByIdCustomer(Long idCustomer);

    @Query(nativeQuery = true, value = "SELECT id_friend FROM customer_friend cf WHERE cf.id_customer = ?1 AND cf.id_friend IN (?2) AND cf.deleted_at IS NULL")
    List<Long> findByIdCustomerAndListFriends(Long idCustomer, List<Long> idFriends);

    CustomerFriend findByIdCustomerAndIdFriend(Long idCustomer, Long idFriend);

    @Query(nativeQuery = true, value = "select count(*) as count from customer_friend where id_customer=?1 and id_friend=?2 and deleted_at is null ;")
    int countFriendRelationShip(Long idCustomer, Long idFriend);

    @Query(nativeQuery = true, value = "select * from customer_friend where id_customer" +
            " in (select distinct (id_friend) from customer_friend where id_customer=?1 and deleted_at is null)" +
            " and deleted_at is null;")
    List<CustomerFriend> findFriendListByCustomerId(Long idCustomer);
}
