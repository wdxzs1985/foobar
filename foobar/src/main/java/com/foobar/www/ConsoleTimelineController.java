package com.foobar.www;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.foobar.domain.UserBean;
import com.foobar.service.ConsolePhotoService;

@Controller
@SessionAttributes("LOGIN_USER")
@RequestMapping("/console/timeline")
@Transactional
public class ConsoleTimelineController {

    @Autowired
    private ConsolePhotoService consolePhotoService = null;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String doGetGroup(@ModelAttribute("LOGIN_USER") final UserBean loginUser, final Model model) {
        final List<Map<String, Object>> groups = this.consolePhotoService.findUserPhotoGroup(loginUser);
        model.addAttribute("groups", groups);
        return "console/timeline/index";
    }

    @RequestMapping(value = "/{year}", method = RequestMethod.GET)
    public String doGetGroupByYear(@PathVariable final int year,
                                   @ModelAttribute("LOGIN_USER") final UserBean loginUser,
                                   final Model model) {
        final List<Map<String, Object>> groups = this.consolePhotoService.findUserPhotoGroup(loginUser, year);
        model.addAttribute("groups", groups);
        return "console/timeline/year";
    }

    @RequestMapping(value = "/{year}/{month}", method = RequestMethod.GET)
    public String doGetGroupByYearMonth(@PathVariable final int year,
                                        @PathVariable final int month,
                                        @ModelAttribute("LOGIN_USER") final UserBean loginUser,
                                        final Model model) {
        final List<Map<String, Object>> groups = this.consolePhotoService.findUserPhotoGroup(loginUser, year, month);
        model.addAttribute("groups", groups);
        return "console/timeline/month";
    }

    @RequestMapping(value = "/{year}/{month}/{day}", method = RequestMethod.GET)
    public String doGetGroupByYearMonthDay(@PathVariable final int year,
                                           @PathVariable final int month,
                                           @PathVariable final int day,
                                           @ModelAttribute("LOGIN_USER") final UserBean loginUser,
                                           final Model model) {
        final List<Map<String, Object>> groups = this.consolePhotoService.findUserPhotoGroup(loginUser,
                                                                                             year,
                                                                                             month,
                                                                                             day);
        model.addAttribute("groups", groups);
        return "console/timeline/day";
    }
}
