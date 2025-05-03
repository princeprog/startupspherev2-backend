package com.startupsphere.capstone.repository;

import com.startupsphere.capstone.entity.Startup;
import com.startupsphere.capstone.entity.User;
import com.startupsphere.capstone.entity.Views;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewsRepository extends JpaRepository<Views, Long> {
    boolean existsByUserAndStartup(User user, Startup startup);

    @Query("SELECT FUNCTION('MONTH', v.timestamp) AS month, COUNT(v) AS count " +
            "FROM Views v WHERE v.startup.id = :startupId " +
            "GROUP BY FUNCTION('MONTH', v.timestamp) " +
            "ORDER BY month")
    List<Object[]> countViewsByMonthAndStartup(@Param("startupId") Long startupId);

    @Query("SELECT COUNT(v) FROM Views v WHERE v.startup.user.id = :userId")
    long countViewsByStartupOwner(@Param("userId") Integer userId);

    @Query("SELECT MONTH(v.timestamp) AS month, COUNT(v) AS count " +
            "FROM Views v " +
            "WHERE v.startup.user.id = :userId " +
            "GROUP BY MONTH(v.timestamp) " +
            "ORDER BY MONTH(v.timestamp)")
    List<Object[]> countViewsGroupedByMonthForUserOwnedStartups(@Param("userId") Integer userId);
}