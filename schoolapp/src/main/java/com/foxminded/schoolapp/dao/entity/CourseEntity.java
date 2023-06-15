package com.foxminded.schoolapp.dao.entity;

import java.util.Objects;

public class CourseEntity {

    private int id;
    private String name;
    private String description;

    public CourseEntity() {
        super();
    }

    public CourseEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public CourseEntity(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj == null) || (getClass() != obj.getClass()))
            return false;
        CourseEntity other = (CourseEntity) obj;
        return Objects.equals(description, other.description) && id == other.id && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return "CourseEntity [id=" + id + ", name=" + name + ", description=" + description + "]";
    }

}
