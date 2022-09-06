package study.datajpa.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.entity.Member;
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

    @PostConstruct
    public void init(){
        memberRepository.save(new Member("userA"));
    }

}
