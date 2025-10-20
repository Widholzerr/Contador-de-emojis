package service;

import model.historicoCagada;
import model.registroCagada;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Exporta o historico para CSV com cabeçalho:
 */
public class ExportadorCSV {

    public static void exportar(File arquivo, historicoCagada historico) throws IOException {
        try (FileWriter writer = new FileWriter(arquivo)) {
            writer.write("Data,Pedro,Gabi\n");
            for (registroCagada reg : historicoCagada.listar()) {
                writer.write(String.format("%s,%d,%d\n",
                        reg.getData(),
                        reg.getPedro(),
                        reg.getGabriela()));
            }
            writer.write(String.format("TOTAL,%d,%d\n",
                    historico.getTotalPedro(),
                    historico.getTotalGabriela()));
            writer.flush();
        }
    }
}