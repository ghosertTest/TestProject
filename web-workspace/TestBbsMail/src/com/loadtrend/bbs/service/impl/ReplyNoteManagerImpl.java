package com.loadtrend.bbs.service.impl;

import java.util.List;

import com.loadtrend.bbs.dao.ReplyNoteDAO;
import com.loadtrend.bbs.po.ReplyNote;
import com.loadtrend.bbs.service.ReplyNoteManager;

public class ReplyNoteManagerImpl extends BaseManager implements ReplyNoteManager 
{
	private ReplyNoteDAO replyNoteDAO = null;

	/* (non-Javadoc)
	 * @see com.loadtrend.bbs.service.impl.ReplyNoteManager#setReplyNoteDAO(com.loadtrend.bbs.replyNoteDAO.ReplyNoteDAO)
	 */
	public void setReplyNoteDAO(ReplyNoteDAO replyNoteDAO) {
		this.replyNoteDAO = replyNoteDAO;
	}

    /* (non-Javadoc)
	 * @see com.loadtrend.bbs.service.impl.ReplyNoteManager#addReplyNote(com.loadtrend.bbs.po.ReplyNote)
	 */
    public void saveReplyNote( ReplyNote replyNote )
    {
        replyNoteDAO.saveReplyNote( replyNote );
    }
    
    /* (non-Javadoc)
	 * @see com.loadtrend.bbs.service.impl.ReplyNoteManager#removeReplyNote(com.loadtrend.bbs.po.ReplyNote)
	 */
    public void removeReplyNote( ReplyNote replyNote )
    {
    	replyNoteDAO.removeReplyNote( replyNote );
    }
    
    /* (non-Javadoc)
	 * @see com.loadtrend.bbs.service.impl.ReplyNoteManager#removeReplyNotes(java.util.List)
	 */
    public void removeReplyNotes( List replyNotes )
    {
    	replyNoteDAO.removeReplyNotes( replyNotes );
    }
}
