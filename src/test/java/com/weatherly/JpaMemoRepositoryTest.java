package com.weatherly;

import com.weatherly.domain.Memo;
import com.weatherly.repository.JpaMemoRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class JpaMemoRepositoryTest {

    @Autowired
    JpaMemoRepository memoRepository;

    @Test
    void insertMemoTest(){
        Memo memo = new Memo(); // ID는 자동으로
        memo.setText("this is a jpa memo");

        memoRepository.save(memo);

        List<Memo> memos = memoRepository.findAll();
        assertTrue(memos.size() > 0);
    }


    @Test
    void findMemoTest(){
        Memo memo = new Memo();
        memo.setText("jpa");

        Memo savedMemo = memoRepository.save(memo);

        Optional<Memo> memoOptional = memoRepository.findById(savedMemo.getId());
        assertEquals("jpa", memoOptional.get().getText());
    }



}
