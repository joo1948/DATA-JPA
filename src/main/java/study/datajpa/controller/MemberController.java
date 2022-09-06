package study.datajpa.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.entity.Member;
import study.datajpa.entity.dto.MemberDto;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member){ //위의 findMember메서드와 동일한 결과가 나옴
        //Spring data jpa가 id 값을 도메인 컨버터가 중간에서 동작하여 회원 엔티티를 바로 반환해줌
        //꼭 조회 할 때만 사용되어야 함. >> 트랜잭션이 없기 때문
        return member.getUsername();
    }

    //페이징
    @GetMapping("/members")
    //localhost:8080/members?page=1&size=3&sort=username,desc
    ///members뒤에 ?page=1&size=3&sort=username,desc넣어서 사용 가능
    //page : 페이지 번호 , size : 한 페이지에 몇개 조회할지, sort : sort 할 것 >> 안할 경우는 default가 20개 가져옴
    //default 값 변경하려면 application.yml변경 or 해당 메서드에만 특정 값 줄 땐 @PageableDefault 사용
    public Page<MemberDto> list(@PageableDefault(size=5) Pageable pageable){
        Page<Member> page = memberRepository.findAll(pageable);
        Page<MemberDto> pageDto = page.map(MemberDto::new);
        return pageDto;
    }

    //Page번호를 0이 아닌 1부터 시작하게 하고 싶다.>> pageable를 본인이 선언하기
    @GetMapping("/members/pagingNumOne")
    public Page<MemberDto> listPageNumOne(@PageableDefault(size=5) Pageable pageable){
        PageRequest request = PageRequest.of(1, 2);
        Page<Member> page = memberRepository.findAll(request);
        Page<MemberDto> pageDto = page.map(MemberDto::new);
        return pageDto;
    }
    
    @PostConstruct //spring 실행 될 때 해당 메서드 실행됨.
    public void init(){
        for(int i=0;i<100;i++){
            memberRepository.save(new Member("user"+i, i, null));
        }
    }

}
