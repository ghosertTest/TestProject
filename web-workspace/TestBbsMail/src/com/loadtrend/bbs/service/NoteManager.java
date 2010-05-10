package com.loadtrend.bbs.service;

import java.util.List;

import com.loadtrend.bbs.dao.NoteDAO;
import com.loadtrend.bbs.po.Note;

public interface NoteManager {

	public void setNoteDAO(NoteDAO noteDAO);

	public void saveNote(Note note);

	public Note getNote(String noteId);

	public List getNotes();

	public void removeNote(Note note);
}