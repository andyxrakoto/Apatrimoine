package school.hei.patrimoine.cas.example;

import static java.time.Month.APRIL;
import static java.time.Month.DECEMBER;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Supplier;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;

public class PatrimoineBako8Avril2025 implements Supplier<Patrimoine> {

    public static final LocalDate DATE_DEBUT = LocalDate.of(2025, APRIL, 8);
    public static final LocalDate DATE_FIN = LocalDate.of(2025, DECEMBER, 31);

    private Compte compteCourantBNI() {
        return new Compte("Compte courant BNI", DATE_DEBUT, ariary(2_000_000));
    }

    private Compte compteEpargneBMOI() {
        return new Compte("Compte épargne BMOI", DATE_DEBUT, ariary(625_000));
    }

    private Compte coffreDomicile() {
        return new Compte("Coffre fort à la maison", DATE_DEBUT, ariary(1_750_000));
    }

    private Materiel laptop() {
        return new Materiel(
                "Ordinateur portable", DATE_DEBUT, DATE_DEBUT, ariary(3_000_000), -0.12);
    }

    private Set<Possession> listePossessions() {
        var courantBNI = compteCourantBNI();
        var epargneBMOI = compteEpargneBMOI();
        var coffre = coffreDomicile();
        var ordinateur = laptop();

        // Salaire mensuel le 2 du mois
        new FluxArgent(
                "Salaire mensuel",
                courantBNI,
                LocalDate.of(2025, 5, 2),
                DATE_FIN,
                2,
                ariary(2_125_000));

        // Épargne automatique de 200 000 Ar depuis BNI vers BMOI chaque 3 du mois
        new FluxArgent(
                "Épargne mensuelle",
                epargneBMOI,
                LocalDate.of(2025, 5, 3),
                DATE_FIN,
                3,
                ariary(200_000));
        new FluxArgent(
                "Épargne mensuelle - ponction BNI",
                courantBNI,
                LocalDate.of(2025, 5, 3),
                DATE_FIN,
                3,
                ariary(-200_000));

        // Dépenses mensuelles (vie courante) le 1er du mois
        new FluxArgent(
                "Dépenses vie courante",
                courantBNI,
                LocalDate.of(2025, 5, 1),
                DATE_FIN,
                1,
                ariary(-700_000));

        // Loyer payé tous les 26 du mois
        new FluxArgent(
                "Loyer colocation",
                courantBNI,
                LocalDate.of(2025, 4, 26),
                DATE_FIN,
                26,
                ariary(-600_000));

        return Set.of(courantBNI, epargneBMOI, coffre, ordinateur);
    }

    @Override
    public Patrimoine get() {
        var personneBako = new Personne("bako");
        var possessions = listePossessions();
        return Patrimoine.of(
                        "Patrimoine de Bako au 8 avril 2025", MGA, DATE_DEBUT, personneBako, possessions)
                .projectionFuture(DATE_FIN);
    }
}
