package MeuRemedio.app.controllers;

import MeuRemedio.app.controllers.EnvioEmailController;
import MeuRemedio.app.models.usuarios.Usuario;
import MeuRemedio.app.models.usuarios.Usuario_code;
import MeuRemedio.app.repository.UserCodeRepository;
import MeuRemedio.app.repository.UsuarioRepository;
import MeuRemedio.app.service.utils.ValidateAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;
import java.util.Random;

@Controller
public class RecuperacaoSenha {
    @Autowired
    EnvioEmailController envioEmailController;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    UserCodeRepository usuarioCode;


    protected String emailUsuario;
    @RequestMapping(value = "/enviarEmail", method = RequestMethod.GET)
    public String receberEmail(){
        /*Remover a linha 30, somente para teste ela*/
       // envioEmailController.emailRecuperarSenha("eric.jin300@gmail.com", codigo());
        return "EmailRecuperacao";
    }
    @RequestMapping(value = "/recuperar_senha", method = RequestMethod.GET)
    public String atualizarSenha(){
        return "RecuperarSenha";
    }

    @RequestMapping(value = "/enviarEmail", method = RequestMethod.POST)
    public String receberEmail (@RequestParam("US_Email") String email) {
       try{
           emailUsuario = email;
           Usuario_code user = new Usuario_code(email, codigo());
           usuarioCode.save(user);

           Usuario_code userEmail = usuarioCode.findByEmail(email);
           envioEmailController.emailRecuperarSenha(userEmail.getEmail(), userEmail.getCodigo());

           return "redirect:/login";

       }catch (Exception e){
           return "TemplateError";
       }
    }

    @RequestMapping(value = "/recuperar_senha", method = RequestMethod.POST)
    public String atualizarSenha (@RequestParam("US_Codigo") String codigo, @RequestParam("US_Senha") String senha){
        Usuario_code userCodigo = usuarioCode.findByCodigo(codigo);

      if (Objects.nonNull(userCodigo) ){
           Usuario usuario = usuarioRepository.findByEmail(userCodigo.getEmail());
           usuario.setSenha(new BCryptPasswordEncoder().encode(senha));
           usuarioRepository.save(usuario);
           usuarioCode.delete(userCodigo);

           return "redirect:/login";
       }
            return "RecuperarSenha";
    }

    //Teste
    public String codigo (){
        int[] codigo = new int [8];
        Random random = new Random();
        String codValidacao= "";

        for (int i = 0; i < 8; i ++ ){
            codigo[i] = random. nextInt(10);
            codValidacao += "" + codigo[i];
        }
        return codValidacao;
    }
}
