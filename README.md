# 💩 Contador de Cagadas — Pedro & Gabi

Um projeto Swing **(Java)** que transforma simples mensagens do WhatsApp em um **relatório visual e estatístico de cagadas**.  
Porque o humor também merece um sistema de gestão. 😎

---

## 🧻 Visão Geral

O **Contador de Cagadas** lê um arquivo `.txt` utilizando a descrição prévia de um grupo ou mensagens, identifica as cagadas diárias de **Pedro** e **Gabi** (pelo emoji 💩) e gera:

- 📅 **Tabela detalhada** com datas, quantidades e observações personalizadas;  
- 📊 **Gráfico visual** comparando quem cagou mais por dia;  
- 📈 **Média e total mensal** de cagadas;  
- 💾 **Exportação automática para CSV**;  
- 🌓 **Modo claro e escuro** (com alternância no topo da janela).

---

## ⚙️ Funcionalidades

| Função | Descrição |
|--------|------------|
| 📂 Importar `.txt` | Lê o histórico exportado do WhatsApp e interpreta os emojis 💩. |
| 📋 Colar descrição | Permite colar o conteúdo direto da área de transferência. |
| ♻️ Limpar | Reseta a tabela e as estatísticas. |
| 💾 Exportar CSV | Gera um relatório completo com totais e médias. |
| 📊 Ver Gráfico | Mostra um comparativo visual diário entre Pedro e Gabi. |
| 🌙 / ☀️ Alternar Tema | Modo claro e escuro (com ajuste dinâmico de contraste). |

---

## 🧠 Estrutura do Projeto
📂 src/
│

├── 📁 model/

│ ├── 🧮 historicoCagada.java → Armazena e calcula médias/totais

│ ├── 💾 registroCagada.java → Representa uma entrada diária

│

├── 📁 service/

│ ├── 📖 Leitor.java → Lê e interpreta o .txt (conta os 💩 e observações)

│ ├── 📤 ExportadorCSV.java → Gera o arquivo CSV formatado

│

├── 📁 ui/

│ ├── 🪟 CagadaUi.java → Interface principal (Swing + modo escuro/claro)

│ ├── 📊 GraficoUi.java → Tela com gráfico de barras comparativo

│

└── 🚀 Main.java → Inicializa a aplicação

## 💡 Como Usar

1. **Exporte** a conversa do WhatsApp para um arquivo txt (Conforme exemplo de entrada).
2. **Abra o programa**.  
3. Clique em **📂 Importar descrição (.txt)** e selecione o arquivo.    

---

## 🖥️ Interface

### 🟤 Modo Claro
<img width="1725" height="949" alt="image" src="https://github.com/user-attachments/assets/7ece64c4-8fd6-42e4-9abc-8d50d6d6e55b" />


### ⚫ Modo Escuro
<img width="1726" height="948" alt="image" src="https://github.com/user-attachments/assets/f682a5e4-94c5-4b1b-95ae-bfa51eb84ac8" />

---

## 🧾 Exemplo de Entrada

22/09

Pedro: 💩💩

Gabi: 💩 (emergencial)


---

## 📊 Saída (Tabela)

| Data | Pedro (💩) | Obs.: Pedro | Gabi (💩) | Obs.: Gabi    |
|------|-------------|-------------|-----------|---------------|
| 22/09 |     2      |             |     1     | (emergencial) |
| 23/09 |     1      |   (leve)    |     2     |               |
| ..... | .......... | ........... | ......... | ............. |

---

## 💾 Exportação CSV

O relatório exportado contém:

Data,Pedro,Gabi,ObsPedro,ObsGabi

22/09,2,1,,"(emergencial)"

23/09,1,2,"(leve)",

...

TOTAL,47,35,,

MÉDIA,1.52,1.13,,

---

## Tecnologia utilizada

- ☕ **Java 24**
- 🖼️ **Swing**
- 📊 **Graphics2D** (para o gráfico)
- 🧾 **File I/O**
- 😎 **Unicode & Emojis**

## 🧑‍💻 Créditos

Sem os dados extremamente precisos de minha namorada Gabriela, esse projeto não teria saido do papel.
