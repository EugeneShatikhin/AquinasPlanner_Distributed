package com.aquinas.servicegroups.repository.model;
import javax.persistence.*;

@Entity
@Table(name = "_groups")
public final class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(unique = true)
    private String title;
    private String description;
    private long owner_id;
    public Group() {
    }

    public Group(String title, String description, long owner_id) {
        this.title = title;
        this.description = description;
        this.owner_id = owner_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public long getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(long owner_id) {
        this.owner_id = owner_id;
    }
}
