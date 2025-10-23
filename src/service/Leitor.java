package service;

import model.historicoCagada;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Lê o arquivo de descrição exportado (ou qualquer .txt com o mesmo formato)
 * Formato esperado:
 * 22/09
 * Pedro: 💩💩
 * Gabi: 💩
 *
 * Aceita "Gabi" ou "Gabriela".
 */
public class Leitor {

    private static final String EMOJI = "\uD83D\uDCA9"; // 💩

    public static historicoCagada lerArquivo(File arquivo) throws IOException {
        historicoCagada historico = new historicoCagada();

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            String dataAtual = null;
            Integer pedroCount = null;
            Integer gabiCount = null;
            String obsPedro = "";
            String obsGabi = "";

            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) {
                    continue;
                }

                if (linha.matches("^\\d{1,2}/\\d{1,2}.*")) {
                    if (dataAtual != null && pedroCount != null && gabiCount != null) {
                        historicoCagada.add(dataAtual, pedroCount, gabiCount, obsPedro, obsGabi);
                    }
                    String[] parts = linha.split("\\s+");
                    dataAtual = parts[0];
                    pedroCount = 0; gabiCount = 0;
                    obsPedro = ""; obsGabi = "";
                } else if (linha.toLowerCase().startsWith("pedro")) {
                    pedroCount = contarEmojis(linha);
                    obsPedro = getObs(linha);
                } else if (linha.toLowerCase().startsWith("gabi") || linha.toLowerCase().startsWith("gabriela")) {
                    gabiCount = contarEmojis(linha);
                    obsGabi = getObs(linha);
                    if (dataAtual != null) {
                        if (pedroCount == null) pedroCount = 0;
                        historicoCagada.add(dataAtual, pedroCount, gabiCount, obsPedro, obsGabi);
                        pedroCount = null;
                        gabiCount = null;
                        dataAtual = null;
                    }
                } else {
                    if (linha.toLowerCase().contains("pedro:")) {
                        pedroCount = contarEmojis(linha);
                        obsPedro = getObs(linha);
                    } else if (linha.toLowerCase().contains("gabi:") || linha.toLowerCase().contains("gabriela:")) {
                        gabiCount = contarEmojis(linha);
                        obsGabi = getObs(linha);
                        if (dataAtual != null) {
                            if (pedroCount == null) pedroCount = 0;
                            historicoCagada.add(dataAtual, pedroCount, gabiCount, obsPedro, obsGabi);
                            pedroCount = null;
                            gabiCount = null;
                            dataAtual = null;
                        }
                    }
                }
            }
            if (dataAtual != null && (pedroCount != null || gabiCount != null)) {
                if (pedroCount == null) pedroCount = 0;
                if (gabiCount == null) gabiCount = 0;
                historicoCagada.add(dataAtual, pedroCount, gabiCount, obsPedro, obsGabi);
            }
        }

        return historico;
    }

    private static int contarEmojis(String linha) {
        int count = 0;
        int index = linha.indexOf(EMOJI);
        while (index != -1) {
            count++;
            index = linha.indexOf(EMOJI, index + EMOJI.length());
        }
        return count;
    }

    private static String getObs(String linha) {
        int idx = linha.indexOf(EMOJI);
        if (idx == -1) return "";
        String resto = linha.substring(idx + EMOJI.length()).trim();
        if (resto.startsWith("(") || resto.startsWith("-")) {
            return resto;
        }
        return "";
    }
}