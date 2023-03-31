package com.example.DoroServer.domain.user.entity;


import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Getter
public class Degree {

    private String school;
    private String studentId;
    private String major;
    @Enumerated(EnumType.STRING)
    private StudentStatus studentStatus;

    protected Degree() {
    }

    public Degree(String school, String studentId, String major, StudentStatus studentStatus) {
        this.school = school;
        this.studentId = studentId;
        this.major = major;
        this.studentStatus = studentStatus;
    }
}
