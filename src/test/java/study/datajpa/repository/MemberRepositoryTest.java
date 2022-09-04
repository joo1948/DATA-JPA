package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext EntityManager em;

    @Test
    public void testMember(){
        Member member = new Member("memberA");

        Member savedMember = memberRepository.save(member);

        Member findMember= memberRepository.findById(savedMember.getId()).get();
        //현재 MemberRepository 인터페이스에 따로 save, findById등을 구현하지 않았는데 사용하고 있음.


        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건조회 건
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);


        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deleteCount = memberRepository.count();
        assertThat(deleteCount).isEqualTo(0);


    }

    @Test
    public void findByUsernameAndAgeGreaterThan(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20 );
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    public void testNamedQuery(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20 );
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("AAA");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);

    }

    @Test
    public void testQuery(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20 );
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        assertThat(result.get(0)).isEqualTo(m1);

    }

    @Test
    public void fdinUserNameList(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20 );
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> result = memberRepository.findUsernameList();
        for(String s : result){
            System.out.println("s = "+ s);
        }

    }

    @Test
    public void findMemberDto(){
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10);
        memberRepository.save(m1);


        List<MemberDto> result = memberRepository.findMemberDto();
        for(MemberDto dto : result){
            System.out.println("dto = " + dto);
        }

    }

    @Test
    public void findByNames(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20 );
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA","BBB"));
        for(Member m : result){
            System.out.println("m = " + m.getUsername());
        }

    }

    @Test
    public void returnTypeTest(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20 );
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member>  listMember = memberRepository.findListByUsername("AAASDFSDF"); //List는 값이 없는 경우 List에 아무것도 안들어감. if(listMember != null) 이런 식의 코드 필요 X
        Member findMember = memberRepository.findMemberByUsername("AAASDFSDF"); //단건 조회를 할 경우 값이 없다면 Member에 null이 들어감
        Optional<Member> optionalMember = memberRepository.findOptionalByUsername("asdfsf");//null인 경우 Optional 사용 (Optional은 null인 경우 empty로 조회됨.)

        System.out.println("optionalMember = " + optionalMember);

        //만약 값이 두개 이상일 경우 ?
        //Optional<Member> optionalMembertWo = memberRepository.findOptionalByUsername("AAA");
        //IncorrectResultSizeDataAccessException 오류 반환해줌.
    }

    @Test
    public void paging(){
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        //jpa는 페이징 시작하는 것이 0임. >> 0페이지에서 3개를 가져오고 username을 DESC로 할거야


        //when
        //1.Page
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        //> DB에서 바로 전달받은 page를 Controller에서 바로 반환 하면 안됨.
        //>> Entity가 바뀌게 된다면 API스팩이 달라지므로, 장애 발생 위험도가 높아짐
        //페이징 처리를 하면서 totalcount에 관한 쿼리도 날려줌

        //2.Slice
        //Slice<Member> page = memberRepository.findByAge(age, pageRequest);

        //3. List >> 페이징 관련된 메서드는 사용 X 그냥 단순 limit 쿼리는 날라감
        //List<Member> page = memberRepository.findByAge(age, pageRequest);

        //then
        Page<MemberDto> toMap = page.map(m -> new MemberDto(m.getId(), m.getUsername(), m.getTeam().getName())); //Entity를 그대로 반환하는 것이 아닌 Dto로 변경하여 반환할 것 .


        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        assertThat(content.size()).isEqualTo(3);//컨텐츠 나오는 개수
        assertThat(page.getTotalElements()).isEqualTo(5);//
        assertThat(page.getNumber()).isEqualTo(0);//페이지 번호 가져올 수 있음 (page.getNumber())
        assertThat(page.getTotalPages()).isEqualTo(2);//전체 페이지 개수 (page.getTotalPages())
        assertThat(page.isFirst()).isTrue();//첫번째 페이지인지 확인
        assertThat(page.hasNext()).isTrue();//다음 페이지가 있는지 확인



        //2. 반환값 Slice에서 사용 >> 차이점 Slice는 limit값에 +1을 하여 가져옴. ==> 페이지 갯수 관련된 메서드 제공 X
        /*
        ///실행된 쿼리 : select member0_.member_id as member_i1_0_, member0_.age as age2_0_, member0_.team_id as team_id4_0_, member0_.username as username3_0_ from member member0_ where member0_.age=10 order by member0_.username desc limit 4;
        List<Member> content = page.getContent();
        //long totalElements = page.getTotalElements(); >>Slice에서 사용 X

        assertThat(content.size()).isEqualTo(3);//컨텐츠 나오는 개수
        //assertThat(page.getTotalElements()).isEqualTo(5);// >> Slice에서 사용 X
        assertThat(page.getNumber()).isEqualTo(0);//페이지 번호 가져올 수 있음 (page.getNumber())
        //assertThat(page.getTotalPages()).isEqualTo(2);// >> Slice에서 사용 X
        assertThat(page.isFirst()).isTrue();//첫번째 페이지인지 확인
        assertThat(page.hasNext()).isTrue();//다음 페이지가 있는지 확인

         */


    }


    @Test
    public void bulkUpdate(){
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);

        List<Member> result = memberRepository.findByUsername("member5");
        Member member5 = result.get(0);
        System.out.println("member5 >> "+ member5);//영속성 컨텍스트가 초기화 되어 있지 않다면 40그대로 남음


        //then
        assertThat(resultCount).isEqualTo(3);
    }




}