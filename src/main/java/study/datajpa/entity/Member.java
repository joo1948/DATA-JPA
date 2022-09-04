package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of={"id","username","age"}) //양방향 되어있는것은 X
@NamedQuery(
        name="Member.findByUsername",
        query="select m from Member m where m.username = :username") //NamedQuery는 어플리케이션 로딩 시점에 sql로 파싱을 해본다. >> 이때 오류가 있으면 잡을 수 있음.
//NamedQuery가 아닌 쿼리를 직접 작성하는경우는 실제 동작할 때 오류가 발생한다. > 장애 나기 쉬움
public class Member {

    @Id
    @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String username;
    private int age;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="team_id")
    private Team team;

    /*
    >> @NoArgsConstructor로 대체

    protected Member( ) {
        //JPA는 기본생성자가 반드시 필요한데, protected까지 열어둬야함.
    }
     */

    public Member(String name) {
        this.username = name;
    }

    public Member(String username, int age, Team team){
        this.username = username;
        this.age = age;
        if(team!=null){//null이면 예외처리해야함. 지금은 X
            changeTeam(team);
        }
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    //양방향 연관관계 메서드
    public void changeTeam(Team team){
        this.team = team; //member에 있는 team 변경
        team.getMembers().add(this);//team에 있는 member 변경
    }
}