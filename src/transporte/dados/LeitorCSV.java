package transporte.dados;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class LeitorCSV {

    private Map<String, Map<String, Integer>> distancias = new HashMap<>();
    private Map<String, String> cidadesCaseSensitive = new HashMap<>();

    private String arquivo;

    public LeitorCSV(String arquivo) {
        this.arquivo = arquivo;
        carregarCSV();
    }

    private void carregarCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha = br.readLine();
            if (linha == null) return;

            String[] cabecalho = linha.split(";");
            List<String> cidades = new ArrayList<>();
            for (int i = 1; i < cabecalho.length; i++) {
                String nomeCidade = cabecalho[i].trim();
                cidades.add(nomeCidade.toLowerCase());
                cidadesCaseSensitive.put(nomeCidade.toLowerCase(), nomeCidade);
            }

            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");
                String cidadeOrigemOriginal = partes[0].trim();
                String cidadeOrigemLowerCase = cidadeOrigemOriginal.toLowerCase();
                cidadesCaseSensitive.put(cidadeOrigemLowerCase, cidadeOrigemOriginal);

                Map<String, Integer> mapaDestino = new HashMap<>();

                for (int i = 1; i < partes.length; i++) {
                    String valor = partes[i].trim();
                    String cidadeDestinoLowerCase = cidades.get(i - 1);

                    if (valor.isEmpty()) {
                        mapaDestino.put(cidadeDestinoLowerCase, -1);
                    } else {
                        try {
                            int distancia = Integer.parseInt(valor);
                            mapaDestino.put(cidadeDestinoLowerCase, distancia);
                        } catch (NumberFormatException e) {
                            mapaDestino.put(cidadeDestinoLowerCase, -1);
                        }
                    }
                }
                distancias.put(cidadeOrigemLowerCase, mapaDestino);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    public int getDistancia(String origem, String destino) {
        String origemLowerCase = origem.trim().toLowerCase();
        String destinoLowerCase = destino.trim().toLowerCase();

        if (distancias.containsKey(origemLowerCase) && distancias.get(origemLowerCase).containsKey(destinoLowerCase)) {
            return distancias.get(origemLowerCase).get(destinoLowerCase);
        }
        return -1;
    }

    public Set<String> getCidades() {
        return distancias.keySet();
    }

    public String exibirCidades() {
        StringBuilder sb = new StringBuilder();
        Set<String> cidadesLowerCase = getCidades();

        if (cidadesLowerCase.isEmpty()) {
            return "Nenhuma cidade encontrada.";
        }

        Iterator<String> iterator = cidadesLowerCase.iterator();
        while (iterator.hasNext()) {
            String cidadeLowerCase = iterator.next();
            sb.append(cidadesCaseSensitive.get(cidadeLowerCase));
            if (iterator.hasNext()) {
                sb.append(";\n ");
            }
        }
        return sb.toString();
    }
}
