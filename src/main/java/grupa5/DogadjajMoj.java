package grupa5;

import java.time.LocalDate;

public class DogadjajMoj {
    private String naziv;
    private LocalDate datum;
    private String imagePath;

    public DogadjajMoj(String naziv, LocalDate datum, String imagePath) {
        this.naziv = naziv;
        this.datum = datum;
        this.imagePath = imagePath;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }
}
