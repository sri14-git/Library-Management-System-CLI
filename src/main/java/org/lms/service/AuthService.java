package org.lms.service;

import org.lms.dao.MemberDAO;
import org.lms.model.Member;

import javax.persistence.EntityManager;

public class AuthService {
    private final MemberDAO memberDAO;
    public AuthService(EntityManager em){
        this.memberDAO= new MemberDAO(em);
    }
    public Member authenticate(String username, String password){
        if (username.equals("admin") && password.equals("admin")) {
            Member admin = new Member();
            admin.setUsername("admin");
            admin.setName("Library Admin");
            return admin;
        }
        Member member = memberDAO.findByUsername(username);
        if (member != null && member.getPassword().equals(password) && member.getStatus().name().equals("ACTIVE")) {
            return member;
        }

        return null;
    }
}

