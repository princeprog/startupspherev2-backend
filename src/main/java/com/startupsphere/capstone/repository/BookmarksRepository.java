package com.startupsphere.capstone.repository;

import com.startupsphere.capstone.entity.Bookmarks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarksRepository extends JpaRepository<Bookmarks, Long> {
}
