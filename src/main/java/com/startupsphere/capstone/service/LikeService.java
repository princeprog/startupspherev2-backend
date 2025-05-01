package com.startupsphere.capstone.service;

import com.startupsphere.capstone.entity.Like;
import com.startupsphere.capstone.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    public String toggleLike(Like like) {
        if (like.getStartup() != null) {
            Optional<Like> existingLike = likeRepository.findByUserIdAndStartupId(
                    like.getUser().getId(), like.getStartup().getId());
            if (existingLike.isPresent()) {
                likeRepository.delete(existingLike.get());
                return "Like removed";
            }
        }

        if (like.getInvestor() != null) {
            Optional<Like> existingLike = likeRepository.findByUserIdAndInvestor_InvestorId(
                    like.getUser().getId(), like.getInvestor().getInvestorId());
            if (existingLike.isPresent()) {
                // If the like exists, delete it (unlike)
                likeRepository.delete(existingLike.get());
                return "Like removed";
            }
        }

        likeRepository.save(like);
        return "Like added";
    }

    public List<Like> getAllLikes() {
        return likeRepository.findAll();
    }

    public Optional<Like> getLikeById(Long id) {
        return likeRepository.findById(id);
    }

    public void deleteLike(Long id) {
        likeRepository.deleteById(id);
    }

    public long getLikeCountByStartupId(Long startupId) {
        return likeRepository.countByStartupId(startupId);
    }

}