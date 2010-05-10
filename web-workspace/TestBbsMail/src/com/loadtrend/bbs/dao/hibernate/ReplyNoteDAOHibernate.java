package com.loadtrend.bbs.dao.hibernate;

import java.util.List;

import com.loadtrend.bbs.dao.ReplyNoteDAO;
import com.loadtrend.bbs.po.Note;
import com.loadtrend.bbs.po.ReplyNote;

public class ReplyNoteDAOHibernate extends BaseDAOHibernate implements ReplyNoteDAO
{
    public void saveReplyNote( ReplyNote replyNote )
    {
        getHibernateTemplate().saveOrUpdate( replyNote );
    }
    
    public List getReplyNotes( Note note )
    {
       return getHibernateTemplate().find( "from ReplyNote r where r.note=?", note ); 
    }

    public List getReplyNotes()
    {
    	return getHibernateTemplate().find( "from ReplyNote" );
    }
    
    public void removeReplyNote( ReplyNote replyNote )
    {
    	getHibernateTemplate().delete( replyNote );
    }
    
    public void removeReplyNotes( List replyNotes )
    {
    	getHibernateTemplate().deleteAll( replyNotes );
    }
}
