package com.example.entity;

import jakarta.persistence.*;


import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Department department;

    @ManyToMany
    @JoinTable(
            name = "student_completed_courses",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> completedCourses = new HashSet<>();

    public Student() {
    }

    public Student(String name, Department department) {
        this.name = name;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Department getDepartment() {
        return department;
    }

    public Set<Course> getCompletedCourses() {
        return completedCourses;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setCompletedCourses(Set<Course> completedCourses) {
        this.completedCourses = completedCourses;
    }

    public void addCompletedCourse(Course course) {
        completedCourses.add(course);
    }
}