package com.sliar.sliarblog.web;

import com.sliar.sliarblog.NotFoundException;
import com.sliar.sliarblog.entity.Type;
import com.sliar.sliarblog.service.BlogService;
import com.sliar.sliarblog.service.TagService;
import com.sliar.sliarblog.service.TypeService;
import com.sliar.sliarblog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String index(@PageableDefault(size = 5, sort = {"update_time"}, direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blog, Model model){
        model.addAttribute("page", blogService.listBlogOnpublishedBlogs(pageable));
        model.addAttribute("types", typeService.listTypeTop(6));
        model.addAttribute("tags", tagService.listTagTop(10));
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(5));
        return "index";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model){
        model.addAttribute("blog", blogService.getAndConvert(id));
        return "blog";
    }

    @PostMapping("/search")
    public String search(@PageableDefault(size = 5, sort = {"update_time"}, direction = Sort.Direction.DESC) Pageable pageable, @RequestParam String query, Model model){
        model.addAttribute("page", blogService.listBlog(pageable, "%"+query+"%"));
        model.addAttribute("query", query);
        return "search";
    }


}
