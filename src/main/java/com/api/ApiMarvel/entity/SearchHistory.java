package com.api.ApiMarvel.entity;

import jakarta.persistence.*;

import java.util.Objects;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "search_history")
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "entity", nullable = false)
    private String entity;

    @Column(name = "entity_type", nullable = false)
    private String entityType;

    @Column(name = "search_time", nullable = false)
    private LocalDateTime searchTime;



    // Constructor sin argumentos
    public SearchHistory() {}


    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }


    public LocalDateTime getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(LocalDateTime searchTime) {
        this.searchTime = searchTime;
    }

    // hashCode, equals y toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchHistory that = (SearchHistory) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(entityType, that.entityType) &&
                Objects.equals(entity, that.entity) &&
                Objects.equals(searchTime, that.searchTime) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entityType,entity, searchTime, user);
    }

    @Override
    public String toString() {
        return "SearchHistory{" +
                "id=" + id +
                ", entityType='" + entityType + '\'' +
                ", entity='" + entity + '\'' +
                ", timestamp=" + searchTime +
                ", user=" + user +
                '}';
    }
}
