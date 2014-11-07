package com.foobar.www;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.foobar.domain.AlbumBean;
import com.foobar.domain.ArticleBean;
import com.foobar.domain.PhotoBean;
import com.foobar.domain.UserBean;
import com.foobar.exception.ForbiddenException;
import com.foobar.exception.PageNotFoundException;
import com.foobar.service.ConsoleAlbumService;
import com.foobar.support.PaginateSupport;

@Controller
@SessionAttributes("LOGIN_USER")
@RequestMapping("/console/album")
@Transactional
public class ConsoleAlbumController {

    @Autowired
    private MessageSource messageSource = null;
    @Autowired
    private ConsoleAlbumService consoleAlbumService = null;

    protected void throwExceptionWhenNotAllow(final ArticleBean articleBean,
                                              final UserBean loginUser,
                                              final Locale locale) {
        if (articleBean == null) {
            throw new PageNotFoundException(this.messageSource.getMessage("error.pageNotFound", null, locale));
        }
        if (!loginUser.equals(articleBean.getUserBean()) && !loginUser.isAdmin()) {
            throw new ForbiddenException(this.messageSource.getMessage("error.forbidden", null, locale));
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String doGetIndex(@RequestParam(value = "p", defaultValue = "1") final Integer pageNumber,
                             @RequestParam(value = "q", required = false) final String query,
                             @ModelAttribute("LOGIN_USER") final UserBean loginUser,
                             final Model model) {
        final PaginateSupport<AlbumBean> page = new PaginateSupport<>(pageNumber);
        page.addParam("query", query);
        page.addParam("userBean", loginUser);
        this.consoleAlbumService.searchAlbum(page);
        model.addAttribute("page", page);

        return "console/album/index";
    }

    @RequestMapping(value = "input", method = RequestMethod.GET)
    public String doGetInput(final Model model) {
        model.addAttribute("articleBean", new AlbumBean());
        return "console/album/input";
    }

    @RequestMapping(value = "input/single", method = RequestMethod.POST)
    public String doPostInputSingle(@ModelAttribute final AlbumBean albumBean,
                                    @ModelAttribute("LOGIN_USER") final UserBean loginUser,
                                    @RequestParam(value = "file") final MultipartFile upload,
                                    final Model model,
                                    final Locale locale) {
        albumBean.setUserBean(loginUser);
        albumBean.setType(AlbumBean.SINGLE);
        final Map<String, Object> modelMap = model.asMap();
        if (this.consoleAlbumService.doCreate(albumBean, modelMap, locale)) {
            final Map<String, Object> fileModel = new HashMap<String, Object>();
            if (this.consoleAlbumService.addPhoto(albumBean, upload, fileModel, locale)) {
                model.addAttribute("id", albumBean.getId());
                return "redirect:/console/album/{id}";
            }
            model.addAttribute("fileModel", fileModel);
        }
        model.addAttribute("articleBean", albumBean);
        return "console/album/input";
    }

    @RequestMapping(value = "input/multiple", method = RequestMethod.POST)
    public String doPostInputMultiple(@ModelAttribute final AlbumBean albumBean,
                                      @ModelAttribute("LOGIN_USER") final UserBean loginUser,
                                      final Model model,
                                      final Locale locale) {
        albumBean.setUserBean(loginUser);
        albumBean.setType(AlbumBean.MULTIPLE);
        if (this.consoleAlbumService.doCreate(albumBean, model.asMap(), locale)) {
            model.addAttribute("id", albumBean.getId());
            return "redirect:/console/album/{id}/upload";
        }
        model.addAttribute("articleBean", albumBean);
        return "console/album/input";
    }

    @RequestMapping(value = "{albumId}", method = RequestMethod.GET)
    public String doGetView(@PathVariable final Integer albumId,
                            @ModelAttribute("LOGIN_USER") final UserBean loginUser,
                            final Model model,
                            final Locale locale) {
        final AlbumBean albumBean = this.consoleAlbumService.findById(albumId);
        this.throwExceptionWhenNotAllow(albumBean, loginUser, locale);

        final List<PhotoBean> photoBeanList = this.consoleAlbumService.findPhotoByAlbum(albumBean);

        model.addAttribute("articleBean", albumBean);
        model.addAttribute("photoBeanList", photoBeanList);

        if (photoBeanList.isEmpty()) {
            return "console/album/upload";
        }

        return "console/album/view";
    }

    @RequestMapping(value = "{albumId}/upload", method = RequestMethod.GET)
    public String doGetUpload(@PathVariable final Integer albumId,
                              @ModelAttribute("LOGIN_USER") final UserBean loginUser,
                              final Model model,
                              final Locale locale) {
        final AlbumBean albumBean = this.consoleAlbumService.findById(albumId);
        this.throwExceptionWhenNotAllow(albumBean, loginUser, locale);

        model.addAttribute("articleBean", albumBean);
        return "console/album/upload";
    }

    @RequestMapping(value = "{albumId}/file", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> doGetFile(@PathVariable final Integer albumId,
                                         @ModelAttribute("LOGIN_USER") final UserBean loginUser,
                                         final Locale locale) {
        final Map<String, Object> model = new HashMap<String, Object>();

        final AlbumBean albumBean = this.consoleAlbumService.findById(albumId);
        this.throwExceptionWhenNotAllow(albumBean, loginUser, locale);
        final List<Map<String, Object>> files = new ArrayList<>();
        final List<PhotoBean> photoList = this.consoleAlbumService.findPhotoByAlbum(albumBean);
        for (final PhotoBean photoBean : photoList) {
            final Map<String, Object> fileModel = new HashMap<String, Object>();

            fileModel.put("id", photoBean.getId());
            fileModel.put("name", photoBean.getName());
            fileModel.put("albumId", albumId);

            files.add(fileModel);
        }
        model.put("files", files);
        return model;
    }

    @RequestMapping(method = RequestMethod.POST, value = "{albumId}/file")
    @ResponseBody
    public Map<String, Object> doPostFile(@PathVariable final Integer albumId,
                                          @RequestParam(value = "files[]") final MultipartFile[] uploads,
                                          @ModelAttribute("LOGIN_USER") final UserBean loginUser,
                                          final Locale locale) {
        final Map<String, Object> model = new HashMap<String, Object>();

        final AlbumBean albumBean = this.consoleAlbumService.findById(albumId);
        this.throwExceptionWhenNotAllow(albumBean, loginUser, locale);

        final List<Map<String, Object>> files = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(uploads)) {
            for (final MultipartFile upload : uploads) {

                final Map<String, Object> fileModel = new HashMap<String, Object>();
                this.consoleAlbumService.addPhoto(albumBean, upload, fileModel, locale);
                files.add(fileModel);
            }
        }
        model.put("files", files);
        return model;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{albumId}/{fileId}/delete")
    @ResponseBody
    public Map<String, Object> doPostFileDelete(@PathVariable final Integer albumId,
                                                @PathVariable final Integer fileId,
                                                @ModelAttribute("LOGIN_USER") final UserBean loginUser,
                                                final Locale locale) {
        final AlbumBean albumBean = this.consoleAlbumService.findById(albumId);
        this.throwExceptionWhenNotAllow(albumBean, loginUser, locale);

        final PhotoBean photoBean = new PhotoBean();
        photoBean.setId(fileId);
        final boolean result = this.consoleAlbumService.deletePhoto(albumBean, photoBean);

        final Map<String, Object> model = new HashMap<String, Object>();
        model.put("result", result);
        return model;
    }

    @RequestMapping(value = "{albumId}/sort", method = RequestMethod.GET)
    public String doGetSort(@PathVariable final Integer albumId,
                            @ModelAttribute("LOGIN_USER") final UserBean loginUser,
                            final Model model,
                            final Locale locale) {
        final AlbumBean albumBean = this.consoleAlbumService.findById(albumId);
        this.throwExceptionWhenNotAllow(albumBean, loginUser, locale);

        final List<PhotoBean> photoBeanList = this.consoleAlbumService.findPhotoByAlbum(albumBean);

        model.addAttribute("articleBean", albumBean);
        model.addAttribute("photoBeanList", photoBeanList);

        if (photoBeanList.isEmpty()) {
            return "console/album/upload";
        }

        return "console/album/sort";
    }

    @RequestMapping(value = "{albumId}/sort", method = RequestMethod.POST)
    public String doPostSort(@PathVariable final Integer albumId,
                             @RequestParam(value = "fileId[]") final Integer[] fileId,
                             @ModelAttribute("LOGIN_USER") final UserBean loginUser,
                             final Model model,
                             final Locale locale) {
        final AlbumBean albumBean = this.consoleAlbumService.findById(albumId);
        this.throwExceptionWhenNotAllow(albumBean, loginUser, locale);

        final List<PhotoBean> photoList = new ArrayList<PhotoBean>();
        for (final Integer id : fileId) {
            final PhotoBean photoBean = new PhotoBean();
            photoBean.setId(id);
            photoList.add(photoBean);
        }
        this.consoleAlbumService.sortPhoto(albumBean, photoList);

        return "redirect:/console/album/{albumId}";
    }
}
