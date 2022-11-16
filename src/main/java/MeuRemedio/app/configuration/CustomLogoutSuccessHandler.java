package MeuRemedio.app.configuration;

import MeuRemedio.app.repository.UsuarioNotificationTokenRepository;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class CustomLogoutSuccessHandler extends
        SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {


    @Autowired
    UsuarioNotificationTokenRepository usuarioNotificationTokenRepository;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        Cookie[] cookies = request.getCookies();
        if(cookies!=null) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("tokenNotification")) {
                    var token = usuarioNotificationTokenRepository.findByIdToken(cookie.getValue());
                    usuarioNotificationTokenRepository.delete(token);
                }
            }
        }
        request.getSession().invalidate();
        response.sendRedirect("/login?logout");
    }
}