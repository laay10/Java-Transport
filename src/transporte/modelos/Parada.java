package transporte.modelos;

import java.util.List;

public class Parada {
    private String cidade;
    private List<Produto> produtosAdicionados;

    public Parada(String cidade, List<Produto> produtosAdicionados) {
        this.cidade = cidade;
        this.produtosAdicionados = produtosAdicionados;
    }

    public String getCidade() {
        return cidade;
    }

    public List<Produto> getProdutosAdicionados() {
        return produtosAdicionados;
    }
}

