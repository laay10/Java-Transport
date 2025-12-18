package transporte.menu;

import transporte.dados.LeitorCSV;
import transporte.servicos.ConsultaTrechos;
import transporte.servicos.GerenciadorTransportes;
import transporte.servicos.RelatorioEstatistico;

import java.util.Scanner;


public class Menu {
    public static void exibirMenu() {
        LeitorCSV leitor = new LeitorCSV("src/transporte/dados/DistanciasCidadesCSV.csv");
        GerenciadorTransportes gerenciador = new GerenciadorTransportes(leitor);
        Scanner sc = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n=== Sistema de Transporte ===");
            System.out.println("1 - Consultar trechos e modalidades");
            System.out.println("2 - Cadastrar transporte");
            System.out.println("3 - Relatório estatístico");
            System.out.println("4 - Finalizar programa");
            System.out.print("Escolha uma opção: ");

            while (!sc.hasNextInt()) {
                System.out.println("Opção inválida! Digite um número.");
                sc.next();
            }
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    ConsultaTrechos.executar(leitor);
                    break;
                case 2:
                    gerenciador.cadastrarTransporte();
                    break;
                case 3:
                    RelatorioEstatistico.gerarRelatorio();
                    break;
                case 4:
                    System.out.println("Encerrando...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 4);
        sc.close();
    }
}
