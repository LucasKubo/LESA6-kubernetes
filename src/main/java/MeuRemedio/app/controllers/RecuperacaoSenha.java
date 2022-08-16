package MeuRemedio.app.controllers;

import MeuRemedio.app.controllers.EnvioEmailController;
import MeuRemedio.app.models.usuarios.Usuario;
import MeuRemedio.app.repository.UsuarioRepository;
import MeuRemedio.app.service.utils.ValidateAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;

@Controller
public class RecuperacaoSenha {
    @Autowired
    EnvioEmailController envioEmailController;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    ValidateAuthentication validateAuthentication;

    @RequestMapping(value = "/enviarEmail", method = RequestMethod.GET)
    public String receberEmail(){
        /*Remover a linha 30, somente para teste ela*/
        envioEmailController.emailRecuperarSenha("eric.jin300@gmail.com", codigo());
        return "EmailRecuperacao";
    }
    @RequestMapping(value = "/recuperar_senha", method = RequestMethod.GET)
    public String atualizarSenha(){

        return "RecuperarSenha";
    }

    @RequestMapping(value = "/enviarEmail", method = RequestMethod.POST)
    public String receberEmail (@RequestParam("US_Email") String email) {
       envioEmailController.emailRecuperarSenha(email, codigo());

       return email;
    }

    @RequestMapping(value = "/recuperar_senha", method = RequestMethod.POST)
    public String atualizarSenha(@RequestParam("US_Codigo") String codigo, @RequestParam("US_Senha") String senha){

        if (codigo.equals(codigo())){
           Usuario usuario = usuarioRepository.findByEmail(receberEmail());
           usuario.setSenha(new BCryptPasswordEncoder().encode(senha));
           usuarioRepository.save(usuario);

           return "/";
        }
        return "RecuperarSenha";
    }

    public String codigo (){
        int[] codigo = new int [4];
        Random random = new Random();
        String codValidacao= "";

        for (int i = 0; i < 4; i ++ ){
            codigo[i] = random. nextInt(10);
            codValidacao += "" + codigo[i];
        }
        return codValidacao;
    }
}
