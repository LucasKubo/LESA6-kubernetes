package MeuRemedio.app.service;

import MeuRemedio.app.controllers.EnvioEmail;
import MeuRemedio.app.models.usuarios.Usuario;
import MeuRemedio.app.repository.UsuarioRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    EnvioEmail emailCadastro;

    public boolean verificaExistencia(String email) {
        if(usuarioRepository.findByEmail(email) != null){
            return true;
        }
        return false;
    }

    public void cadastrar (Usuario usuario, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String randomCode = RandomString.make(64);
        usuario.setVerificationCode(randomCode);
        usuario.setEnabled(false);
        usuarioRepository.save(usuario);
        emailCadastro.emailValidacaoCadastro(usuario,getSiteURL(request));
    }

    public boolean verificarCodigoDeCadastro(String verificationCode) {
        Usuario usuario = usuarioRepository.findByVerificationCode(verificationCode);

        if (usuario == null || usuario.isEnabled()) {
            return false;
        } else {
            usuario.setVerificationCode(null);
            usuario.setEnabled(true);
            usuarioRepository.save(usuario);
            return true;
        }

    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}
