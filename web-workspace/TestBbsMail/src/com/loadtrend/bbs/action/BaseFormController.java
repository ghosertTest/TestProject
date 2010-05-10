package com.loadtrend.bbs.action;

import java.util.Locale;

import org.springframework.web.servlet.mvc.SimpleFormController;

import com.loadtrend.bbs.service.NoteManager;
import com.loadtrend.bbs.util.PostMailInfo;

public class BaseFormController extends SimpleFormController
{
    private NoteManager noteManager = null;
    
    private PostMailInfo postMailInfo = null;
    
	/**
     * @return Returns the postMailInfo.
     */
    public PostMailInfo getPostMailInfo() {
        return postMailInfo;
    }

    /**
     * @param postMailInfo The postMailInfo to set.
     */
    public void setPostMailInfo(PostMailInfo postMailInfo) {
        this.postMailInfo = postMailInfo;
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
    
    public String getMessage( String key, Object[] args, Locale locale )
    {
        return super.getMessageSourceAccessor().getMessage( key, args, locale );
    }
    
    public String getMessage( String key, Locale locale )
    {
        Object[] args = new Object[]{};
        return getMessage( key, args, locale );
    }
    
    public String getMessage( String key, String arg1, Locale locale )
    {
        Object[] args = new Object[]{ arg1 };
        return getMessage( key, args, locale );
    }
    
    public String getMessage( String key, String arg1, String arg2, Locale locale )
    {
        Object[] args = new Object[]{ arg1, arg2 };
        return getMessage( key, args, locale );
    }
    
    public String getMessage( String key, String arg1, String arg2, String arg3, Locale locale )
    {
        Object[] args = new Object[]{ arg1, arg2, arg3 };
        return getMessage( key, args, locale );
    }
    
    public String getMessage( String key, String arg1, String arg2, String arg3, String arg4, Locale locale )
    {
        Object[] args = new Object[]{ arg1, arg2, arg3, arg4 };
        return getMessage( key, args, locale );
    }
}
