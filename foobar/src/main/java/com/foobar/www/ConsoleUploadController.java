package com.foobar.www;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
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

import com.foobar.domain.PhotoBean;
import com.foobar.domain.UserBean;
import com.foobar.service.ConsolePhotoService;

@Controller
@SessionAttributes("LOGIN_USER")
@RequestMapping("/console/upload")
@Transactional
public class ConsoleUploadController {

    @Autowired
    private ConsolePhotoService consolePhotoService = null;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String doGetUpload(@ModelAttribute("LOGIN_USER") final UserBean loginUser,
                              final Model model,
                              final Locale locale) {

        return "console/upload";
    }

    @RequestMapping(method = RequestMethod.GET, value = "file")
    @ResponseBody
    public Map<String, Object> doGetFile(@ModelAttribute("LOGIN_USER") final UserBean loginUser, final Locale locale) {
        final Map<String, Object> model = new HashMap<String, Object>();
        final List<Map<String, Object>> files = new ArrayList<>();
        final Calendar calendar = Calendar.getInstance(locale);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH) + 1;
        final int date = calendar.get(Calendar.DATE);
        final List<Map<String, Object>> photoGroup = this.consolePhotoService.findUserPhotoGroup(loginUser,
                                                                                                 year,
                                                                                                 month,
                                                                                                 date);
        if (CollectionUtils.isNotEmpty(photoGroup)) {
            for (final Map<String, Object> map : photoGroup) {
                @SuppressWarnings("unchecked")
                final List<PhotoBean> content = (List<PhotoBean>) map.get("content");
                for (final PhotoBean photoBean : content) {
                    final Map<String, Object> fileModel = new HashMap<String, Object>();
                    fileModel.put("id", photoBean.getId());
                    fileModel.put("name", photoBean.getName());
                    files.add(fileModel);
                }
            }
        }
        model.put("files", files);
        return model;
    }

    @RequestMapping(method = RequestMethod.POST, value = "file")
    @ResponseBody
    public Map<String, Object> doPostFile(@RequestParam(value = "files[]") final MultipartFile[] uploads,
                                          @ModelAttribute("LOGIN_USER") final UserBean loginUser,
                                          final Locale locale) {
        final Map<String, Object> model = new HashMap<String, Object>();
        final List<Map<String, Object>> files = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(uploads)) {
            for (final MultipartFile upload : uploads) {
                final Map<String, Object> fileModel = new HashMap<String, Object>();
                final PhotoBean photoBean = this.consolePhotoService.addPhoto(loginUser, upload, fileModel, locale);
                fileModel.put("id", photoBean.getId());
                fileModel.put("name", photoBean.getName());
                files.add(fileModel);
            }
        }
        model.put("files", files);
        return model;
    }
}
