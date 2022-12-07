package MeuRemedio.app.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;


@ExtendWith(MockitoExtension.class)
public class ContatoControllerTest {


    @InjectMocks
    ContatoController contatoController;

    @DisplayName("Deve retornar a tela de contatos")
    @Test
    public void telaContato(){

        String contato = contatoController.mostrarContatos();
        Assertions.assertEquals(contato, "Contatos");
    }
}
