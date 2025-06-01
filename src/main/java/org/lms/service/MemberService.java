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
    public  void printMembers(List<Member> members) {
        String format = "| %-10s | %-20s | %-15s | %-15s |\n";
        String separator = "+------------+----------------------+-----------------+-----------------+";

        System.out.println(separator);
        System.out.printf(format, "Member ID", "Name", "Username", "Password");
        System.out.println(separator);

        for (Member m : members) {
            String masked = "*".repeat(m.getPassword().length());
            System.out.printf(format, m.getMemberId(), m.getName(), m.getUsername(), masked);
        }

        System.out.println(separator);
    }

    public  void printMember(Member m) {
        String format = "| %-10s | %-20s | %-15s | %-15s |\n";
        String separator = "+------------+----------------------+-----------------+-----------------+";

        System.out.println(separator);
        System.out.printf(format, "Member ID", "Name", "Username", "Password");
        System.out.println(separator);

        String masked = "*".repeat(m.getPassword().length());
        System.out.printf(format, m.getMemberId(), m.getName(), m.getUsername(), masked);

        System.out.println(separator);
    }

}
