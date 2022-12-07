package MeuRemedio;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MeuRemedioApplicationTest {

    @Test
    public void testeMain(){
        String[] args = new String[0];
        Assertions.assertDoesNotThrow(()-> MeuRemedioApplication.main(args));
    }
}