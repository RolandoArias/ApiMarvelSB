package com.api.ApiMarvel.entity;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "comics")
public class Comic {

    @Id
    private Long comicId;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(name = "thumbnail_url", nullable = false)
    private String thumbnailUrl;

    // Constructor sin argumentos
    public Comic() {}

    // Constructor con argumentos
    public Comic(String title, String description,String thumbnailUrl) {
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
    }

    // Getters y Setters
    public Long getComicId() {
        return comicId;
    }


    public void setComicId(Long comicId) {
        this.comicId = comicId;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    // hashCode, equals y toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comic comic = (Comic) o;
        return Objects.equals(comicId, comic.comicId) &&
                Objects.equals(title, comic.title) &&
                Objects.equals(description, comic.description) &&
                Objects.equals(thumbnailUrl, comic.thumbnailUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(comicId, title, description,thumbnailUrl);
    }

    @Override
    public String toString() {
        return "Comic{" +
                "comicId=" + comicId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                '}';
    }
}

