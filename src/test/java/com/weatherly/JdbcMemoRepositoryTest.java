package com.weatherly;

import com.weatherly.domain.Memo;
import com.weatherly.repository.JdbcMemoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * JdbcMemoRepository의 기능을 테스트하는 클래스
 */
@SpringBootTest
public class JdbcMemoRepositoryTest {

    @Autowired
    JdbcMemoRepository memoRepository; // JdbcMemoRepository 빈을 주입받습니다.
    @Autowired
    private JdbcMemoRepository jdbcMemoRepository;

    /**
     * 메모를 DB에 저장하는 기능(insert)을 테스트하는 메서드입니다.
     */
    @Test
    void insertMemoTest() {
        // given
        Memo memo = new Memo(2, "this is new memo");

        // when
        memoRepository.save(memo);

        //then
       Optional<Memo> memoOptional = memoRepository.findById(2);
       assertEquals(memoOptional.get().getText(), "this is new memo");
    }

    @Test
    void findMemoByIdTest() {
        List<Memo> memoList = jdbcMemoRepository.findAll();
        System.out.println(memoList);
        assertNotNull(memoList);
    }
}
