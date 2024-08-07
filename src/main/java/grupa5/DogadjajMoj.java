package grupa5;

import java.time.LocalDate;

public class DogadjajMoj {
    private String naziv;
    private LocalDate datum;

    public DogadjajMoj(String naziv, LocalDate datum) {
        this.naziv = naziv;
        this.datum = datum;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }
}
