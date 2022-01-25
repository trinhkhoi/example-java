package org.example.repository;


import org.example.entity.CustomerDefaultAvatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerDefaultAvatarRepository extends JpaRepository<CustomerDefaultAvatar, Long> {
    @Query("Select c.avatarUrl from CustomerDefaultAvatar c where c.firstLetter = :firstLetter")
    List<String> findAllAvatarUrlByLetter(String firstLetter);
}
