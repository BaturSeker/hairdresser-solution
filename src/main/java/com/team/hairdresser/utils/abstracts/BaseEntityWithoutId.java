package com.team.hairdresser.utils.abstracts;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
public abstract class BaseEntityWithoutId implements Serializable {

    @SuppressWarnings("squid:S3437")
    @JsonIgnore
    @Column(name = "createdOn")
    private Instant createdOn;

    @SuppressWarnings("squid:S3437")
    @JsonIgnore
    @Column(name = "updateOn")
    private Instant updateOn;

    @SuppressWarnings("squid:S3437")
    @JsonIgnore
    @Column(name = "deletedOn")
    private Instant deletedOn;

    public Instant getCreatedOn() {
        return createdOn;
    }

    public Instant getUpdateOn() {
        return updateOn;
    }

    public Instant getDeletedOn() {
        return deletedOn;
    }


    @PrePersist
    protected void onCreate(){
        this.createdOn = Instant.now();
        this.updateOn = Instant.now();
    }

    @PreUpdate
    protected void onUpdate(){
        this.updateOn = Instant.now();
    }

}

