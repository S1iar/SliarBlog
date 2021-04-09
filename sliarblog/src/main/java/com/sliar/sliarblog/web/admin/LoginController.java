package com.sliar.sliarblog.web.admin;


import com.sliar.sliarblog.entity.User;
import com.sliar.sliarblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    //跳转登陆页面
    @GetMapping
    public String loginPage(){
        return "admin/login";
    }

    //登陆
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, RedirectAttributes attributes) {
        User user = userService.checkUser(username,password);
        if (user != null){
            user.setPassword(null);
            session.setAttribute("user",user);
            return "admin/index";
        }

        attributes.addFlashAttribute("message","用户名和密码错误");
        return "redirect:/admin"; //重定向到 /admin的请求
    }

    @GetMapping("/login")
    public String login2(){
        return "redirect:/admin";
    }

    //登出
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/admin";
    }
}
