package com.example.service;

import com.example.dao.CourseDAO;
import com.example.dao.EnrollmentDAO;
import com.example.dao.StudentDAO;
import com.example.entity.Course;
import com.example.entity.Enrollment;
import com.example.entity.EnrollmentStatus;
import com.example.entity.Student;

import java.time.LocalDate;
import java.util.List;

public class RegistrationService {

    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;
    private final EnrollmentDAO enrollmentDAO;

    public RegistrationService() {
        studentDAO = new StudentDAO();
        courseDAO = new CourseDAO();
        enrollmentDAO = new EnrollmentDAO();
    }

   
    

    public void register(Long studentId, Long courseId) {

   
        Student student = studentDAO.findById(studentId);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

       
        Course course = courseDAO.findById(courseId);

        if (course == null) {
            System.out.println("Course not found.");
            return;
        }

        // RULE 1: COURSE CAPACITY

        long currentEnrollment =
                enrollmentDAO.countActiveEnrollments(courseId);

        if (currentEnrollment >= course.getCapacity()) {

            System.out.println(
                    "Registration failed: Course is full."
            );

            return;
        }

      

        Enrollment existingEnrollment =
                enrollmentDAO.findActiveEnrollment(
                        studentId,
                        courseId
                );

        if (existingEnrollment != null) {

            System.out.println(
                    "Registration failed: "
                            + "Student is already registered "
                            + "for this course."
            );

            return;
        }

        

        for (Course prerequisite : course.getPrerequisites()) {

            boolean completed =
                    student.getCompletedCourses()
                            .contains(prerequisite);

            if (!completed) {

                System.out.println(
                        "Registration failed: "
                                + "Prerequisite not completed."
                );

                System.out.println(
                        "Required course: "
                                + prerequisite.getName()
                );

                return;
            }
        }

        // rule 4: SCHEDULE CONFLICT

        List<Enrollment> activeEnrollments =
                enrollmentDAO.findActiveByStudent(studentId);

        for (Enrollment enrollment : activeEnrollments) {

            Course registeredCourse =
                    enrollment.getCourse();

            if (registeredCourse.getTimeSlot() != null
                    && course.getTimeSlot() != null
                    && registeredCourse.getTimeSlot()
                    .overlaps(course.getTimeSlot())) {

                System.out.println(
                        "Registration failed: "
                                + "Schedule conflict."
                );

                System.out.println(
                        "Conflict with: "
                                + registeredCourse.getName()
                );

                return;
            }
        }

        

                if (student.getDepartment() != course.getDepartment()) {

                System.out.println();
                System.out.println("-----------------------------------------");
                System.out.println("Registration failed!");
                System.out.println("Course: " + course.getName());
                System.out.println("Your Department: " + student.getDepartment());
                System.out.println("Course Department: " + course.getDepartment());
                System.out.println(
                        "You cannot register for this course because "
                                + "the department does not match."
                );
                System.out.println("-----------------------------------------");

                return;
        }

       

        Enrollment enrollment =
                new Enrollment(student, course);

        enrollmentDAO.save(enrollment);

        System.out.println();
        System.out.println(
                "-----------------------------------------"
        );
        System.out.println(
                " Course Registered Successfully "
        );
        System.out.println(
                "-----------------------------------------"
        );

        System.out.println(
                "Student : " + student.getName()
        );

        System.out.println(
                "Course  : " + course.getName()
        );
    }


  

    public void drop(Long studentId, Long courseId) {

       
        Student student =
                studentDAO.findById(studentId);

        if (student == null) {

            System.out.println(
                    "Student not found."
            );

            return;
        }

        
        Course course =
                courseDAO.findById(courseId);

        if (course == null) {

            System.out.println(
                    "Course not found."
            );

            return;
        }

        
        Enrollment enrollment =
                enrollmentDAO.findActiveEnrollment(
                        studentId,
                        courseId
                );

        if (enrollment == null) {

            System.out.println(
                    "Student is not registered "
                            + "for this course."
            );

            return;
        }

        // RULE 6: DROP DEADLINE

        LocalDate today = LocalDate.now();

        if (course.getDropDeadline() == null) {

            System.out.println(
                    "Drop deadline is not configured."
            );

            return;
        }

        if (today.isAfter(course.getDropDeadline())) {

            System.out.println(
                    "Cannot drop course."
            );

            System.out.println(
                    "Drop deadline has already passed."
            );

            return;
        }

        
        enrollment.setStatus(
                EnrollmentStatus.DROPPED
        );

        enrollmentDAO.update(enrollment);

        System.out.println();
        System.out.println(
                "Course dropped successfully."
        );

        System.out.println(
                "Student : " + student.getName()
        );

        System.out.println(
                "Course  : " + course.getName()
        );
    }


   

    public void viewStudentCourses(Long studentId) {

        Student student =
                studentDAO.findById(studentId);

        if (student == null) {

            System.out.println(
                    "Student not found."
            );

            return;
        }

        List<Enrollment> enrollments =
                enrollmentDAO.findActiveByStudent(
                        studentId
                );

        System.out.println();
        System.out.println(
                "===== REGISTERED COURSES ====="
        );

        if (enrollments.isEmpty()) {

            System.out.println(
                    "No courses registered."
            );

            return;
        }

        for (Enrollment enrollment : enrollments) {

            Course course =
                    enrollment.getCourse();

            System.out.println(
                    "Course ID   : " + course.getId()
            );

            System.out.println(
                    "Course Name : " + course.getName()
            );

            System.out.println(
                    "Registered  : "
                            + enrollment.getRegisteredAt()
            );

            System.out.println(
                    "Status      : "
                            + enrollment.getStatus()
            );

            System.out.println(
                    "-----------------------------"
            );
        }
    }


    

    public void viewCourseStudents(Long courseId) {

        Course course =
                courseDAO.findById(courseId);

        if (course == null) {

            System.out.println(
                    "Course not found."
            );

            return;
        }

        List<Enrollment> enrollments =
                enrollmentDAO.findActiveByCourse(
                        courseId
                );

        System.out.println();
        System.out.println(
                "===== COURSE DETAILS ====="
        );

        System.out.println(
                "Course ID : " + course.getId()
        );

        System.out.println(
                "Course    : " + course.getName()
        );

        System.out.println(
                "Capacity  : " + course.getCapacity()
        );

        System.out.println(
                "Enrolled  : " + enrollments.size()
        );

        System.out.println();
        System.out.println(
                "===== ENROLLED STUDENTS ====="
        );

        if (enrollments.isEmpty()) {

            System.out.println(
                    "No students enrolled."
            );

            return;
        }

        for (Enrollment enrollment : enrollments) {

            Student student =
                    enrollment.getStudent();

            System.out.println(
                    student.getId()
                            + " - "
                            + student.getName()
            );
        }
    }
}

