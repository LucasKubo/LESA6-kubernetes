package MeuRemedio.app.mocks;

import MeuRemedio.app.models.usuarios.Usuario;

public class UsuarioMock {
    public static Usuario usuarioMock(){
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("teste");
        usuario.setEmail("teste");
        usuario.setSobrenome("teste");
        return usuario;
    }
}
