package com.startupsphere.capstone.service;

import com.startupsphere.capstone.entity.ViewEntity;
import com.startupsphere.capstone.repository.ViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ViewService {

    @Autowired
    private ViewRepository viewRepository;

    // Create
    public ViewEntity createView(ViewEntity view) {
        return viewRepository.save(view);
    }

    // Read all
    public List<ViewEntity> getAllViews() {
        return viewRepository.findAll();
    }

    // Read by ID
    public Optional<ViewEntity> getViewById(Integer id) {
        return viewRepository.findById(id);
    }

    // Read by userId
    public List<ViewEntity> getViewsByUserId(Integer userId) {
        return viewRepository.findByUserId(userId);
    }

    // Update
    public ViewEntity updateView(Integer id, ViewEntity updatedView) {
        Optional<ViewEntity> existingView = viewRepository.findById(id);
        if (existingView.isPresent()) {
            ViewEntity view = existingView.get();
            view.setTimestamp(updatedView.getTimestamp());
            view.setUser(updatedView.getUser());
            view.setStartupId(updatedView.getStartupId());
            view.setInvestorId(updatedView.getInvestorId());
            return viewRepository.save(view);
        }
        throw new RuntimeException("View not found with id: " + id);
    }

    // Delete
    public void deleteView(Integer id) {
        viewRepository.deleteById(id);
    }
}