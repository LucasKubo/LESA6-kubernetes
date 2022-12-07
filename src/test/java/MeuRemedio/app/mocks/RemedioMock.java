package MeuRemedio.app.mocks;

import MeuRemedio.app.models.remedios.Remedio;

public class RemedioMock {

    public static Remedio remedioMock(){
        Remedio remedio = new Remedio();
        remedio.setRM_ID(1L);
        remedio.setRM_Nome("teste");
        remedio.setRM_Dosagem("1");
        remedio.setRM_UnidadeDosagem("mg");

        return remedio;
    }
}
