package com.startupsphere.capstone.repository;

import com.startupsphere.capstone.entity.Bookmarks;
import com.startupsphere.capstone.entity.Investor;
import com.startupsphere.capstone.entity.Startup;
import com.startupsphere.capstone.entity.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarksRepository extends JpaRepository<Bookmarks, Long> {
    List<Bookmarks> findByUser(User user);
    Optional<Bookmarks> findByUserAndStartupAndInvestor(User user, Startup startup, Investor investor);
    long countByStartup_Id(Long startupId);
}