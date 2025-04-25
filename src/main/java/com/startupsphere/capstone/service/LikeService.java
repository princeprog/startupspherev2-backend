package com.startupsphere.capstone.service;

import com.startupsphere.capstone.entity.LikeEntity;
import com.startupsphere.capstone.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    // Create
    public LikeEntity createLike(LikeEntity like) {
        return likeRepository.save(like);
    }

    // Read all
    public List<LikeEntity> getAllLikes() {
        return likeRepository.findAll();
    }

    // Read by ID
    public Optional<LikeEntity> getLikeById(Integer id) {
        return likeRepository.findById(id);
    }

    // Read by userId
    public List<LikeEntity> getLikesByUserId(Integer userId) {
        return likeRepository.findByUserId(userId);
    }

    // Update
    public LikeEntity updateLike(Integer id, LikeEntity updatedLike) {
        Optional<LikeEntity> existingLike = likeRepository.findById(id);
        if (existingLike.isPresent()) {
            LikeEntity like = existingLike.get();
            like.setTimestamp(updatedLike.getTimestamp());
            like.setUser(updatedLike.getUser());
            like.setStartupId(updatedLike.getStartupId());
            like.setInvestorId(updatedLike.getInvestorId());
            return likeRepository.save(like);
        }
        throw new RuntimeException("Like not found with id: " + id);
    }

    // Delete
    public void deleteLike(Integer id) {
        likeRepository.deleteById(id);
    }
}