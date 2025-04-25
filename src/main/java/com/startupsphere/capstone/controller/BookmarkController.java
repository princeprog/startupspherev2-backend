package com.startupsphere.capstone.controller;

import com.startupsphere.capstone.entity.BookmarkEntity;
import com.startupsphere.capstone.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    @Autowired
    private BookmarkService bookmarkService;

    // Create
    @PostMapping
    public ResponseEntity<BookmarkEntity> createBookmark(@RequestBody BookmarkEntity bookmark) {
        BookmarkEntity createdBookmark = bookmarkService.createBookmark(bookmark);
        return ResponseEntity.status(201).body(createdBookmark);
    }

    // Read all
    @GetMapping
    public ResponseEntity<List<BookmarkEntity>> getAllBookmarks() {
        List<BookmarkEntity> bookmarks = bookmarkService.getAllBookmarks();
        return ResponseEntity.ok(bookmarks);
    }

    // Read by ID
    @GetMapping("/{id}")
    public ResponseEntity<BookmarkEntity> getBookmarkById(@PathVariable Integer id) {
        Optional<BookmarkEntity> bookmark = bookmarkService.getBookmarkById(id);
        return bookmark.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.status(404).build());
    }

    // Read by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookmarkEntity>> getBookmarksByUserId(@PathVariable Integer userId) {
        List<BookmarkEntity> bookmarks = bookmarkService.getBookmarksByUserId(userId);
        return ResponseEntity.ok(bookmarks);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<BookmarkEntity> updateBookmark(@PathVariable Integer id, @RequestBody BookmarkEntity bookmark) {
        BookmarkEntity updatedBookmark = bookmarkService.updateBookmark(id, bookmark);
        return ResponseEntity.ok(updatedBookmark);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable Integer id) {
        bookmarkService.deleteBookmark(id);
        return ResponseEntity.status(204).build();
    }
}