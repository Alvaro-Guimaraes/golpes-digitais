


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    private static final List<String> PALAVRAS_SUSPEITAS = List.of(
            "senha",
            "codigo de segurança",
            "código de segurança",
            "pix",
            "premio",
            "prêmio",
            "ganhou",
            "ganhador",
            "urgente",
            "clique aqui",
            "conta bloqueada",
            "conta será bloqueada",
            "confirme seus dados",
            "atualize seus dados",
            "dados bancarios",
            "dados bancários",
            "cpf",
            "cartao",
            "cartão",
            "transferencia",
            "transferência",
            "deposito",
            "depósito"
    );

    private static final List<String> EXPRESSOES_GOLPE = List.of(
            "mande o codigo",
            "mande o código",
            "informe sua senha",
            "clique no link",
            "acesse o link",
            "faça um pix",
            "envie o pix",
            "transfira agora",
            "confirme sua conta",
            "sua conta foi bloqueada"
    );

    private static final Pattern PADRAO_URL =
            Pattern.compile("https?://\\S+|www\\.\\S+");

    public static Resultado analisar(String mensagem) {

        if (mensagem == null || mensagem.isBlank()) {
            return new Resultado(
                    "MENSAGEM VAZIA",
                    0,
                    List.of("Nenhuma mensagem foi informada.")
            );
        }

        String texto = mensagem.toLowerCase();

        int pontuacao = 0;
        List<String> motivos = new ArrayList<>();

        // ==========================================
        // 1. PALAVRAS SUSPEITAS
        // ==========================================

        for (String palavra : PALAVRAS_SUSPEITAS) {

            if (texto.contains(palavra)) {

                pontuacao += 5;

                motivos.add(
                        "Termo suspeito encontrado: \"" + palavra + "\""
                );
            }
        }

        // ==========================================
        // 2. EXPRESSÕES DE GOLPE
        // ==========================================

        for (String expressao : EXPRESSOES_GOLPE) {

            if (texto.contains(expressao)) {

                pontuacao += 15;

                motivos.add(
                        "Expressão suspeita encontrada: \"" + expressao + "\""
                );
            }
        }

        // ==========================================
        // 3. LINK
        // ==========================================

        if (PADRAO_URL.matcher(texto).find()) {

            pontuacao += 15;

            motivos.add("A mensagem contém um link.");
        }

        // ==========================================
        // 4. EXCESSO DE EXCLAMAÇÕES
        // ==========================================

        long quantidadeExclamacoes = texto
                .chars()
                .filter(c -> c == '!')
                .count();

        if (quantidadeExclamacoes >= 3) {

            pontuacao += 5;

            motivos.add(
                    "Uso excessivo de exclamações."
            );
        }

        // ==========================================
        // 5. CLASSIFICAÇÃO
        // ==========================================

        String classificacao;

        if (pontuacao >= 40) {

            classificacao = "ALTO RISCO - POSSÍVEL GOLPE";

        } else if (pontuacao >= 20) {

            classificacao = "RISCO MÉDIO - MENSAGEM SUSPEITA";

        } else {

            classificacao = "BAIXO RISCO";
        }

        return new Resultado(
                classificacao,
                pontuacao,
                motivos
        );
    }

    // ==========================================
    // PROGRAMA PRINCIPAL
    // ==========================================

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {

            System.out.println("====================================");
            System.out.println("       DETECTOR DE GOLPES DIGITAIS");
            System.out.println("====================================");

            System.out.println("\nDigite a mensagem:");

            String mensagem = scanner.nextLine();

            Resultado resultado = analisar(mensagem);

            System.out.println("\n====================================");
            System.out.println("             RESULTADO");
            System.out.println("====================================");

            System.out.println(
                    "Classificação: " + resultado.classificacao()
            );

            System.out.println(
                    "Pontuação de risco: " + resultado.pontuacao()
            );

            if (!resultado.motivos().isEmpty()) {

                System.out.println("\nMotivos:");

                for (String motivo : resultado.motivos()) {
                    System.out.println("- " + motivo);
                }

            } else {

                System.out.println(
                        "\nNenhum sinal suspeito encontrado."
                );
            }
        }
    }

    // ==========================================
    // RECORD - JAVA 21
    // ==========================================

    public record Resultado(
            String classificacao,
            int pontuacao,
            List<String> motivos
    ) {
    }
}
