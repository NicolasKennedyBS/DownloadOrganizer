package org.example;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        // Define o caminho da pasta de Downloads (pegando a home do usuário automaticamente)
        String userHome = System.getProperty("user.home");
        Path downloadDir = Paths.get(userHome, "Downloads");

        System.out.println("=== Faxineiro de Downloads Iniciado ===");
        System.out.println("Varrendo pasta: " + downloadDir);

        // Aqui virá a lógica de mover arquivos depois

        System.out.println("Sistema pronto para operar.");
    }
}