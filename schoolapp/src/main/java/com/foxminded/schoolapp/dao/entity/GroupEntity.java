package com.foxminded.schoolapp.dao.entity;

import java.util.Objects;

public class GroupEntity {

    private int group_id;
    private String name;

    public GroupEntity() {
        super();
    }

    public GroupEntity(int id, String name) {
        this.group_id = id;
        this.name = name;
    }

    public GroupEntity(String name) {
        this.name = name;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(group_id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GroupEntity other = (GroupEntity) obj;
        return group_id == other.group_id && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return "GroupEntity [id=" + group_id + ", name=" + name + "]";
    }

}
