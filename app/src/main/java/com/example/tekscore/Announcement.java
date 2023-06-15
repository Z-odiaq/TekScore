package com.example.tekscore;

import android.util.Log;

public class Announcement {
    private String title;
    private String date;
    private String content;

    private String emoji;
    public Announcement(String title, String date, String content, String emoji) {
        this.title = title;
        this.date = date;
        this.content = content;
        this.emoji = emoji;
    }
    public Announcement() {
        // Default constructor required for Firebase deserialization
    }
    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getEmoji() {

        return emoji.equals("info") ? "ℹ️" : emoji.equals("important") ? "❗" : "⚠️";


    }

    public String getContent() {
        return content;
    }
}
