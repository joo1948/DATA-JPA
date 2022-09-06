package study.datajpa.entity.dto;

import lombok.Getter;
import study.datajpa.entity.Member;

@Getter
public class MemberDto {

    private Long id;
    private String username;
    private String teamName;

    public MemberDto(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    //Dto는 entity를 참조해도 상관 X
    public MemberDto(Member member){
        this.id= member.getId();
        this.username = member.getUsername();
    }
}
