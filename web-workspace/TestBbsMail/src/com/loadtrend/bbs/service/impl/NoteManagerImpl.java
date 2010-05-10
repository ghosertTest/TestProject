package com.loadtrend.bbs.service.impl;

import java.util.List;

import com.loadtrend.bbs.dao.NoteDAO;
import com.loadtrend.bbs.po.Note;
import com.loadtrend.bbs.service.NoteManager;

public class NoteManagerImpl extends BaseManager implements NoteManager
{
	private NoteDAO noteDAO = null;
	
    /**
	 * @param noteDAO The noteDAO to set.
	 */
	public void setNoteDAO(NoteDAO noteDAO) {
		this.noteDAO = noteDAO;
	}

	public void saveNote( Note note )
    {
        noteDAO.saveNote( note );
    }
    
    public Note getNote( String noteId )
    {
    	return noteDAO.getNote( noteId );
    }
    
    public List getNotes()
    {
    	return noteDAO.getNotes();
    }
    
    public void removeNote( Note note )
    {
    	noteDAO.removeNote( note );
    }
}
