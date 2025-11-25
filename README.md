Um organizador de arquivos automático, robusto e multiplataforma desenvolvido em Java.
O objetivo deste projeto é acabar com a bagunça da pasta **Downloads**, movendo arquivos automaticamente para pastas categorizadas (Imagens, Áudio, Documentos, etc.)


## Funcionalidades


* **Categorização Automática:** Reconhece mais de 50 tipos de arquivos (PDF, JPG, MP3, ISO, DEB, EXE, etc.).
* **Organização de Áudio:** Separa projetos de DAW (`.flp`, `.als`) de arquivos de música (`.mp3`) e alta fidelidade (`.wav`).
* **Cross-Platform:** Funciona perfeitamente em **Windows**, **Linux** e **macOS**.
* **Proteção de Arquivos:** Se um arquivo já existir na pasta de destino, ele é renomeado automaticamente (ex: `boleto.pdf` vira `boleto_1.pdf`) em vez de ser substituído.
* **Atomic Moves:** Garante que o arquivo só seja deletado da origem se for copiado com sucesso para o destino.


## Arquivos Suportados


O sistema organiza automaticamente as seguintes categorias:


| Categoria | Extensões Suportadas |
| :--- | :--- |
| **Imagens** | jpg, png, gif, svg, webp, psd, ai, raw... |
| **Vídeos** | mp4, mkv, mov, avi, webm... |
| **Áudio** | mp3, wav, flac, ogg, m4a... |
| **Projetos DAW** | flp (FL Studio), als (Ableton), rpp (Reaper)... |
| **Documentos** | pdf, docx, txt, xlsx, pptx... |
| **Instaladores** | exe, msi, dmg, pkg, deb, rpm, apk... |
| **Compactados** | zip, rar, 7z, tar.gz... |
| **Código** | java, py, js, html, css, php... |


## Requisitos


* **Java Runtime Environment (JRE):** Versão 17 ou superior.
* Para verificar se você tem o Java instalado, digite no terminal:
  ```bash
  java -version
  ```
  
## Como Executar?
Primeiro, baixe a versão mais recente na aba Releases.

No Windows

(Interface): Se o Java estiver configurado corretamente, basta dar dois cliques no arquivo DownloadOrganizer.jar. Ele vai rodar em segundo plano e organizar a pasta.

(Terminal/PowerShell): Abra o terminal na pasta onde baixou o arquivo e digite:
 ```bash
  java -jar DownloadOrganizer.jar
 ```

No Linux (Ubuntu, Fedora, Debian)

Abra o terminal na pasta do arquivo e execute:

 ```bash
  java -jar DownloadOrganizer.jar
 ```
