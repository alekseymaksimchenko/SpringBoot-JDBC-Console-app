package com.foxminded.schoolapp.dao.entity;

import java.util.Objects;

public class GroupEntity {

    private int groupId;
    private String name;

    public GroupEntity() {
        super();
    }

    public GroupEntity(int id, String name) {
        this.groupId = id;
        this.name = name;
    }

    public GroupEntity(String name) {
        this.name = name;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj == null) || (getClass() != obj.getClass()))
            return false;
        GroupEntity other = (GroupEntity) obj;
        return groupId == other.groupId && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return "GroupEntity [id=" + groupId + ", name=" + name + "]";
    }

}
