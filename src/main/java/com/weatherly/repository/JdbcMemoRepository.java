package com.weatherly.repository;

import com.weatherly.domain.Memo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

/**
 * Spring의 JdbcTemplate을 이용하여 SQL 쿼리를 수행
 */
@Repository
public class JdbcMemoRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * 생성자 주입을 통해 DataSource를 받아 JdbcTemplate을 초기화합니다.
     * @param dataSource Spring이 관리하는 DB 연결 DataSource
     */
    @Autowired
    public JdbcMemoRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Memo 객체를 데이터베이스에 저장합니다.
     * @param memo 저장할 Memo 객체
     * @return 저장된 Memo 객체
     */
    public Memo save(Memo memo) {
        // SQL 작성
        String sql = "insert into memo values(?, ?)";
        // update 메서드로 INSERT 실행
        jdbcTemplate.update(sql, memo.getId(), memo.getText());
        return memo;
    }

    /**
     * 데이터베이스에 저장된 모든 Memo를 조회합니다.
     * @return 조회된 Memo 리스트
     */
    public List<Memo> findAll() {
        String sql = "select * from memo";
        // query 메서드를 사용하여 전체 조회 및 RowMapper를 이용해 매핑
        return jdbcTemplate.query(sql, memoRowMapper());
    }

    /**
     * 특정 ID를 가진 Memo를 조회합니다.
     * @param id 조회할 Memo의 ID
     * @return 조회된 Memo 객체 (Optional로 반환)
     */
    public Optional<Memo> findById(int id) {
        String sql = "select * from memo where id = ?";
        // query 메서드로 조회 후 첫 번째 결과만 반환
        return jdbcTemplate.query(sql, memoRowMapper(), id).stream().findFirst();
    }

    /**
     * ResultSet의 행(row)을 Memo 객체로 변환하는 RowMapper를 제공합니다.
     * @return Memo를 매핑하는 RowMapper
     */
    private RowMapper<Memo> memoRowMapper(){
        return (rs, rowNum) -> new Memo(
                rs.getInt("id"),    // ResultSet에서 id 컬럼 가져오기
                rs.getString("text") // ResultSet에서 text 컬럼 가져오기
        );
    }

}
