package teammates.ui.webapi;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.Cookie;

import teammates.common.datatransfer.attributes.AccountAttributes;
import teammates.common.util.Const;
import teammates.common.util.HttpRequestHelper;
import teammates.common.util.StringHelper;
import teammates.ui.output.AuthInfo;

/**
 * Action: gets user authentication information.
 *
 * <p>This does not log in or log out the user.
 */
class GetAuthInfoAction extends Action {

    @Override
    public AuthType getMinAuthLevel() {
        return AuthType.PUBLIC;
    }

    @Override
    void checkSpecificAccessControl() {
        // Login information is available to everyone
    }

    @Override
    public JsonResult execute() {
        String frontendUrl = getRequestParamValue("frontendUrl");
        String nextUrl = getRequestParamValue("nextUrl");
        if (frontendUrl == null) {
            frontendUrl = "";
        }

        AuthInfo output;
        if (userInfo == null) {
            if (nextUrl == null) {
                output = new AuthInfo(
                        createLoginUrl(frontendUrl, Const.WebPageURIs.STUDENT_HOME_PAGE),
                        createLoginUrl(frontendUrl, Const.WebPageURIs.INSTRUCTOR_HOME_PAGE),
                        createLoginUrl(frontendUrl, Const.WebPageURIs.ADMIN_HOME_PAGE),
                        createLoginUrl(frontendUrl, Const.WebPageURIs.MAINTAINER_HOME_PAGE)
                );
            } else {
                output = new AuthInfo(
                        createLoginUrl(frontendUrl, nextUrl),
                        createLoginUrl(frontendUrl, nextUrl),
                        createLoginUrl(frontendUrl, nextUrl),
                        createLoginUrl(frontendUrl, nextUrl)
                );
            }
        } else {
            String googleId = userInfo.getId();
            AccountAttributes accountInfo = logic.getAccount(googleId);
            String institute = accountInfo == null ? null : accountInfo.getInstitute();
            output = new AuthInfo(userInfo, institute, authType == AuthType.MASQUERADE);
        }

        String csrfToken = StringHelper.encrypt(req.getSession().getId());
        String existingCsrfToken = HttpRequestHelper.getCookieValueFromRequest(req, Const.SecurityConfig.CSRF_COOKIE_NAME);
        if (csrfToken != null && csrfToken.equals(existingCsrfToken)) {
            return new JsonResult(output);
        }
        Cookie csrfTokenCookie = new Cookie(Const.SecurityConfig.CSRF_COOKIE_NAME, csrfToken);
        csrfTokenCookie.setPath("/");
        List<Cookie> cookieList = Collections.singletonList(csrfTokenCookie);
        return new JsonResult(output, cookieList);
    }

    String createLoginUrl(String frontendUrl, String nextUrl) {
        return Const.WebPageURIs.LOGIN + "?nextUrl=" + frontendUrl + nextUrl;
    }

}
