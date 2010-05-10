package com.loadtrend.bbs.dao;

import java.util.List;

import com.loadtrend.bbs.po.Note;
import com.loadtrend.bbs.po.ReplyNote;

public interface ReplyNoteDAO extends DAO
{
    public void saveReplyNote( ReplyNote replyNote );
    
    public List getReplyNotes( Note note );
    
    public void removeReplyNote( ReplyNote replyNote );
    
    public void removeReplyNotes( List replyNotes );
}
