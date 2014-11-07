package com.foobar.www;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.foobar.domain.UserBean;
import com.foobar.service.ConsolePhotoService;

@Controller
@SessionAttributes("LOGIN_USER")
@RequestMapping("/console/photo")
@Transactional
public class ConsolePhotoController {

    @Autowired
    private ConsolePhotoService consolePhotoService = null;

    @RequestMapping(value = "upload", method = RequestMethod.GET)
    public String doGetUpload(@ModelAttribute("LOGIN_USER") final UserBean loginUser,
                              final Model model,
                              final Locale locale) {

        return "console/photo/upload";
    }

    @RequestMapping(method = RequestMethod.POST, value = "upload/file")
    @ResponseBody
    public Map<String, Object> doPostFile(@RequestParam(value = "files[]") final MultipartFile[] uploads,
                                          @ModelAttribute("LOGIN_USER") final UserBean loginUser,
                                          final Locale locale) {
        final Map<String, Object> model = new HashMap<String, Object>();

        final List<Map<String, Object>> files = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(uploads)) {
            for (final MultipartFile upload : uploads) {

                final Map<String, Object> fileModel = new HashMap<String, Object>();
                this.consolePhotoService.addPhoto(loginUser, upload, fileModel, locale);
                files.add(fileModel);
            }
        }
        model.put("files", files);
        return model;
    }
}
