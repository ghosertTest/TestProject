package com.loadtrend.bbs.action;

import java.sql.Timestamp;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.loadtrend.bbs.info.PostNoteInfo;
import com.loadtrend.bbs.po.Note;

public class PostNoteAction extends BaseFormController
{
    protected final Log log = LogFactory.getLog(getClass());
    
    protected ModelAndView onSubmit( HttpServletRequest request, HttpServletResponse response, Object cmd, BindException ex ) throws Exception
	{
		PostNoteInfo postNoteInfo = (PostNoteInfo) cmd;
		
        // Test Log4J here. The first way logger below is enough.
        super.logger.info( "spring logger postNoteInfo:" + postNoteInfo );
        this.log.info( "self logger postNoteInfo:" + postNoteInfo );
		
		// Convert the info to Note.
		Note  note = new Note();
		note.setAuthor( "" );
		note.setEmail( postNoteInfo.getEmail() );
		note.setTitle( postNoteInfo.getTitle() );
		note.setContent( postNoteInfo.getContent() );
		note.setPostTime( new Timestamp( System.currentTimeMillis() ) );
		note.setStatus( new Integer( "0" ) );
		
		// Add note.
		super.getNoteManager().saveNote( note );
		
        // Sent email.
		SimpleEmail email = new SimpleEmail();
		email.setHostName( super.getPostMailInfo().getHostName() );
		email.setAuthentication( super.getPostMailInfo().getUsername(), super.getPostMailInfo().getPassword() );
		Iterator it2 = super.getPostMailInfo().getAddressList().iterator();
		while ( it2.hasNext() )
		{
			email.addTo( (String) it2.next() );
		}
		
		// Set Title: Subject, From Name as UTF-8, This sentence should be put before email.setFrom, email.setSubject method.
		email.setCharset( super.getPostMailInfo().getMailTitleEncoding() ); 
		email.setFrom( super.getPostMailInfo().getFromAddress(), super.getMessage( "BBS_MAIL_FROMNAME_TITLE", request.getLocale() ) ); 
		email.setSubject( super.getMessage( "BBS_MAIL_MAILTITLE_TITLE", note.getTitle(), request.getLocale() ) ); 
		email.setContent( super.getMessage( "BBS_MAIL_MAILCONTENT_TITLE", note.getAuthor(), note.getPostTime().toLocaleString(), note.getTitle(),
                                            note.getContent(), request.getLocale() ), 
				          super.getPostMailInfo().getMailContentEncoding() );
		email.send();

		return new ModelAndView( this.getSuccessView() );
	}
}
