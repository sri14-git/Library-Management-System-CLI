package org.lms.dao;

import org.lms.model.Member;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class MemberDAO {

    private final EntityManager em;

    public MemberDAO(EntityManager em) {
        this.em = em;
    }

    public void save(Member member) {
        em.getTransaction().begin();
        em.persist(member);
        em.getTransaction().commit();
    }
    public Member findById(int memberId){
        try {
            return em.find(Member.class, memberId);
        }
        catch (Exception e){
            return null;
        }
    }
    public void update(Member member) {
        em.getTransaction().begin();
        em.merge(member);
        em.getTransaction().commit();
    }

    public void remove(int memberId){
        Member member = findById(memberId);
        if (member == null) {
            return;
        }
        em.getTransaction().begin();
        em.remove(member);
        em.getTransaction().commit();
    }
    
    public List<Member> findAll(){
     TypedQuery<Member> query = em.createQuery("SELECT m FROM Member m ORDER BY m.memberId",Member.class);
     return query.getResultList();
    }

    public Member findByUsername(String username) {
        try {
            TypedQuery<Member> query = em.createQuery("SELECT m FROM Member m WHERE m.username = :username ", Member.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
