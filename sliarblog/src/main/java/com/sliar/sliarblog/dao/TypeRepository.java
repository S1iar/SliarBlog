package com.sliar.sliarblog.dao;

import com.sliar.sliarblog.entity.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface TypeRepository extends JpaRepository<Type,Long> {
    Type findByName(String name);


    @Query(value = "select id,name from t_type", nativeQuery = true)
    List<Type> findTop(Pageable pageable);
}
