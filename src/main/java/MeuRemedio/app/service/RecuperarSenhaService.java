package MeuRemedio.app.service;

import MeuRemedio.app.models.usuarios.TokenResetarSenha;
import MeuRemedio.app.repository.TokenResetarSenhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RecuperarSenhaService {
    @Autowired
    TokenResetarSenhaRepository tokenResetarSenhaRepository;

    public String validarTokenResetarSenha(String token){
        final TokenResetarSenha tokenValido = tokenResetarSenhaRepository.findByToken(token);
        return !isTokenFound(tokenValido) ? "invalidToken"
                : isTokenExpired(tokenValido) ? "expired"
                : null;
    }

    private boolean isTokenFound(TokenResetarSenha passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(TokenResetarSenha passToken) {
        final LocalDateTime localDateTime = LocalDateTime.now();
        return passToken.getDataExpiracao().isBefore(localDateTime);
    }
}
