package com.sliar.sliarblog.dao;

import com.sliar.sliarblog.entity.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Long> {
    Tag findByName(String name);

    @Query(value = "select id,name from t_tag", nativeQuery = true)
    List<Tag> findTop(Pageable pageable);
}
