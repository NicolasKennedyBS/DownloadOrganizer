package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Main {

    // Configuração Central: Mapeia Extensão -> Pasta de Destino
    private static final Map<String, Path> REGRAS_ORGANIZACAO = new HashMap<>();

    static {
        // Imagens (Adicionado webp, svg, psd para design)
        cadastrarRegra("Imagens", "jpg", "jpeg", "png", "gif", "bmp", "svg", "webp", "tiff", "ico", "raw", "psd", "ai");

        // Vídeos (Filmes, clipes, gravações de tela)
        cadastrarRegra("Videos", "mp4", "mkv", "avi", "mov", "wmv", "flv", "webm", "m4v", "mpeg", "3gp");

        // Áudio (Sua área: Adicionado flac, ogg, m4a)
        cadastrarRegra(Paths.get("Audio", "Musica").toString(), "mp3", "aac", "wma", "m4a");
        cadastrarRegra(Paths.get("Audio", "Qualidade_Alta").toString(), "wav", "flac", "aiff", "alac");
        cadastrarRegra(Paths.get("Audio", "Projetos_DAW").toString(), "flp", "als", "rpp", "cpr", "ptx", "logic"); // FL, Ableton, Reaper, Cubase, ProTools

        // Documentos de Texto e Office
        cadastrarRegra("Documentos", "pdf", "doc", "docx", "txt", "rtf", "odt", "tex", "wpd");

        // Planilhas e Dados
        cadastrarRegra("Planilhas", "xls", "xlsx", "csv", "ods", "xml", "json");

        // Apresentações
        cadastrarRegra("Apresentacoes", "ppt", "pptx", "odp", "key");

        // Compactados (Adicionado rar, 7z, tar.gz)
        cadastrarRegra("Compactados", "zip", "rar", "7z", "tar", "gz", "bz2", "xz");

        // Executáveis e Instaladores (Adicionado apk para Android)
        cadastrarRegra("Instaladores", "exe", "msi", "bat", "sh", "cmd", "apk", "jar", "bin", "dmg", "pkg");

        // Imagens de Disco
        cadastrarRegra("ISOs", "iso", "img", "vhd", "vmdk");

        // Fontes (Útil para design)
        cadastrarRegra("Fontes", "ttf", "otf", "woff", "woff2", "eot");

        // Scripts e Código Fonte (Já que você curte PHP, JS, HTML)
        cadastrarRegra("Codigo_Fonte", "java", "py", "js", "html", "css", "php", "c", "cpp", "h", "cs", "sql", "ts");

        // Pacotes de instalação Linux (Fedora usa .rpm, Debian/Ubuntu usa .deb)
        cadastrarRegra("Linux_Packages", "deb", "rpm", "AppImage", "flatpakref", "snap");

        // Scripts Shell (já tinha sh, mas adicionei mais variantes)
        cadastrarRegra("Scripts_Shell", "sh", "bash", "zsh", "fish");

        // Arquivos compactados comuns no Linux (tarballs)
        cadastrarRegra("Compactados", "tar", "gz", "xz", "bz2", "tgz");
    }

    public static void main(String[] args) {
        // Pega automaticamente a pasta Downloads do usuário atual
        String userHome = System.getProperty("user.home");
        Path downloadDir = Paths.get(userHome, "Downloads");

        System.out.println("=== Faxineiro Ultimate Iniciado ===");
        System.out.println("Pasta Alvo: " + downloadDir + "\n");

        if (!Files.exists(downloadDir)) {
            System.err.println(" Erro: A pasta de downloads não foi encontrada!");
            return;
        }

        // Contador para estatísticas finais
        long arquivosMovidos = 0;

        try (Stream<Path> arquivos = Files.list(downloadDir)) {
            // O .count() no final consome a stream, então vamos usar um truque com map para contar
            arquivosMovidos = arquivos.filter(Files::isRegularFile)
                    .map(arquivo -> processarArquivo(arquivo, downloadDir)) // Processa e retorna true/false
                    .filter(status -> status) // Mantém só os que deram certo
                    .count(); // Conta

        } catch (IOException e) {
            System.err.println(" Erro fatal ao ler pasta: " + e.getMessage());
        }

        System.out.println("\n=== Faxina Concluída. Arquivos movidos: " + arquivosMovidos + " ===");
    }

    // Retorna true se moveu, false se ignorou ou deu erro
    private static boolean processarArquivo(Path arquivoOriginal, Path pastaBase) {
        String nomeArquivo = arquivoOriginal.getFileName().toString();

        // Ignora arquivos temporários comuns ou arquivos de sistema ocultos (começados com ponto)
        if (nomeArquivo.startsWith(".") || nomeArquivo.endsWith(".tmp") || nomeArquivo.endsWith(".crdownload")) {
            return false;
        }

        String extensao = pegarExtensao(nomeArquivo);

        if (REGRAS_ORGANIZACAO.containsKey(extensao)) {
            Path pastaDestinoRelativa = REGRAS_ORGANIZACAO.get(extensao);
            Path pastaDestinoFinal = pastaBase.resolve(pastaDestinoRelativa);

            try {
                if (!Files.exists(pastaDestinoFinal)) {
                    Files.createDirectories(pastaDestinoFinal);
                }

                Path caminhoFinal = resolverConflitoNome(pastaDestinoFinal, nomeArquivo);
                Files.move(arquivoOriginal, caminhoFinal, StandardCopyOption.ATOMIC_MOVE);

                System.out.println(" [ " + extensao.toUpperCase() + " ] " + nomeArquivo + " -> " + pastaDestinoRelativa);
                return true;

            } catch (IOException e) {
                System.err.println(" Erro ao mover " + nomeArquivo + ": " + e.getMessage());
                return false;
            }
        }
        return false;
    }

    private static void cadastrarRegra(String pasta, String... extensoes) {
        for (String ext : extensoes) {
            REGRAS_ORGANIZACAO.put(ext.toLowerCase(), Paths.get(pasta));
        }
    }

    private static String pegarExtensao(String nomeArquivo) {
        int i = nomeArquivo.lastIndexOf('.');
        if (i > 0) {
            return nomeArquivo.substring(i + 1).toLowerCase();
        }
        return "";
    }

    private static Path resolverConflitoNome(Path pastaDestino, String nomeOriginal) {
        Path caminhoCompleto = pastaDestino.resolve(nomeOriginal);

        if (!Files.exists(caminhoCompleto)) return caminhoCompleto;

        String nomeSemExt = nomeOriginal.substring(0, nomeOriginal.lastIndexOf('.'));
        String ext = pegarExtensao(nomeOriginal);
        int contador = 1;

        while (Files.exists(caminhoCompleto)) {
            String novoNome = nomeSemExt + "_" + contador + "." + ext;
            caminhoCompleto = pastaDestino.resolve(novoNome);
            contador++;
        }

        return caminhoCompleto;
    }
}