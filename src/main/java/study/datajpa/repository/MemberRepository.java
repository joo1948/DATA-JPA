package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);


    //반환타입 예시
    List<Member> findListByUsername(String username);//컬렉션
    Member findMemberByUsername(String username);//단건
    Optional<Member> findOptionalByUsername(String username);//단건


    //spring data jpa 페이징
    @Query(value = "select m from Member m",countQuery = "select count(m) from Member m") // >> 쿼리문에 join이 있을 경우 , count만 하는데 조인이 걸릴 수 있음 > 그럴 경우 count쿼리는 단순하게 @Query로 만들어서 사용한다.
    //1.Page
    Page<Member> findByAge(int age, Pageable pagealbe);
    //2. Slice
    //Slice<Member> findByAge(int age, Pageable pa `  gealbe);


    //벌크연산
    @Modifying(clearAutomatically = true)//영속성 컨텍스트 자동으로 초기화 해주는 것(clearAutomatically = true)
    //순수 JPA에 있는 .executeUpdate()와 같은 것! 벌크연산을 Spring DataJPA로 만들라면 꼭 써줄 것
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

}
