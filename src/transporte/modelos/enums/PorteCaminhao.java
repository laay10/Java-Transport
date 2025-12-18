package transporte.modelos.enums;

public enum PorteCaminhao {
    PEQUENO(1000, 4.87),
    MEDIO(4000, 11.92),
    GRANDE(10000, 27.44);

    private final int capacidadeKg;
    private final double custoKm;

    PorteCaminhao(int capacidadeKg, double custoKm) {
        this.capacidadeKg = capacidadeKg;
        this.custoKm = custoKm;
    }

    public int getCapacidadeKg() {
        return capacidadeKg;
    }

    public double getCustoKm() {
        return custoKm;
    }
}
