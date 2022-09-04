package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    //1. 메소드 이름으로 쿼리 생성 (Spring Data JPA에서 제공) >> 이름이 계속 길어질 수 있으며, 이름이 형식과 달라지면 오류 발생
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    //2. NamedQuery 사용
    //@Query(name = "Member.findByUserName")
    List<Member> findByUsername(@Param("username") String username);


    //3. @Query, 레포지토리 메소드에 쿼리 정의 >> 제일 많이 씀 *** (정적쿼리) 동적쿼리는 Query DSL 사용할것 !
    //쿼리 문자열에 오류가 있다면, 어플리케이션 로딩 시점에 오류 발생.
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    //***DTO 조회
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

}
