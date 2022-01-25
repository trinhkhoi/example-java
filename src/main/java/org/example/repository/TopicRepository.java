package org.example.repository;

import org.example.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    @Modifying
    @Transactional(noRollbackFor = Exception.class)
    @Query(value = "UPDATE Topic t SET t.totalSelected = t.totalSelected + 1, t.updatedAt = current_timestamp WHERE t.topicCode = :topicCode")
    void increaseTotalSelected(@Param("topicCode") final String topicCode);

    @Modifying
    @Transactional(noRollbackFor = Exception.class)
    @Query(value = "UPDATE Topic t SET t.totalSelected = t.totalSelected - 1, t.updatedAt = current_timestamp WHERE t.topicCode = :topicCode")
    void decreaseTotalSelected(@Param("topicCode") final String topicCode);
}
