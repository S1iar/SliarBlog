package com.sliar.sliarblog.web.admin;

import com.sliar.sliarblog.entity.Blog;
import com.sliar.sliarblog.entity.User;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT= "admin/blogs-input";
    private static final String LIST= "admin/blogs";
    private static final String REDIRECT_LIST= "redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blog, Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("page", blogService.listBlog(pageable, blog, 0));
        return LIST;
    }

    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blog, Model model){
        model.addAttribute("page", blogService.listBlog(pageable, blog , 0));
        return "admin/blogs :: blogList";
    }

    @GetMapping("/blogs/input")
    public String input(Model model){
        setTypeAndTag(model);
        model.addAttribute("blog", new Blog());
        return INPUT;
    }

    @GetMapping("/blogs/{id}/input")
    public String editinput(@PathVariable Long id, Model model){
        Blog blog = blogService.getBlog(id);
        blog.init();
        setTypeAndTag(model);
        model.addAttribute("blog", blogService.getBlog(id));
        return INPUT;
    }

    private void setTypeAndTag(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
    }

    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session){
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        if (blog.getFlag() == null || "".equals(blog.getFlag())){
            blog.setFlag("原创");
        }
        Blog b;
        if (blog.getId() == null){
            b = blogService.saveBlog(blog);
        }else {
            b = blogService.updateBlog(blog.getId(), blog);
        }

        if(b == null){
            attributes.addFlashAttribute("message","操作失敗");
        }
        attributes.addFlashAttribute("message","操作成功");
        return REDIRECT_LIST;
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message","删除成功");
        return REDIRECT_LIST;
    }

}
