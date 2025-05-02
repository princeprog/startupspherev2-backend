package com.startupsphere.capstone.service;

import com.startupsphere.capstone.entity.Bookmarks;
import com.startupsphere.capstone.entity.User;
import com.startupsphere.capstone.repository.BookmarksRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public List<Bookmarks> getBookmarksByUser(User user) {
        return bookmarksRepository.findByUser(user);
    }

    public List<Bookmarks> getAllBookmarks() {
        return bookmarksRepository.findAll();
    }

    @Transactional
    public boolean deleteBookmark(Long id) {
        try {
            if (bookmarksRepository.existsById(id)) {
                System.out.println("Deleting bookmark with id: " + id);
                Bookmarks bookmark = bookmarksRepository.findById(id).orElse(null);
                if (bookmark != null) {
                    // Remove the bookmark from the user's list
                    if (bookmark.getUser() != null) {
                        bookmark.getUser().getBookmarks().remove(bookmark);
                    }
                    // Remove the bookmark from the startup's list
                    if (bookmark.getStartup() != null) {
                        bookmark.getStartup().getBookmarks().remove(bookmark);
                    }
                    // Remove the bookmark from the investor's list
                    if (bookmark.getInvestor() != null) {
                        bookmark.getInvestor().getBookmarks().remove(bookmark);
                    }
                    bookmarksRepository.delete(bookmark);
                    return true;
                }
            }
            System.out.println("Bookmark with id " + id + " not found");
            return false;
        } catch (Exception e) {
            System.err.println("Error deleting bookmark: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public long getBookmarkCountByStartupId(Long startupId) {
        return bookmarksRepository.countByStartup_Id(startupId);
    }
}
