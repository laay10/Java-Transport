package transporte.servicos;

import transporte.dados.LeitorCSV;
import transporte.modelos.enums.PorteCaminhao;

import java.util.Scanner;

public class ConsultaTrechos {

    public static void executar(LeitorCSV leitor) {
        Scanner sc = new Scanner(System.in);

        System.out.println("\nCidades disponíveis: \n " + leitor.exibirCidades());

        System.out.print("Digite a cidade de origem: ");
        String origem = sc.nextLine().trim();

        System.out.print("Digite a cidade de destino: ");
        String destino = sc.nextLine().trim();

        Integer distancia = leitor.getDistancia(origem, destino);
        if (distancia != null && distancia != -1) {
            System.out.println("\nEscolha o porte do caminhão:");
            for (PorteCaminhao p : PorteCaminhao.values()) {
                System.out.printf("%s - capacidade %d kg - custo R$ %.2f/km%n",
                        p.name(), p.getCapacidadeKg(), p.getCustoKm());
            }

            System.out.print("Digite o porte desejado: ");
            String porteEscolhido = sc.nextLine().trim().toUpperCase();

            try {
                PorteCaminhao caminhao = PorteCaminhao.valueOf(porteEscolhido);
                double custo = distancia * caminhao.getCustoKm();

                System.out.printf("\nDe %s até %s são %d km.%n", origem, destino, distancia);
                System.out.printf("Usando caminhão %s, o custo será de R$ %.2f%n",
                        caminhao.name(), custo);
            } catch (IllegalArgumentException e) {
                System.out.println("Porte de caminhão inválido!");
            }
        } else {
            System.out.println("Cidade inválida ou distância não encontrada!");
        }
    }
}
