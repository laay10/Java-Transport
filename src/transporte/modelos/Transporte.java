// src/transporte/modelos/Transporte.java
package transporte.modelos;

import transporte.modelos.enums.PorteCaminhao;

import java.util.Map;

public class Transporte {
    private String origem;
    private String destino;
    private int distancia;
    private Map<PorteCaminhao, Integer> frota;
    private double custoTotal;
    private double pesoTotal;
    private int itensTotal;

    public Transporte(String origem, String destino, int distancia,
                      Map<PorteCaminhao, Integer> frota,
                      double custoTotal, double pesoTotal, int itensTotal) {
        this.origem = origem;
        this.destino = destino;
        this.distancia = distancia;
        this.frota = frota;
        this.custoTotal = custoTotal;
        this.pesoTotal = pesoTotal;
        this.itensTotal = itensTotal;
    }

    public String getOrigem() { return origem; }
    public String getDestino() { return destino; }
    public int getDistancia() { return distancia; }
    public Map<PorteCaminhao, Integer> getFrota() { return frota; }
    public double getCustoTotal() { return custoTotal; }
    public double getPesoTotal() { return pesoTotal; }
    public int getItensTotal() { return itensTotal; }
}


