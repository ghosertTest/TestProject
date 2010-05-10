package com.packtpub.t5first.data;

import java.util.List;

import com.packtpub.t5first.model.Celebrity;

public interface IDataSource {
    public List<Celebrity> getAllCelebrities();
    public Celebrity getCelebrityById(long id);
    public void addCelebrity(Celebrity c);
}
