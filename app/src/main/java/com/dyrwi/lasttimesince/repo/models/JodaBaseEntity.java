package com.dyrwi.lasttimesince.repo.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.Date;

/**
 * Created by Ben on 03-Mar-16.
 *
 * BaseEntity class. This holds only three values, id, dateCreated and dateModified.
 * This is using the ORMLite system. Please check their documentation for further help.
 */
public abstract class JodaBaseEntity {
    // STATIC FIELDS
    public final static String ID = "id";
    public final static String DATE_CREATED = "date_created";
    public final static String DATE_MODIFIED = "date_modified";

    //Database Fields
    @DatabaseField(generatedId = true, columnName = ID)
    private long id;

    @DatabaseField(columnName = DATE_CREATED, dataType = DataType.SERIALIZABLE)
    private LocalDateTime dateCreated;

    @DatabaseField(columnName = DATE_MODIFIED, dataType = DataType.SERIALIZABLE)
    private LocalDateTime dateModified;

    //Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getDateModified() {
        return dateModified;
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified = dateModified;
    }

}
