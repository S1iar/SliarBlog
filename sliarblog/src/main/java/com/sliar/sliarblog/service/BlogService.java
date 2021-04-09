package com.sliar.sliarblog.service;

import com.sliar.sliarblog.entity.Blog;
import com.sliar.sliarblog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {
    Blog getBlog(Long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog, int published);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlog(Pageable pageable, String query);

    Page<Blog> listBlog(Long tagId, Pageable pageable);

    Blog getAndConvert(Long id);

    Map<String, List<Blog>> archiveBlog();

    Long countBlog();

    List<Blog> listRecommendBlogTop(Integer size);

    Page<Blog> listBlogOnpublishedBlogs(Pageable pageable);

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id, Blog blog);

    void deleteBlog(Long id);
}
