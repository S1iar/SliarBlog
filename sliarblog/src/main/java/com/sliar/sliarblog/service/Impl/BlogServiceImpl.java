package com.sliar.sliarblog.service.Impl;

import com.sliar.sliarblog.NotFoundException;
import com.sliar.sliarblog.dao.BlogRepository;
import com.sliar.sliarblog.entity.Blog;
import com.sliar.sliarblog.entity.Type;
import com.sliar.sliarblog.service.BlogService;
import com.sliar.sliarblog.util.MarkdownUtils;
import com.sliar.sliarblog.util.MyBeanUtils;
import com.sliar.sliarblog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Blog getBlog(Long id) {
        return blogRepository.findById(id).get();
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog, int published) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null ){
                    predicates.add(cb.like(root.<String>get("title"), "%"+blog.getTitle()+"%"));
                }
                if (blog.getTypeId() != null){
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if (blog.isRecommend()){
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                if (published == 1 ){
                    predicates.add(cb.equal(root.<Boolean>get("published"), published));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, String query) {
        return blogRepository.findByQuery(query, pageable);
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("tags");
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal(root.<Boolean>get("published"), 1));
                predicates.add(cb.equal(join.get("id"), tagId));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.findById(id).orElse(null);
        if (blog == null){
            throw new NotFoundException("该博客不存在");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog, b);

        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        blogRepository.updateViews(id);
        return b;
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String,List<Blog>> map = new HashMap<>();
        for (String year : years){
            map.put(year,blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }

    @Override
    public Page<Blog> listBlogOnpublishedBlogs(Pageable pageable) {
        return blogRepository.findpublishedBlogs(pageable);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "update_time");
        Pageable pageable = PageRequest.of(0,size, sort);
        return blogRepository.findTop(pageable);
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);

        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.findById(id).orElse(null);
        if (b == null){
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullProperyNames(blog));
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
