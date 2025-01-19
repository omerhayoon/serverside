package com.dev.repository;
import com.dev.models.Review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    void deleteByUsername(String username);
    boolean existsByUsername(String username);
}

//משמש אותנו כדי לפנות למסד הנתונים
//יש לו המון פונקציות מוכנות כמו findall,findbyid, save,delete וכו
//ניתן להוסף פונצקיות מותאמות נניח:
//List<Review> findByUserName(String username);
//boolean existsByUsername(String username);
