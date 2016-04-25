package com.dyrwi.lasttimesince.repo.implementations;

import org.joda.time.LocalDateTime;

import java.util.Date;
import java.util.List;

/**
 * Created by Ben on 03-Mar-16.
 */
public interface JodaImplementation<T> {
    int createOrUpdate(T t);
    int delete(T t);
    T findByID(String id);
    T findByID(long id);
    List<T> findByDateCreated(LocalDateTime dateCreated);
    List<T> findByDateModified(LocalDateTime dateModified);
    List<T> getAll();
    T get(T t);
    void deleteAll();
}
