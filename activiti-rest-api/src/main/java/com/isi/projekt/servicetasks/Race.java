package com.isi.projekt.servicetasks;

public class Race {
    private String email;
    private String typ_biegu;
    private String data_biegu;
    private int cena_pakietu;

    public Race() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTyp_biegu() {
        return typ_biegu;
    }

    public void setTyp_biegu(String typ_biegu) {
        this.typ_biegu = typ_biegu;
    }

    public String getData_biegu() {
        return data_biegu;
    }

    public void setData_biegu(String data_biegu) {
        this.data_biegu = data_biegu;
    }

    public int getCena_pakietu() {
        return cena_pakietu;
    }

    public void setCena_pakietu(int cena_pakietu) {
        this.cena_pakietu = cena_pakietu;
    }

    @Override
    public String toString() {
        return "Race{" +
                "email='" + email + '\'' +
                ", typ_biegu='" + typ_biegu + '\'' +
                ", data_biegu='" + data_biegu + '\'' +
                ", cena_pakietu=" + cena_pakietu +
                '}';
    }
}
