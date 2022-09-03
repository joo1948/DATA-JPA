package study.datajpa.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    protected Member( ) {
        //JPA는 기본생성자가 반드시 필요한데, protected까지 열어둬야함.
    }

    public Member(String name) {
        this.name = name;
    }
}