package com.dyrwi.lasttimesince.repo.models;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

/**
 * Created by Ben on 03-Mar-16.
 *
 * BaseEntity class. This holds only three values, id, dateCreated and dateModified.
 * This is using the ORMLite system. Please check their documentation for further help.
 */
public abstract class BaseEntity {
    // STATIC FIELDS
    public final static String ID = "id";
    public final static String DATE_CREATED = "date_created";
    public final static String DATE_MODIFIED = "date_modified";

    //Database Fields
    @DatabaseField(generatedId = true, columnName = ID)
    private long id;

    @DatabaseField(columnName = DATE_CREATED)
    private Date dateCreated;

    @DatabaseField(columnName = DATE_MODIFIED)
    private Date dateModified;

    //Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

}
