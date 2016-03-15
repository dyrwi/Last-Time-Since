package com.dyrwi.lasttimesince.repo.implementations;

import java.util.Date;
import java.util.List;

/**
 * Created by Ben on 03-Mar-16.
 */
public interface Implementation<T> {
    int createOrUpdate(T t);
    int delete(T t);
    T findByID(String id);
    T findByID(long id);
    List<T> findByDateCreated(Date dateCreated);
    List<T> findByDateModified(Date dateModified);
    List<T> getAll();
    void deleteAll();
}
