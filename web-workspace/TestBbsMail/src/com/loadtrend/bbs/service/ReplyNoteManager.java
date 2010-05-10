package com.loadtrend.bbs.service;

import java.util.List;

import com.loadtrend.bbs.dao.ReplyNoteDAO;
import com.loadtrend.bbs.po.ReplyNote;

public interface ReplyNoteManager {

	public void setReplyNoteDAO(ReplyNoteDAO noteDAO);

	public void saveReplyNote(ReplyNote replyNote);

	public void removeReplyNote(ReplyNote replyNote);

	public void removeReplyNotes(List replyNotes);

}