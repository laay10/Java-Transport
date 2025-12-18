package transporte.servicos;

import transporte.dados.BancoProdutos;
import transporte.dados.LeitorCSV;
import transporte.modelos.Parada; // Importação da nova classe
import transporte.modelos.Produto;
import transporte.modelos.Transporte;
import transporte.modelos.enums.PorteCaminhao;

import java.util.*;

public class GerenciadorTransportes {
    private static List<Transporte> historico = new ArrayList<>();
    private final LeitorCSV leitor;
    private final Scanner sc = new Scanner(System.in);

    public GerenciadorTransportes(LeitorCSV leitor) {
        this.leitor = leitor;
    }

    public static List<Transporte> getHistorico() {
        return historico;
    }

    public void cadastrarTransporte() {
        System.out.print(leitor.exibirCidades());
        System.out.print("\nDigite a cidade de origem: ");
        String origem = sc.nextLine().trim();

        System.out.print("Digite o destino final: ");
        String destino = sc.nextLine().trim();

        List<Produto> produtosIniciais = coletarProdutos("origem");

        List<Parada> paradas = new ArrayList<>();
        paradas.add(new Parada(destino, produtosIniciais));

        System.out.print("Deseja adicionar paradas intermediárias? (s/n): ");
        String adicionarParada = sc.nextLine();

        while (adicionarParada.equalsIgnoreCase("s")) {
            System.out.print("Digite a próxima parada: ");
            String proximaParada = sc.nextLine().trim();
            List<Produto> produtosParada = coletarProdutos("parada " + proximaParada);
            paradas.add(new Parada(proximaParada, produtosParada));

            System.out.print("Deseja adicionar outra parada? (s/n): ");
            adicionarParada = sc.nextLine();
        }

        calcularERegistrarRota(origem, paradas);
    }

    private void calcularERegistrarRota(String origem, List<Parada> paradas) {
        String ultimaParada = origem;
        double pesoTotal = 0;
        int distanciaTotal = 0;
        Map<PorteCaminhao, Integer> frotaAtual = new LinkedHashMap<>();

        for (Parada parada : paradas) {
            Integer distanciaTrecho = leitor.getDistancia(ultimaParada, parada.getCidade());

            if (distanciaTrecho == null || distanciaTrecho == -1) {
                System.err.println("Erro: Não foi possível calcular a distância do trecho " + ultimaParada + " -> " + parada.getCidade());
                return;
            }

            distanciaTotal += distanciaTrecho;

            for (Produto produto : parada.getProdutosAdicionados()) {
                pesoTotal += produto.getPesoKg();
            }

            if (!ultimaParada.equals(origem)) {
                System.out.printf("\nChegando na parada em %s. Peso da carga atual: %.2f kg.%n", parada.getCidade(), pesoTotal);
                System.out.print("Deseja trocar o caminhão ou adicionar mais caminhões? (s/n): ");
                String opcaoTroca = sc.nextLine();
                if (opcaoTroca.equalsIgnoreCase("s")) {
                    frotaAtual = calcularFrota(pesoTotal);
                    System.out.println("Frota atualizada para o trecho " + ultimaParada + " -> " + parada.getCidade() + ":");
                    frotaAtual.forEach((porte, qtd) -> System.out.print(qtd + " x " + porte.name() + " | "));
                    System.out.println();
                }
            } else {
                frotaAtual = calcularFrota(pesoTotal);
                System.out.println("Frota inicial definida para o trecho " + origem + " -> " + parada.getCidade() + ":");
                frotaAtual.forEach((porte, qtd) -> System.out.print(qtd + " x " + porte.name() + " | "));
                System.out.println();
            }

            ultimaParada = parada.getCidade();
        }

        double custoTotal = calcularCustoTotal(frotaAtual, distanciaTotal);
        int itensTotal = paradas.stream().mapToInt(p -> p.getProdutosAdicionados().size()).sum();

        Transporte novoTransporte = new Transporte(origem, paradas.get(paradas.size() - 1).getCidade(), distanciaTotal, frotaAtual, custoTotal, pesoTotal, itensTotal);
        historico.add(novoTransporte);

        System.out.println("\n--- Transporte Cadastrado com Sucesso! ---");
        System.out.printf("Trecho: %s -> %s (%d km)%n", origem, paradas.get(paradas.size() - 1).getCidade(), distanciaTotal);
        System.out.printf("Peso Total: %.2f kg%n", pesoTotal);
        System.out.printf("Custo Total: R$ %.2f%n", custoTotal);
        System.out.print("Frota: ");
        frotaAtual.forEach((porte, qtd) -> System.out.print(qtd + " x " + porte.name() + " | "));
        System.out.println("\n-------------------------------------------");
    }

    private List<Produto> coletarProdutos(String localizacao) {
        List<Produto> produtosSelecionados = new ArrayList<>();
        String continuar = "s";
        System.out.printf("\nColetando produtos para %s...%n", localizacao);

        while (continuar.equalsIgnoreCase("s")) {
            BancoProdutos.listarProdutos();
            System.out.print("Digite o nome do produto a ser adicionado: ");
            String nomeProduto = sc.nextLine();

            Produto produto = BancoProdutos.getProduto(nomeProduto);
            if (produto != null) {
                int quantidade = 0;
                while (quantidade <= 0) {
                    System.out.print("Digite a quantidade de itens: ");
                    try {
                        quantidade = Integer.parseInt(sc.nextLine());
                        if (quantidade <= 0) {
                            System.err.println("Quantidade deve ser maior que zero.");
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Entrada inválida. Digite um número inteiro para a quantidade.");
                    }
                }
                for (int i = 0; i < quantidade; i++) {
                    produtosSelecionados.add(new Produto(produto.getNome(), produto.getPesoKg()));
                }
                System.out.println(quantidade + " itens de '" + produto.getNome() + "' adicionados.");
            } else {
                System.err.println("Produto não encontrado. Tente novamente.");
            }

            System.out.print("Deseja adicionar outro produto? (s/n): ");
            continuar = sc.nextLine();
        }
        return produtosSelecionados;
    }

    private Map<PorteCaminhao, Integer> calcularFrota(double pesoTotal) {
        Map<PorteCaminhao, Integer> frota = new LinkedHashMap<>();
        double restante = pesoTotal;
        PorteCaminhao[] portes = PorteCaminhao.values();
        Arrays.sort(portes, (a, b) -> Integer.compare(b.getCapacidadeKg(), a.getCapacidadeKg()));

        for (PorteCaminhao p : portes) {
            if (restante >= p.getCapacidadeKg()) {
                int qtd = (int) (restante / p.getCapacidadeKg());
                frota.put(p, qtd);
                restante %= p.getCapacidadeKg();
            }
        }
        if (restante > 0) {
            PorteCaminhao pequeno = PorteCaminhao.PEQUENO;
            frota.put(pequeno, frota.getOrDefault(pequeno, 0) + 1);
        }
        return frota;
    }

    private static double calcularCustoTotal(Map<PorteCaminhao, Integer> frota, int distancia) {
        double total = 0;
        for (var entry : frota.entrySet()) {
            PorteCaminhao caminhao = entry.getKey();
            int qtd = entry.getValue();
            total += qtd * caminhao.getCustoKm() * distancia;
        }
        return total;
    }
}
