package study.datajpa.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Item implements Persistable<String> {

    //@Id @GeneratedValue
    //private Long id;

    @Id
    private String id;
    //ID를 AI로 만드는 것이 아닌 직접 ID를 넣어준다거나 @GenerateValue가 없는 경우 save 하게 되면 persist가 아닌 merge가 발생한다.
    //이를 방지하기 위해서 해당 엔티티가 취할 것
    //**주의**
    //1. Persistable<id타입> 상속
    //1-2. createdDate생성 >> Entity에 @EntityListener(AuditingEntityListener.class) 사용
    //2. 해당 id getter 생성
    //3. isNew()생성하여 null주입
    //**주의**

    @CreatedDate
    private LocalDate createdDate;


    public Item(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean isNew(){
        return createdDate == null;
    }
}
