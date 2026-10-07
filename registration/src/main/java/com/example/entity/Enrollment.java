package com.example.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private LocalDateTime registeredAt;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    
    public Enrollment() {
    }

    
    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.registeredAt = LocalDateTime.now();
        this.status = EnrollmentStatus.ACTIVE;
    }


    public Long getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }


    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }
}
