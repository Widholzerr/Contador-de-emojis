package model;

public class registroCagada {
    private final String data;
    private final int pedro;
    private final int gabriela;

    public registroCagada(String data, int pedro, int gabriela) {
        this.data = data;
        this.pedro = pedro;
        this.gabriela = gabriela;
    }

    public String getData() {
        return data;
    }
    public int getPedro(){
        return pedro;
    }
    public int getGabriela(){
        return gabriela;
    }

    public String toString() {
        return String.format("%s - Pedro: %d 💩 | Gabi: %d 💩", data, pedro, gabriela);
    }

}
