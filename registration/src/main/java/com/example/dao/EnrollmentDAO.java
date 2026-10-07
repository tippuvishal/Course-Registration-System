package com.example.dao;



import com.example.entity.Enrollment;
import com.example.entity.EnrollmentStatus;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EnrollmentDAO {

    // Save enrollment
    public void save(Enrollment enrollment) {

        Transaction transaction = null;

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(enrollment);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            e.printStackTrace();
        }
    }

    // Find enrollment by ID
    public Enrollment findById(Long id) {

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            return session.get(Enrollment.class, id);

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    // Get all enrollments
    public List<Enrollment> findAll() {

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            return session
                    .createQuery(
                            "FROM Enrollment",
                            Enrollment.class
                    )
                    .getResultList();

        } catch (Exception e) {

            e.printStackTrace();
            return List.of();
        }
    }

    // Update enrollment
    public void update(Enrollment enrollment) {

        Transaction transaction = null;

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.merge(enrollment);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            e.printStackTrace();
        }
    }

    // Find active enrollment
    public Enrollment findActiveEnrollment(
            Long studentId,
            Long courseId) {

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                            """
                            FROM Enrollment e
                            WHERE e.student.id = :studentId
                            AND e.course.id = :courseId
                            AND e.status = :status
                            """,
                            Enrollment.class
                    )
                    .setParameter("studentId", studentId)
                    .setParameter("courseId", courseId)
                    .setParameter(
                            "status",
                            EnrollmentStatus.ACTIVE
                    )
                    .uniqueResult();

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    // Get all active enrollments for a student
    public List<Enrollment> findActiveByStudent(
            Long studentId) {

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                            """
                            FROM Enrollment e
                            WHERE e.student.id = :studentId
                            AND e.status = :status
                            """,
                            Enrollment.class
                    )
                    .setParameter("studentId", studentId)
                    .setParameter(
                            "status",
                            EnrollmentStatus.ACTIVE
                    )
                    .getResultList();

        } catch (Exception e) {

            e.printStackTrace();
            return List.of();
        }
    }

    // Get all active enrollments for a course
    public List<Enrollment> findActiveByCourse(
            Long courseId) {

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                            """
                            FROM Enrollment e
                            WHERE e.course.id = :courseId
                            AND e.status = :status
                            """,
                            Enrollment.class
                    )
                    .setParameter("courseId", courseId)
                    .setParameter(
                            "status",
                            EnrollmentStatus.ACTIVE
                    )
                    .getResultList();

        } catch (Exception e) {

            e.printStackTrace();
            return List.of();
        }
    }

    // Count active enrollments for a course
    public long countActiveEnrollments(Long courseId) {

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                            """
                            SELECT COUNT(e)
                            FROM Enrollment e
                            WHERE e.course.id = :courseId
                            AND e.status = :status
                            """,
                            Long.class
                    )
                    .setParameter("courseId", courseId)
                    .setParameter(
                            "status",
                            EnrollmentStatus.ACTIVE
                    )
                    .getSingleResult();

        } catch (Exception e) {

            e.printStackTrace();
            return 0;
        }
    }
}