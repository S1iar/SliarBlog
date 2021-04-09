package com.sliar.sliarblog.dao;

import com.sliar.sliarblog.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    @Query(value = "select * from t_blog where recommend = true and published = true", nativeQuery = true)
    List<Blog> findTop(Pageable pageable);

    @Query(value = "select * from t_blog where  published = true", nativeQuery = true)
    Page<Blog> findpublishedBlogs(Pageable pageable);

    @Query(value = "select * from t_blog where title like ?1 or content like ?1 ", nativeQuery = true)
    Page<Blog> findByQuery(String query, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "update Blog b set b.views = b.views+1 where b.id = ?1")
    int updateViews(Long id);

    @Query("select function('date_format',b.updateTime,'%Y') as year from Blog b group by function('date_format',b.updateTime,'%Y') order by year desc ")
    List<String> findGroupYear();


    @Query("select b from Blog b where function('date_format',b.updateTime,'%Y') = ?1 ")
    List<Blog> findByYear(String year);
}
