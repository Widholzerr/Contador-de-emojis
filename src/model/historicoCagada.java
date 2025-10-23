package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;


public class historicoCagada {
    private static final LinkedHashMap<String, registroCagada> registros = new LinkedHashMap<>();

    public static void add(String data, int p, int g, String oP, String oG){
        registros.put(data, new registroCagada(data, p, g, oP, oG));
    }

    public static List<registroCagada> listar() {
        return new ArrayList<>(registros.values());
    }

    public static int getTotalPedro(){
        return registros.values().stream().mapToInt(registroCagada::getPedro).sum();
    }

    public int getTotalGabriela(){
        return registros.values().stream().mapToInt(registroCagada::getGabriela).sum();
    }

    public double getMediaPedro(){
        return registros.isEmpty() ? 0 : (double) getTotalPedro() / registros.size();
    }

    public double getMediaGabi(){
        return registros.isEmpty() ? 0 : (double) getTotalGabriela() / registros.size();
    }

    public boolean estaVazio() {
        return registros.isEmpty();
    }

    public void limpar(){
        registros.clear();
    }


}
