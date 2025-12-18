// src/transporte/modelos/Produto.java
package transporte.modelos;

public class Produto {
    private String nome;
    private double pesoKg;

    public Produto(String nome, double pesoKg) {
        this.nome = nome;
        this.pesoKg = pesoKg;
    }

    public String getNome() {
        return nome;
    }

    public double getPesoKg() {
        return pesoKg;
    }
}
