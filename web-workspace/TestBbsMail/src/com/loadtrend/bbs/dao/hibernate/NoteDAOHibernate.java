package com.loadtrend.bbs.dao.hibernate;

import java.util.List;

import org.hibernate.Hibernate;

import com.loadtrend.bbs.dao.NoteDAO;
import com.loadtrend.bbs.po.Note;

public class NoteDAOHibernate extends BaseDAOHibernate implements NoteDAO {
	public void saveNote(Note note) {
		getHibernateTemplate().saveOrUpdate(note);
	}

	public Note getNote(String noteId) {
		Note note = (Note) getHibernateTemplate().get(Note.class, noteId);
        // Force to get children data, without lazy loading.
        Hibernate.initialize(note.getReplyNotes());
        return note;
	}

	public List getNotes() {
		return getHibernateTemplate().find("from Note");
	}

	public void removeNote(Note note) {
		getHibernateTemplate().delete(note);
	}
}
