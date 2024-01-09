package com.rapha.vendafavorita;

public class Constantes {

    //USUARIO

    public static final int CONTROLE_VERSAO_USUARIO = 1;

    public static final String COLECAO_USUARIO = "Usuario";


    public static final int USUARIO_TIPO_CLIENTE = 1;
    public static final int USUARIO_TIPO_VENDEDOR = 2;

    //FIREBASE MESSAGING
    public static final String TOKEN = "token";
    public static final String TOKEN_NOTIFICACAO = "tokenNotificacao";


    public static final int STATUS_SOLICIADA = 1;
    public static final int STATUS_CONFIRMADA = 2;
    public static final int STATUS_CANCELADA = 3;
    public static final int STATUS_EM_ANDAMENTO = 4;//INDO PRA O CLIENTE
    public static final int STATUS_CONCLUIDA = 5;


    public static String getMotivoCancelamento(int id) {
        switch(id) {
            case 1:
                return "Produto está em falta no estoque e indisponivel no fornecedor";
            case 2:
                return "Produto está em falta no estoque e não conseguimos pegar no fornecedor";
            case 3:
                return "Produto era falta no estoque e não chegou no tempo que o cliente desejava";
            case 4:
                return "O cliente desistiu por causa de um longo tempo de entrega";
            case 5:
                return "O cliente desistiu por exigir que a entrega fosse imediata";
            case 6:
                return "O cliente desistiu na hora da entrega por nao está de acordo com as caracteristicas do produto";
            case 7:
                return "Venda cadastrada com informações ou quantidade erradas";
            case 8:
                return "Cliente não respondeu, ou não atendeu o suporte";
            case 9:
                return "Cliente desistiu da compra antes do pedido sair da loja";
            default:
                return "Nenhum motivo declarado";
        }
    }


}
