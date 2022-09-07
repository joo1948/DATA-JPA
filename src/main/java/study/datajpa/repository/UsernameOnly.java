package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

//인터페이스 기반 Projection
public interface UsernameOnly {
    //username만 가져오게 하고 싶다.
    @Value(("#{target.username + ' ' + target.age}"))
    //이렇게 @Value를 사용한다면 query로 가져올 때는 전체 엔티티를 가져와서 애플리케이션에서 username과 age만 넣어준다.
    String getUsername();
    //인터페이스를 구현하면, 관련된 구현 클래스 내용은 Spring dataJpa가 만들어준다.
    //따라서 내가 username만 가져오라고 한 것도 아닌데 알아서 가져와줌.
}
