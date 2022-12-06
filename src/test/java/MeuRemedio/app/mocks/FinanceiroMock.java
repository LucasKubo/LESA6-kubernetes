package MeuRemedio.app.mocks;

import MeuRemedio.app.models.usuarios.Financeiro;

public class FinanceiroMock {

    public static Financeiro gastoMock() {
        Financeiro financeiro = new Financeiro();
        financeiro.setId(1l);
        financeiro.setData("2022-05-01");
        financeiro.setQtdParcela(12);
        financeiro.setValor(500.00);
        return financeiro;
    }
    public static Financeiro gastoMockAnoPassado() {
        Financeiro financeiro = new Financeiro();
        financeiro.setId(1l);
        financeiro.setData("2021-05-01");
        financeiro.setQtdParcela(12);
        financeiro.setValor(500.00);
        return financeiro;
    }
    public static Financeiro gastoMockAnoQueVem() {
        Financeiro financeiro = new Financeiro();
        financeiro.setId(1l);
        financeiro.setData("2023-05-01");
        financeiro.setQtdParcela(12);
        financeiro.setValor(500.00);
        return financeiro;
    }
}
