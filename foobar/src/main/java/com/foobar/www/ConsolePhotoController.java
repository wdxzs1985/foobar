package com.foobar.www;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.foobar.document.PhotoDocument;
import com.foobar.domain.UserBean;
import com.foobar.service.ConsolePhotoService;

@Controller
@SessionAttributes("LOGIN_USER")
@RequestMapping("/console/photo")
@Transactional
public class ConsolePhotoController {

    @Autowired
    private ConsolePhotoService consolePhotoService = null;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String doGetIndex(@ModelAttribute("LOGIN_USER") final UserBean loginUser,
                             final Model model,
                             final Pageable pageable) {
        final Page<PhotoDocument> page = this.consolePhotoService.findUserPhotos(loginUser, pageable);
        model.addAttribute("page", page);
        return "console/photo/index";
    }

}
