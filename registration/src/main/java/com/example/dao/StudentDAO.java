package com.example.dao;

import com.example.entity.Student;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class StudentDAO {

    
    public void save(Student student) {

        Transaction transaction = null;

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(student);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            e.printStackTrace();
        }
    }

    
    public Student findById(Long id) {

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            return session.get(Student.class, id);

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    
    public List<Student> findAll() {

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            return session
                    .createQuery("FROM Student", Student.class)
                    .getResultList();

        } catch (Exception e) {

            e.printStackTrace();
            return List.of();
        }
    }

   
    public void update(Student student) {

        Transaction transaction = null;

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.merge(student);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            e.printStackTrace();
        }
    }

    
    public void delete(Student student) {

        Transaction transaction = null;

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.remove(student);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            e.printStackTrace();
        }
    }
}
