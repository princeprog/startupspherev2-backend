package com.startupsphere.capstone.service;

import com.startupsphere.capstone.entity.Bookmarks;
import com.startupsphere.capstone.repository.BookmarksRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

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

    public List<Bookmarks> getAllBookmarks() {
        return bookmarksRepository.findAll();
    }

    public void deleteBookmark(Long id) {
        Bookmarks bookmark = bookmarksRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Bookmark not found with id: " + id));
        bookmarksRepository.delete(bookmark);
    }
    
}
