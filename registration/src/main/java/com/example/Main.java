package com.example;

import com.example.dao.CourseDAO;
import com.example.dao.StudentDAO;
import com.example.entity.Course;
import com.example.entity.Department;
import com.example.entity.Student;
import com.example.service.RegistrationService;
import com.example.util.HibernateUtil;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final StudentDAO studentDAO = new StudentDAO();
    private static final CourseDAO courseDAO = new CourseDAO();
    private static final RegistrationService registrationService = new RegistrationService();

    public static void main(String[] args) {

    boolean running = true;

    while (running) {

        showMenu();

        int choice = readInt("Enter your choice: ");

        switch (choice) {

            case 1:
                registerStudent();
                break;

            case 2:
                viewStudents();
                break;

            case 3:
                createCourse();
                break;

            case 4:
                viewCourses();
                break;

            case 5:
                registerForCourse();
                break;

            case 6:
                viewMyCourses();
                break;

            case 7:
                dropCourse();
                break;

            case 8:
                viewCourseStudents();
                break;

            case 9:
                running = false;

                System.out.println();
                System.out.println(
                        "Thank you for using Course Registration System!"
                );

                break;

            default:
                System.out.println(
                        "Invalid choice. Please try again."
                );
        }
    }

    scanner.close();
    HibernateUtil.shutdown();
}


   
    private static void showMenu() {

        System.out.println("-----------------------------------------");
        System.out.println("      ANUDIP COURSE REGISTRATION ");
        System.out.println("-----------------------------------------");

        System.out.println("1. Register Student");
        System.out.println("2. View Students");
        System.out.println("3. Create Course");
        System.out.println("4. View Courses");
        System.out.println("5. Register for Course");
        System.out.println("6. View My Courses");
        System.out.println("7. Drop Course");
        System.out.println("8. View Course Students");
        System.out.println("9. Exit");

        System.out.println("-----------------------------------------");
    }

   
    //reg stdudent
    private static void registerStudent() {

        System.out.println();
        System.out.println("========== REGISTER STUDENT ==========");

        System.out.print("Enter student name: ");
        String name = scanner.nextLine();

        System.out.println();
        System.out.println("Select Department:");

        Department[] departments = Department.values();

        for (int i = 0; i < departments.length; i++) {
            System.out.println(
                    (i + 1) + ". " + departments[i]
            );
        }

        int departmentChoice =
                readInt("Enter department: ");

        if (departmentChoice < 1
                || departmentChoice > departments.length) {

            System.out.println(
                    "Invalid department choice."
            );
            return;
        }

        Department department =
                departments[departmentChoice - 1];

        Student student =
                new Student(name, department);

        studentDAO.save(student);

        System.out.println();
        System.out.println(
                "Student registered successfully!"
        );

        System.out.println(
                "Student ID: " + student.getId()
        );
    }

  



private static void createCourse() {

        System.out.println();
        System.out.println("========== CREATE COURSE ==========");

        System.out.print("Enter course name: ");
        String name = scanner.nextLine();

        int capacity =
                readInt("Enter course capacity: ");

        if (capacity <= 0) {
                System.out.println(
                        "Capacity must be greater than 0."
                );
                return;
        }

        System.out.println();
        System.out.println("Select Department:");

        Department[] departments = Department.values();

        for (int i = 0; i < departments.length; i++) {

                System.out.println(
                        (i + 1) + ". " + departments[i]
                );
        }

        int departmentChoice =
                readInt("Enter department: ");

        if (departmentChoice < 1
                || departmentChoice > departments.length) {

                System.out.println(
                        "Invalid department choice."
                );

                return;
        }

        Department department =
                departments[departmentChoice - 1];

        System.out.print(
                "Enter drop deadline (yyyy-MM-dd): "
        );

        java.time.LocalDate dropDeadline;

        try {

                dropDeadline =
                        java.time.LocalDate.parse(
                                scanner.nextLine()
                        );

        } catch (Exception e) {

                System.out.println(
                        "Invalid date format."
                );

                return;
        }

        System.out.println();
        System.out.println("Select Day:");

        java.time.DayOfWeek[] days =
                java.time.DayOfWeek.values();

        for (int i = 0; i < days.length; i++) {

                System.out.println(
                        (i + 1) + ". " + days[i]
                );
        }

        int dayChoice =
                readInt("Enter day: ");

        if (dayChoice < 1
                || dayChoice > days.length) {

                System.out.println(
                        "Invalid day choice."
                );

                return;
        }

        java.time.DayOfWeek day =
                days[dayChoice - 1];

        System.out.print(
                "Enter start time (HH:mm): "
        );

        java.time.LocalTime startTime;

        try {

                startTime =
                        java.time.LocalTime.parse(
                                scanner.nextLine()
                        );

        } catch (Exception e) {

                System.out.println(
                        "Invalid time format."
                );

                return;
        }

        System.out.print(
                "Enter end time (HH:mm): "
        );

        java.time.LocalTime endTime;

        try {

                endTime =
                        java.time.LocalTime.parse(
                                scanner.nextLine()
                        );

        } catch (Exception e) {

                System.out.println(
                        "Invalid time format."
                );

                return;
        }

        if (!startTime.isBefore(endTime)) {

                System.out.println(
                        "End time must be after start time."
                );

                return;
        }

        com.example.entity.TimeSlot timeSlot =
                new com.example.entity.TimeSlot(
                        day,
                        startTime,
                        endTime
                );

        Course course =
        new Course(
                name,
                capacity,
                department,
                dropDeadline,
                timeSlot
        );

        course.setAllowedDepartments(
           java.util.Set.of(department)
       );

        courseDAO.save(course);

        System.out.println();
        System.out.println(
                "Course created successfully!"
        );

        System.out.println(
                "Course ID: " + course.getId()
        );

        System.out.println(
                "Course Name: " + course.getName()
        );
}

    //view students
    private static void viewStudents() {

        System.out.println();
        System.out.println("========== STUDENTS ==========");

        List<Student> students =
                studentDAO.findAll();

        if (students.isEmpty()) {

            System.out.println(
                    "No students found."
            );
            return;
        }

        for (Student student : students) {

            System.out.println(
                    "ID         : " + student.getId()
            );

            System.out.println(
                    "Name       : " + student.getName()
            );

            System.out.println(
                    "Department : " + student.getDepartment()
            );

            System.out.println(
                    "------------------------------"
            );
        }
    }

    // 3. VIEW COURSES

    private static void viewCourses() {

        System.out.println();
        System.out.println("========== AVAILABLE COURSES ==========");

        List<Course> courses =
                courseDAO.findAll();

        if (courses.isEmpty()) {

            System.out.println(
                    "No courses found."
            );
            return;
        }

        for (Course course : courses) {

            long enrolled =
                    new com.example.dao.EnrollmentDAO()
                            .countActiveEnrollments(
                                    course.getId()
                            );

            long availableSeats =
                    course.getCapacity() - enrolled;

            System.out.println(
                    "ID       : " + course.getId()
            );

            System.out.println(
                    "Course   : " + course.getName()
            );

            System.out.println(
                    "Capacity : " + course.getCapacity()
            );

            System.out.println(
                    "Available: " + availableSeats
            );

            System.out.println(
                    "------------------------------"
            );
        }
    }

    // 4. REGISTER FOR COURSE

    private static void registerForCourse() {

        System.out.println();
        System.out.println("========== COURSE REGISTRATION ==========");

        System.out.println();
        System.out.println("Available Courses:");

        List<Course> courses =
                courseDAO.findAll();

        if (courses.isEmpty()) {

            System.out.println(
                    "No courses available."
            );
            return;
        }

        for (Course course : courses) {

            long enrolled =
                    new com.example.dao.EnrollmentDAO()
                            .countActiveEnrollments(
                                    course.getId()
                            );

            long availableSeats =
                    course.getCapacity() - enrolled;

            System.out.println(
                    course.getId()
                            + "  "
                            + course.getName()
                            + "  Seats: "
                            + availableSeats
            );
        }

        System.out.println();

        long studentId =
                readLong("Enter Student ID: ");

        long courseId =
                readLong("Enter Course ID: ");

        registrationService.register(
                studentId,
                courseId
        );
    }

    // 5. VIEW MY COURSES

    private static void viewMyCourses() {

        System.out.println();

        long studentId =
                readLong("Enter Student ID: ");

        registrationService.viewStudentCourses(
                studentId
        );
    }

    // 6. DROP COURSE

    private static void dropCourse() {

        System.out.println();
        System.out.println("========== DROP COURSE ==========");

        long studentId =
                readLong("Enter Student ID: ");

        long courseId =
                readLong("Enter Course ID: ");

        registrationService.drop(
                studentId,
                courseId
        );
    }

    // 7. VIEW COURSE STUDENTS

    private static void viewCourseStudents() {

        System.out.println();

        long courseId =
                readLong("Enter Course ID: ");

        registrationService.viewCourseStudents(
                courseId
        );
    }

    // INPUT HELPERS
    private static int readInt(String message) {

        while (true) {

            try {

                System.out.print(message);

                int value =
                        Integer.parseInt(
                                scanner.nextLine()
                        );

                return value;

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }

    private static long readLong(String message) {

        while (true) {

            try {

                System.out.print(message);

                long value =
                        Long.parseLong(
                                scanner.nextLine()
                        );

                return value;

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }
}