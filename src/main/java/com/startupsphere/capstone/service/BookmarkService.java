package com.startupsphere.capstone.service;

import com.startupsphere.capstone.entity.BookmarkEntity;
import com.startupsphere.capstone.repository.BookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookmarkService {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    // Create
    public BookmarkEntity createBookmark(BookmarkEntity bookmark) {
        return bookmarkRepository.save(bookmark);
    }

    // Read all
    public List<BookmarkEntity> getAllBookmarks() {
        return bookmarkRepository.findAll();
    }

    // Read by ID
    public Optional<BookmarkEntity> getBookmarkById(Integer id) {
        return bookmarkRepository.findById(id);
    }

    // Read by userId
    public List<BookmarkEntity> getBookmarksByUserId(Integer userId) {
        return bookmarkRepository.findByUserId(userId);
    }

    // Update
    public BookmarkEntity updateBookmark(Integer id, BookmarkEntity updatedBookmark) {
        Optional<BookmarkEntity> existingBookmark = bookmarkRepository.findById(id);
        if (existingBookmark.isPresent()) {
            BookmarkEntity bookmark = existingBookmark.get();
            bookmark.setTimestamp(updatedBookmark.getTimestamp());
            bookmark.setUser(updatedBookmark.getUser());
            bookmark.setStartupId(updatedBookmark.getStartupId());
            bookmark.setInvestorId(updatedBookmark.getInvestorId());
            return bookmarkRepository.save(bookmark);
        }
        throw new RuntimeException("Bookmark not found with id: " + id);
    }

    // Delete
    public void deleteBookmark(Integer id) {
        bookmarkRepository.deleteById(id);
    }
}