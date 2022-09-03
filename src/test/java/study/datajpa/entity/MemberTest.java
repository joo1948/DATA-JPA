package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Test
    public void testEntity(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("membe1", 10, teamA);
        Member member2 = new Member("membe2", 20, teamA);
        Member member3 = new Member("membe3", 30, teamB);
        Member member4 = new Member("membe4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        //초기화
        em.flush();//강제로 DB에 쿼리를 날림
        em.clear(); //영속성 컨테스트에 있는 것들을 날림

        //확인
        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        for(Member member : members){
            System.out.println("member = " + member);
            System.out.println("-> member.team = "+ member.getTeam());
        }



    }

}