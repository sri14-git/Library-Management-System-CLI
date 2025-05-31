package org.lms.dao;

import org.lms.model.Member;

import javax.persistence.EntityManager;
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
        return em.find(Member.class, memberId);
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
     TypedQuery<Member> query = em.createQuery("SELECT m FROM Member m",Member.class);
     return query.getResultList();
    }

    public Member findByUsername(String username) {
        TypedQuery<Member> query=em.createQuery("SELECT m from Member m WHERE m.username=:username", Member.class);
        query.setParameter("username",username);
        return query.getSingleResult();
    }

}
