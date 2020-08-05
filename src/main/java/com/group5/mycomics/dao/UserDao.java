package com.group5.mycomics.dao;

import com.group5.mycomics.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

@Repository
@Transactional
public class UserDao {
    @Autowired
    private EntityManager entityManager;

    public User findUser(String email){
        try {
            String sql = "SELECT u FROM User u where u.email= :email";
            Query query = entityManager.createQuery(sql, User.class);
            query.setParameter("email", email);
            return (User) query.getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    public void updateUserPassword(String email, String password){
        String sql = "UPDATE User u SET u.password=:password WHERE email=:email";
        Query query = entityManager.createQuery(sql,User.class);
        query.setFlushMode(FlushModeType.AUTO);
        query.setParameter("password", password);
        query.setParameter("email",email);
        query.executeUpdate();
    }

    public void addUser(User user){
        entityManager.persist(user);
    }
}
