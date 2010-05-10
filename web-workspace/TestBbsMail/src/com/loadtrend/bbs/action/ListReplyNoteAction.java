package com.loadtrend.bbs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.loadtrend.bbs.info.NoteInfo;
import com.loadtrend.bbs.info.ReplyNoteInfo;
import com.loadtrend.bbs.po.Note;
import com.loadtrend.bbs.po.ReplyNote;
import com.loadtrend.bbs.service.NoteManager;

public class ListReplyNoteAction implements Controller
{
	private String pageView = null;
	
	private String errorView = null;

    private NoteManager noteManager = null;
    
	/**
	 * @return Returns the errorView.
	 */
	public String getErrorView() {
		return errorView;
	}

	/**
	 * @param errorView The errorView to set.
	 */
	public void setErrorView(String errorView) {
		this.errorView = errorView;
	}

	/**
	 * @return Returns the pageView.
	 */
	public String getPageView() {
		return pageView;
	}

	/**
	 * @param pageView The pageView to set.
	 */
	public void setPageView(String pageView) {
		this.pageView = pageView;
	}

	/**
     * @return Returns the noteManager.
     */
    public NoteManager getNoteManager() {
        return noteManager;
    }

    /**
     * @param noteManager The noteManager to set.
     */
    public void setNoteManager(NoteManager noteManager) {
        this.noteManager = noteManager;
    }

    /* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String noteId = request.getParameter( "noteid" );
		
		// Get notes.
		Note note = this.noteManager.getNote( noteId );
		
        NoteInfo noteInfo = new NoteInfo();
        BeanUtils.copyProperties( noteInfo, note );
        noteInfo.setPostTime( note.getPostTime().toLocaleString() );
        
        List replyNoteInfos = new ArrayList();
        
        Iterator it = note.getReplyNotes().iterator();
        while ( it.hasNext() )
        {
            ReplyNote replyNote = ( ReplyNote ) it.next();
            ReplyNoteInfo replyNoteInfo = new ReplyNoteInfo();
            BeanUtils.copyProperties( replyNoteInfo, replyNote );
            replyNoteInfo.setPostTime( replyNote.getPostTime().toLocaleString() );
            replyNoteInfos.add( replyNoteInfo );
        }
        
		Map resultMap = new HashMap();
		resultMap.put( "note", noteInfo );
		resultMap.put( "replyNotes", replyNoteInfos );
		
		return new ModelAndView( this.getPageView(), resultMap );
	}
}
