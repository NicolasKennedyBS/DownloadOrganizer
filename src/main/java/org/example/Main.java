package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        // Pega a pasta de Downloads
        String userHome = System.getProperty("user.home");
        Path downloadDir = Paths.get(userHome, "Downloads");

        System.out.println("=== Faxineiro de Downloads Iniciado ===");
        System.out.println("Varrendo pasta: " + downloadDir + "\n");

        // Tenta listar e mover os ficheiros
        try (Stream<Path> ficheiros = Files.list(downloadDir)) {

            ficheiros.filter(Files::isRegularFile) // Ignora pastas, pega apenas ficheiros
                    .forEach(ficheiro -> moverFicheiro(ficheiro, downloadDir));

        } catch (IOException e) {
            System.err.println("Erro ao ler pasta: " + e.getMessage());
        }
    }

    // Regras
    private static void moverFicheiro(Path ficheiro, Path pastaBase) {
        String nomeFicheiro = ficheiro.getFileName().toString().toLowerCase();
        Path subPasta = null;

        // Imagens
        if (nomeFicheiro.endsWith(".jpg") || nomeFicheiro.endsWith(".png") || nomeFicheiro.endsWith(".jpeg")) {
            subPasta = Paths.get("Imagens_Organizadas");

            // Documentos
        } else if (nomeFicheiro.endsWith(".pdf") || nomeFicheiro.endsWith(".docx") || nomeFicheiro.endsWith(".txt")) {
            subPasta = Paths.get("Documentos_Organizados");

            // Instaladores e Compactados
        } else if (nomeFicheiro.endsWith(".exe") || nomeFicheiro.endsWith(".msi") || nomeFicheiro.endsWith(".zip")) {
            subPasta = Paths.get("Instaladores_Organizados");


            // ISOs (Numa pasta dedicada "iso")
        } else if (nomeFicheiro.endsWith(".iso")) {
            subPasta = Paths.get("iso");

            // Áudio MP3 (Dentro de sons -> mp3)
        } else if (nomeFicheiro.endsWith(".mp3")) {
            subPasta = Paths.get("sons", "mp3");

            // Áudio WAV (Dentro de sons -> wavs)
        } else if (nomeFicheiro.endsWith(".wav")) {
            subPasta = Paths.get("sons", "wavs");
        }

        // Se encontrou uma categoria, executa o movimento
        if (subPasta != null) {
            try {
                // Resolve o caminho completo (ex: Downloads/sons/mp3)
                Path destinoFinal = pastaBase.resolve(subPasta);

                // Cria todas as pastas necessárias (incluindo subpastas) se não existirem
                if (!Files.exists(destinoFinal)) {
                    Files.createDirectories(destinoFinal);
                }

                // Move o ficheiro
                Path caminhoFinal = destinoFinal.resolve(ficheiro.getFileName());
                Files.move(ficheiro, caminhoFinal, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("Movido: " + nomeFicheiro + " -> " + subPasta);

            } catch (IOException e) {
                System.err.println("Falha ao mover " + nomeFicheiro + ": " + e.getMessage());
            }
        }
    }
}