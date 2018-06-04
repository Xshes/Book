package com.example.book.Entity;

/**
 * Created by youzi on 2018/6/4.
 */

public class CreateBook {
    public String tag;
    public String author;
    public byte[] cover;
    public String bookName;
    public String account;
    public CreateBook(String BookTag,String BookAuthor,byte[] BookCover,String BookName,String acc)
    {
        this.tag=BookTag;
        this.author=BookAuthor;
        this.cover=BookCover;
        this.bookName=BookName;
        this.account=acc;
    }
}
