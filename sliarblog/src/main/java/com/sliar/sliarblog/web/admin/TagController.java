package com.sliar.sliarblog.web.admin;

import com.sliar.sliarblog.entity.Tag;
import com.sliar.sliarblog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public String Tags(
            @PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
             Model model){

        model.addAttribute("page",tagService.listTag(pageable));

        return "admin/tags";
    }

    @GetMapping("/tags/input")
    public String input(Model model){
        model.addAttribute("tag", new Tag());
        return "admin/tags-input";
    }

    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("tag",tagService.getTag(id));
        return "admin/tags-input";
    }

    @PostMapping("/tags")
    public String post(@Valid Tag Tag, BindingResult result, RedirectAttributes redirectAttributes){
        Tag Tag1 = tagService.getTagByName(Tag.getName());
        if (Tag1 != null){
            result.rejectValue("name","nameError","已存在该标签");
            return "admin/tags-input";
        }

        if (result.hasErrors()){
            return "admin/tags-input";
        }

        Tag t = tagService.saveTag(Tag);
        if (t == null){
            redirectAttributes.addFlashAttribute("message","操作失败");
        }else{
            redirectAttributes.addFlashAttribute("message","操作成功");
        }
        return "redirect:/admin/tags";
    }

    @PostMapping("/tags/{id}")
    public String editPost(@Valid Tag Tag, BindingResult result,@PathVariable Long id, RedirectAttributes redirectAttributes){
        Tag Tag1 = tagService.getTagByName(Tag.getName());
        if (Tag1 != null){
            result.rejectValue("name","nameError","已存在该标签");
            return "admin/tags-input";
        }

        if (result.hasErrors()){
            return "admin/tags-input";
        }

        Tag t = tagService.updateTag(id,Tag);
        if (t == null){
            redirectAttributes.addFlashAttribute("message","操作失败");
        }else{
            redirectAttributes.addFlashAttribute("message","操作成功");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes){
        tagService.deleteTag(id);
        redirectAttributes.addFlashAttribute("message","操作成功");
        return "redirect:/admin/tags";
    }

}
