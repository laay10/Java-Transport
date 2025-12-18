package transporte.servicos;

import transporte.modelos.Transporte;
import transporte.modelos.enums.PorteCaminhao;

import java.util.List;
import java.util.Map;

public class RelatorioEstatistico {
    public static void gerarRelatorio() {
        List<Transporte> historico = GerenciadorTransportes.getHistorico();

        if (historico.isEmpty()) {
            System.out.println("\nNenhum transporte cadastrado ainda.");
            return;
        }

        double custoTotal = 0;
        int totalItens = 0;
        int totalVeiculos = 0;
        int totalKm = 0;

        System.out.println("\n=== Relatório Estatístico ===");
        for (Transporte t : historico) {
            custoTotal += t.getCustoTotal();
            totalItens += t.getItensTotal();
            totalKm += t.getDistancia();

            int veiculos = t.getFrota().values().stream().mapToInt(Integer::intValue).sum();
            totalVeiculos += veiculos;

            System.out.println("Trecho: " + t.getOrigem() + " → " + t.getDestino() + " (" + t.getDistancia() + " km)");
            System.out.printf("Peso total: %.2f kg | Custo: R$ %.2f%n", t.getPesoTotal(), t.getCustoTotal());
            System.out.print("Frota: ");
            t.getFrota().forEach((porte, qtd) -> System.out.print(qtd + " x " + porte.name() + " "));
            System.out.println("\n----------------------------------");
        }

        double custoMedioKm = (totalKm > 0) ? custoTotal / totalKm : 0;

        System.out.println("\n--- Totais ---");
        System.out.printf("Custo total: R$ %.2f%n", custoTotal);
        System.out.printf("Custo médio por km: R$ %.2f%n", custoMedioKm);
        System.out.printf("Total de veículos deslocados: %d%n", totalVeiculos);
        System.out.printf("Total de itens transportados: %d%n", totalItens);
    }
}
