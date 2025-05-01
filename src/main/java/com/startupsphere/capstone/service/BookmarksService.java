package com.startupsphere.capstone.service;

import com.startupsphere.capstone.entity.Bookmarks;
import com.startupsphere.capstone.entity.User;
import com.startupsphere.capstone.repository.BookmarksRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@Service
public class BookmarksService {

    private final BookmarksRepository bookmarksRepository;

    @Autowired
    public BookmarksService(BookmarksRepository bookmarksRepository) {
        this.bookmarksRepository = bookmarksRepository;
    }

    public Bookmarks createBookmark(Bookmarks bookmark) {
        return bookmarksRepository.save(bookmark);
    }

    public List<Bookmarks> getBookmarksByUser(User user) {
        return bookmarksRepository.findByUser(user);
    }

    public List<Bookmarks> getAllBookmarks() {
        return bookmarksRepository.findAll();
    }

    public boolean deleteBookmark(Long id) {
        Optional<Bookmarks> bookmark = bookmarksRepository.findById(id);
        if (bookmark.isPresent()) {
            System.out.println("Deleting bookmark with id: " + id); // Add logging
            bookmarksRepository.delete(bookmark.get());
            return true;
        } else {
            System.out.println("Bookmark with id " + id + " not found"); // Add logging
            return false;
        }
    }
}
