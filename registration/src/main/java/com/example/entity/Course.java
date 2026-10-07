package com.example.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int capacity;

    @Enumerated(EnumType.STRING)
    private Department department;

    private LocalDate dropDeadline;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Department> allowedDepartments = new HashSet<>();

    @Embedded
    private TimeSlot timeSlot;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "course_prerequisites",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "prerequisite_id")
    )
    private Set<Course> prerequisites = new HashSet<>();

    public Course() {
    }

    public Course(String name,
                  int capacity,
                  Department department,
                  LocalDate dropDeadline,
                  TimeSlot timeSlot) {

        this.name = name;
        this.capacity = capacity;
        this.department = department;
        this.dropDeadline = dropDeadline;
        this.timeSlot = timeSlot;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public Department getDepartment() {
        return department;
    }

    public LocalDate getDropDeadline() {
        return dropDeadline;
    }

    public Set<Department> getAllowedDepartments() {
        return allowedDepartments;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public Set<Course> getPrerequisites() {
        return prerequisites;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setDropDeadline(LocalDate dropDeadline) {
        this.dropDeadline = dropDeadline;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public void setAllowedDepartments(
            Set<Department> allowedDepartments) {

        this.allowedDepartments = allowedDepartments;
    }

    public void addPrerequisite(Course course) {
        prerequisites.add(course);
    }
}