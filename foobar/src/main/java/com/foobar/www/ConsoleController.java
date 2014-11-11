package com.foobar.www;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.foobar.domain.UserBean;
import com.foobar.service.ConsoleUserService;
import com.foobar.support.PaginateSupport;

@Controller
@SessionAttributes("LOGIN_USER")
@Transactional
public class ConsoleController {

    public static int PAGE_SIZE = 12;

    @Autowired
    private ConsoleUserService userService = null;

    @RequestMapping(method = RequestMethod.GET, value = "/console")
    public String doGetIndex(@ModelAttribute("LOGIN_USER") final UserBean loginUser, final Model model) {
        final Integer id = loginUser.getId();
        final UserBean userBean = this.userService.getById(id);
        model.addAttribute("userBean", userBean);
        return "console/index";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/console/follow")
    public String doGetFollow(@RequestParam(value = "p", defaultValue = "1") final Integer pageNumber,
                              @ModelAttribute("LOGIN_USER") final UserBean loginUser,
                              final Model model) {
        final PaginateSupport<UserBean> page = new PaginateSupport<>(pageNumber, ConsoleController.PAGE_SIZE);
        page.addParam("userBean", loginUser);
        this.userService.searchFollow(page);
        model.addAttribute("page", page);

        return "console/user/follow";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/console/follower")
    public String doGetFollower(@RequestParam(value = "p", defaultValue = "1") final Integer pageNumber,
                                @ModelAttribute("LOGIN_USER") final UserBean loginUser,
                                final Model model) {
        final PaginateSupport<UserBean> page = new PaginateSupport<>(pageNumber, ConsoleController.PAGE_SIZE);
        page.addParam("userBean", loginUser);
        this.userService.searchFollower(page);
        model.addAttribute("page", page);

        return "console/user/follower";
    }

}
