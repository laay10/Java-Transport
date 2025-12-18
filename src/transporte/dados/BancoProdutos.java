// src/transporte/dados/BancoProdutos.java
package transporte.dados;

import transporte.modelos.Produto;

import java.util.HashMap;
import java.util.Map;

public class BancoProdutos {
    private static final Map<String, Produto> produtos = new HashMap<>();

    static {
        produtos.put("celular", new Produto("Celular", 0.2));
        produtos.put("geladeira", new Produto("Geladeira", 60));
        produtos.put("playstation", new Produto("PlayStation 5", 4.5));
        produtos.put("luminaria", new Produto("Luminária", 1.0));
        produtos.put("freezer", new Produto("Freezer", 80));
        produtos.put("nintendo", new Produto("Nintendo Switch", 1.5));
        produtos.put("airfryer", new Produto("Air Fryer", 5.0));
        produtos.put("cadeira", new Produto("Cadeira", 7.0));
    }

    public static Produto getProduto(String nome) {
        return produtos.get(nome.toLowerCase());
    }

    public static void listarProdutos() {
        System.out.println("\nProdutos disponíveis:");
        for (Produto p : produtos.values()) {
            System.out.printf("- %s (%.2f kg/unidade)%n", p.getNome(), p.getPesoKg());
        }
    }
}
