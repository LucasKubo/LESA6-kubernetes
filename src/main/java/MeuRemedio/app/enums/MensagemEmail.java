package MeuRemedio.app.enums;

public enum MensagemEmail {
    /*Assunto dos E-MAILs enviados*/
    CADASTRO_VALIDACAO("Verifique seu registro no Meu Remédio"),
    CADASTRO_REALIZADO("Cadastro realizado"),
    RECUPERACAO_SENHA("Recuperação de senha"),

    REMEDIO_CADASTRADO("Remédio cadastrado"),

    NOTIFICACAO_REMEDIO("Hora de tomar sua medicação"),

    //Corpo dos E-MAILs a serem desparadas
    CADASTRO_MENSAGEM(". Queremos agradecer por ter se registrado na plataforma Meu Remédio. \n Acesse a plataforma em:"),
    RECUPERACAO_MENSAGEM("Olá, vimos que perdeu a senha, mas não se preocupe ! Para recuperar sua senha acesse o link: "),

    CADASTRO_REMEDIO("Olá, vimos que você cadastrou um novo remédio, ''"),

    NOTIFICACAO_MENSAGEM("Olá! Já está na hora de tomar os seus remédios: ");
    private String descricao;

    MensagemEmail(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao(){
        return descricao;
    }
    @Override
    public String toString(){
        return getDescricao();
    }
}