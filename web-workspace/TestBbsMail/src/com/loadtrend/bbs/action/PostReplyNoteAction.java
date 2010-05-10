package com.loadtrend.bbs.action;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.loadtrend.bbs.info.PostReplyNoteInfo;
import com.loadtrend.bbs.po.Note;
import com.loadtrend.bbs.po.ReplyNote;
import com.loadtrend.bbs.service.ReplyNoteManager;

public class PostReplyNoteAction extends BaseFormController
{
	private ReplyNoteManager replyNoteManager = null;
	
	public void setReplyNoteManager(ReplyNoteManager replyNoteManager) {
		this.replyNoteManager = replyNoteManager;
	}

	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception
	{
		PostReplyNoteInfo postReplyNoteInfo = (PostReplyNoteInfo) command;
		
		super.logger.debug( "postReplyNoteInfo:" + postReplyNoteInfo );
		
		// Convert the info to Note.
		ReplyNote  replyNote = new ReplyNote();
		replyNote.setAuthor( postReplyNoteInfo.getAuthor() );
		replyNote.setEmail(  postReplyNoteInfo.getEmail() );
		replyNote.setContent( postReplyNoteInfo.getContent() );
		replyNote.setPostTime( new Timestamp( System.currentTimeMillis() ) );
		
		// Add replynote.
		String noteId = request.getParameter( "noteid" );
		
//      Method 1 to save many
//		Note note = super.getNoteManager().getNote( noteId );
//		note.getReplyNotes().add( replyNote );
//		replyNote.setNote( note );
//		super.getNoteManager().saveNote( note );
		
//      Method 2 to save many
		Note note = super.getNoteManager().getNote( noteId );
		replyNote.setNote( note );
		this.replyNoteManager.saveReplyNote( replyNote );
		
		return new ModelAndView( getSuccessView() );
	}
}
