package MeuRemedio.app;

import MeuRemedio.app.controllers.SobreController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SobreNosControllerTest {

    @InjectMocks
    SobreController sobreController;

    @DisplayName("Deve retornar a tela sobre nós")
    @Test
    public void telaSobreNos(){
        String sobre = sobreController.sobreNos();
        Assertions.assertEquals(sobre,"SobreNos");
    }

}
