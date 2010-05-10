package com.packtpub.t5first.pages;

import java.text.Format;
import java.util.List;

import org.apache.tapestry.annotations.ApplicationState;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.OnEvent;

import com.packtpub.t5first.data.MockDataSource;
import com.packtpub.t5first.model.Celebrity;
import com.packtpub.t5first.model.User;
import com.packtpub.t5first.util.Formats;

public class ShowAll {
    @ApplicationState
    private User user;

    /**
     * It seems if you use userExists to check a ASO, the ASO above should be defined in this class.
     */
    private boolean userExists;

    @ApplicationState
    private MockDataSource dataSource;

    @InjectPage
    private Details detailsPage;

    private Celebrity celebrity;
    
    /**
     * The onActivate() method is invoked every time the page is loaded, and if there is some value in the activation context,
     * it will be passed as an argument to this method.
     * @return
     */
    Object onActivate() {
        if (!userExists) return Start2.class;
        return null;
    }

    @OnEvent(component = "detailsLink")
    Object onShowDetails(long id) {
        Celebrity celebrity = dataSource.getCelebrityById(id);
        detailsPage.setCelebrity(celebrity);
        return detailsPage;
    }

    public List<Celebrity> getAllCelebrities() {
        return dataSource.getAllCelebrities();
    }

    public Celebrity getCelebrity() {
        return celebrity;
    }

    public void setCelebrity(Celebrity celebrity) {
        this.celebrity = celebrity;
    }

    public Format getDateFormat() {
        return Formats.getDateFormat();
    }
}