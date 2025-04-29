package com.weatherly.repository;

import com.weatherly.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMemoRepository extends JpaRepository<Memo, Integer> {
}
