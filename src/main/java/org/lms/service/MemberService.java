package org.lms.service;

import org.lms.dao.MemberDAO;
import org.lms.model.Member;

import javax.persistence.EntityManager;
import java.util.List;

public class MemberService {
    private final MemberDAO memberDAO;

    public MemberService(EntityManager em) {
        this.memberDAO = new MemberDAO(em);
    }
    public void save(Member member){
        memberDAO.save(member);
    }
    public Member findById(int id){
        return memberDAO.findById(id);
    }
    public void update(Member member){ /// /need to remove this
        memberDAO.update(member);
    }
    public List<Member> findAll(){
        return memberDAO.findAll();
    }
    public Member findByUsername(String username){
        return memberDAO.findByUsername(username);
    }

    public void remove(int memberId){ /// /need to return status
        Member member = findById(memberId);
        if (member == null) {
            return;
        }
        memberDAO.remove(memberId);
    }
}
