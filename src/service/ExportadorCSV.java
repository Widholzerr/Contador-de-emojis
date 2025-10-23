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
            writer.write("Data,Pedro,Gabi,ObsPedro,ObsGabi\n");

            // percorre os registros do histórico
            for (registroCagada reg : historico.listar()) {
                writer.write(String.format("%s,%d,%d,\"%s\",\"%s\"\n",
                        reg.getData(),
                        reg.getPedro(),
                        reg.getGabriela(),
                        reg.getObsPedro().replace("\"", "'"),
                        reg.getObsGabi().replace("\"", "'")));
            }

            // adiciona totais e médias ao final
            writer.write(String.format("TOTAL,%d,%d,,\n",
                    historico.getTotalPedro(),
                    historico.getTotalGabriela()));

            writer.write(String.format("MÉDIA,%.2f,%.2f,,\n",
                    historico.getMediaPedro(),
                    historico.getMediaGabi()));
        }
    }
}