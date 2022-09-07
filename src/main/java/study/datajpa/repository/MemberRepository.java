package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

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
    @Query(value = "select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t", nativeQuery = true)
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


    //패치 조인
    //JPQL 사용한 것
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    //EntityGraph 사용
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    //EntityGraph + JPQL 사용
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph(String member1);

    //쿼리 메서드 이름으로 판단 + EntityGraph
    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    @EntityGraph("Member.all")
    List<Member> findNamedEntityGraphByUsername(@Param("username") String username);

    //DB에서 값을 불러온 후 just 화면에 뿌려주는 용도 == read만 하는 용도로 사용할거야
    //근데 JPA라서 setter를 하게 된다면 변경감지 일어남 !
    //JPA Hint를 사용 (@QueryHints, @QueryHint)
    //많이 사용 ㄴㄴ
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

    //interface 기반 projection
    List<UsernameOnly> findProjectionsByUsername(@Param("username") String username);

    //class 기반 projection
    List<NestedClosedProjection> findProjectionsByUsername(@Param("username") String username);

}
