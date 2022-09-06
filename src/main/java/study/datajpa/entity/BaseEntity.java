package study.datajpa.entity;

import lombok.Getter;
import net.bytebuddy.asm.Advice;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class) //이런 공통 엔티티가 많은 경우 META-INF 에 META-INF/orm.xml를 넣어준다. >> 전체적용
@MappedSuperclass
@Getter
//스프링 데이타 JPA로 구현
public class BaseEntity extends BaseTimeEntity{ //왜 시간과 등록/수정자를 구분지은 것인가 ? >> 시간은 거의 모든 곳에서 사용하지만, 등록자&수정자는 사용하는 부분이 있을수도 없을수도 있기 때문.
    //시간만 사용한다면 Entity에 BaseTimeEntity만 상속하고, 등록자+시간 포함하려면 BaseENtity를 상속해주면 되겠지 ?



    //특정 사용자 코드를 넣어야 하는 경우
    //DataJpaApplication에 @Bean 생성하여 적용한다. 현재는 그냥 랜덤값으로 넣어주었지만 실무에서는 시큐리티를 사용하여 세션 등을 넣어줘야함.
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;

}
