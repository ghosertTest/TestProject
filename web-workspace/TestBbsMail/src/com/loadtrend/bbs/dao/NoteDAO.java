package com.loadtrend.bbs.dao;

import java.util.List;

import com.loadtrend.bbs.po.Note;

public interface NoteDAO extends DAO
{
    public void saveNote( Note note );
    
    public Note getNote( String noteId );
    
    public List getNotes();
    
    public void removeNote( Note note );
}
