package com.foobar.interceptor;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.foobar.CommonConstants;
import com.foobar.domain.UserBean;

@Component
public class GuestAuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private MessageSource messageSource = null;

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws Exception {
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        final HttpSession session = request.getSession();
        final UserBean loginUser = (UserBean) session.getAttribute(CommonConstants.LOGIN_USER);
        if (loginUser == null) {
            final Locale locale = request.getLocale();
            final String nickname = this.messageSource.getMessage("application.guest.nickname",
                                                                  null,
                                                                  locale);

            final UserBean guestUser = new UserBean();
            guestUser.setId(0);
            guestUser.setNickname(nickname);
            guestUser.setOnlyTranslateFlg(CommonConstants.CHR_FALSE);
            guestUser.setOnlyVocalFlg(CommonConstants.CHR_FALSE);
            guestUser.setIncludeRedFlg(CommonConstants.CHR_FALSE);

            session.setAttribute(CommonConstants.LOGIN_USER, guestUser);
        }
        return true;
    }
}
