package com.sliar.sliarblog.web.admin;

import com.sliar.sliarblog.entity.Tag;
import com.sliar.sliarblog.service.BlogService;
import com.sliar.sliarblog.service.TagService;
import com.sliar.sliarblog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TagShowController {

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/tags/{id}")
    public String tags(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blog, @PathVariable Long id, Model model){
        List<Tag> tags = tagService.listTagTop(10000);
        if (id == -1){
            id = tags.get(0).getId();
        }
        //published
        model.addAttribute("tags",tags);
        model.addAttribute("page",blogService.listBlog(id,pageable));
        model.addAttribute("activeTagId",id);
        return "tags";
    }
}
