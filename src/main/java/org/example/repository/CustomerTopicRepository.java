package org.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.example.entity.CustomerTopic;

import java.util.List;

@Repository
public interface CustomerTopicRepository extends JpaRepository<CustomerTopic, Long> {

    @Query(nativeQuery = true, value = "SELECT topic_code FROM customer_topic WHERE id_customer = ?1 and topic_code in (?2)")
    List<String> findListCustomerTopics(Long idCustomer, List<String> topicCodes);

    @Modifying
    @Transactional(noRollbackFor = Exception.class)
    @Query(nativeQuery = true, value = "UPDATE customer_topic SET deleted_at = NOW() WHERE id_customer = ?1 and topic_code in (?2)")
    void deleteByIdCustomerAndTopicCode(Long idCustomer, List<String> topicCodes);

    List<CustomerTopic> findAllByIdCustomer(Long idCustomer);
}
