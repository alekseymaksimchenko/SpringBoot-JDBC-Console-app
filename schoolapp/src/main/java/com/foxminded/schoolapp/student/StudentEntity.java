package com.foxminded.schoolapp.student;

import java.util.Objects;

public class StudentEntity {

    private int id;
    private String firstname;
    private String lastname;
    private int groupId;

    public StudentEntity() {
        super();
    }

    public StudentEntity(int id, String firstname, String lastname, int groupId) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.groupId = groupId;
    }

    public StudentEntity(String firstname, String lastname, int groupId) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.groupId = groupId;
    }

    public int getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public int getGroupId() {
        return groupId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, groupId, id, lastname);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StudentEntity other = (StudentEntity) obj;
        return Objects.equals(firstname, other.firstname) && groupId == other.groupId && id == other.id
                && Objects.equals(lastname, other.lastname);
    }

    @Override
    public String toString() {
        return "StudentEntity [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", groupId="
                + groupId + "]";
    }

}
