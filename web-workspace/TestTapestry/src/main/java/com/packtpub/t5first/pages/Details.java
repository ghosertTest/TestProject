package com.packtpub.t5first.pages;

import java.text.Format;

import org.apache.tapestry.annotations.ApplicationState;
import org.apache.tapestry.annotations.Persist;

import com.packtpub.t5first.data.MockDataSource;
import com.packtpub.t5first.model.Celebrity;
import com.packtpub.t5first.util.Formats;

public class Details {
    
    // @ApplicationState is added for PageLink component, ActionLink doesn't need it.
    @ApplicationState
    private MockDataSource dataSource;
    
    // @Persist is added for the ActionLink component on the ShowAll.tml to pass in the celebrity object.
    // PageLink doesn't need it.
    @Persist
    private Celebrity celebrity;

    public void setCelebrity(Celebrity c) {
        this.celebrity = c;
    }

    public Celebrity getCelebrity() {
        return celebrity;
    }

    public Format getDateFormat() {
        return Formats.getDateFormat();
    }
    
    /**
     * The method below is added for PageLink component on the ShowAll.tml to pass in the parameter id.
     * ActionLink doesn't need it.
     * @param id
     */
    void onActivate(long id)
    {
        celebrity = dataSource.getCelebrityById(id);
    }
}
