package com.example.dao;



import com.example.util.HibernateUtil;

import com.example.entity.Course;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CourseDAO {

    
    public void save(Course course) {

        Transaction transaction = null;

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(course);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            e.printStackTrace();
        }
    }

    // find course by id
    public Course findById(Long id) {

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            return session.get(Course.class, id);

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    // get all courses
    public List<Course> findAll() {

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            return session
                    .createQuery("FROM Course", Course.class)
                    .getResultList();

        } catch (Exception e) {

            e.printStackTrace();
            return List.of();
        }
    }

    
    public void update(Course course) {

        Transaction transaction = null;

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.merge(course);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            e.printStackTrace();
        }
    }

   
    public void delete(Course course) {

        Transaction transaction = null;

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.remove(course);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            e.printStackTrace();
        }
    }
}
