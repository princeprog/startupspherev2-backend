package com.startupsphere.capstone.repository;

import com.startupsphere.capstone.entity.BookmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Integer> {
    List<BookmarkEntity> findByUserId(Integer userId);
}