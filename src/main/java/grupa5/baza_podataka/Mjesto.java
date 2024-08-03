package grupa5.baza_podataka;
import jakarta.persistence.*;

@Entity
@Table(name = "Mjesta")
public class Mjesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mjestoID;

    @Column(nullable = false)
    private Integer postanskiBroj;

    @Column(nullable = false)
    private String naziv;

    // Getters and Setters
}
