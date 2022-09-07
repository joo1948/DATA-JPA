package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.entity.Item;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired ItemRepository itemRepository;


    @Test
    public void save(){
        Item item = new Item("A");
        //id를 현재 임의로 생성함 >> save할 때 persist가 아닌 merge가 발생 !!
        //>>Persistable<id타입> 사용
        itemRepository.save(item);
    }

}