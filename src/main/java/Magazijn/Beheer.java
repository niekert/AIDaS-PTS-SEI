/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Magazijn;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import magazijnapplicatie.*;

/**
 *
 * @author Rob Maas
 */
public class Beheer implements IMagazijn,APInterface {

    //Variabelen
    /**
     * Deze klasse wordt gebruikt om de database te benaderen.
     */
    private DatabaseConnectie db;

    //Constructor
    /**
     * MaaktEEE een nieuwe databaseConnectie-klasse aan. Deze geeft toegang tot de methoden die nodig zijn om de database te benaderen.
     */
    public Beheer() {
        try {
            db = new DatabaseConnectie();
        } catch (Exception ex) {
            Logger.getLogger(Beheer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //<editor-fold desc="Methoden">

    //<editor-fold desc="Opvraag">
    /**
     * Haalt een lijst op van alle onderdelen uit de database.
     * @return Een lijst van IOnderdeel objecten (Interface).
     * Als er iets fout gegaan is wordt een lege ArrayList gereturneerd.
     * (Let dan wel op, want het kan ook zijn dat de query geen row teruggeeft!)
     */
    public ArrayList<IOnderdeel> GetOnderdelen() {
        ArrayList<IOnderdeel> ond = new ArrayList<IOnderdeel>();
        try {
            ond = db.GetOnderdelen();
        } catch (Exception e) {
            System.out.println("Er is iets fout gegaan bij het ophalen van de onderdelen.  " + e.getMessage());
        }
        return ond;
    }

    /**
     * Haalt een onderdeel op uit de database.
     * @param code De code van het op te halen onderdeel.
     * @return Het bijbehorende IOnderdeel-object (Interface).
     */
    public IOnderdeel GetOnderdeel(int code) {
        IOnderdeel ond = null;
        try {
            ond = db.GetOnderdeel(code);
        } catch (Exception e) {
            System.out.println("Er is iets fout gegaan bij het ophalen van het onderdeel.   " + e.getMessage());
        }
        return ond;
    }

    /**
     * Haalt een lijst op van alle klanten uit de database.
     * @return Een lijst van IKlant objecten (Interface).
     * Als er iets fout gegaan is wordt een lege ArrayList gereturneerd.
     * (Let dan wel op, want het kan ook zijn dat de query geen row teruggeeft!)
     */
    public ArrayList<IKlant> GetKlanten() {
        ArrayList<IKlant> klanten = null;
        try {
            klanten = db.GetKlanten();
        } catch (Exception e) {
            System.out.println("Er is iets fout gegaan bij het ophalen van de klanten.  " + e.getMessage());
        }
        return klanten;
    }

    /**
     * Haalt een lijst op van alle facturen uit de database.
     * @return Een lijst van IFactuur objecten (Interface).
     * Als er iets fout gegaan is wordt een lege ArrayList gereturneerd.
     * (Let dan wel op, want het kan ook zijn dat de query geen row teruggeeft!)
     */
    public ArrayList<IFactuur> GetFacturen() {
        ArrayList<IFactuur> facturen = null;
        try {
            facturen = db.GetFacturen();
        } catch (Exception e) {
            System.out.println("Er is iets fout gegaan bij het ophalen van de klanten.  " + e.getMessage());
        }
        return facturen;
    }
    //</editor-fold>

    //<editor-fold desc="ToeVoeg-Methoden">
    /**
     * Creert een Onderdeel object en deze wordt toegevoegd in de database.
     * Als het aantal of de prijs minder dan 0 is, de omschrijving null of gelijk is aan een lege string wordt de actie onderbroken: Returns -1.
     * @param omschrijving De omschrijving van het Onderdeel (zoals deze in de database wordt opgeslagen).
     * @param aantal Het x aantal keer dat het product in het magazijn wordt toegevoegd.
     * @param prijs De prijs van het Onderdeel (zoals deze in de database wordt opgeslagen).
     * @return True als het object correct is toegevoegd, false als dit niet het geval is.
     */
    public int VoegOnderdeelToe(String omschrijving, int aantal, int prijs) {
        if (aantal < 0 || prijs < 0 || omschrijving == null || omschrijving.equals("")) {
            return -1;
        }
        try {
            ArrayList<IOnderdeel> onderdelen = db.GetOnderdelen();
            int highest = 0;
            for (IOnderdeel ond : onderdelen) {
                if (ond.getCode() > highest) {
                    highest = ond.getCode();
                }
            }
            IOnderdeel ond = new Onderdeel((highest + 1), omschrijving, aantal, prijs);
            if (db.VoegOnderdeelToe(ond)) {
                return ond.getCode();
            }
        } catch (Exception e) {
            System.out.println("Er is iets fout gegaan met het toevoegen van het onderdeel.   " + e.getMessage());
        }
        return -1;
    }

    /**
     * Zoekt een geschikt klantnummer en voegt de nieuwe klant toe aan de database.
     * Als de parameters gelijk zijn aan null of gelijk zijn aan een lege string wordt de actie afgebroken: Returns -1.
     * @param naam De naam van de Klant (Zoals deze in de database wordt opgeslagen).
     * @param adres Het aders van de Klant (Zoals deze in de database wordt opgeslagen).
     * @return Het klantnummer als de klant correct is toegevoegd, returneert -1 als dit niet het geval is.
     */
    public int VoegKlantToe(String naam, String adres) {
        if (naam.equals("") || naam == null || adres == null || adres.equals("")) {
            return -1;
        }
        try {
            ArrayList<IKlant> klanten = db.GetKlanten();
            int highest = 0;
            for (IKlant kl : klanten) {
                if (kl.getId() > highest) {
                    highest = kl.getId();
                }
            }
            IKlant klant = new Klant((highest + 1), naam, adres);
            if (db.VoegKlantToe(klant)) {
                return klant.getId();
            }
        } catch (Exception e) {
            System.out.println("Er is iets fout gegaan met het toevoegen van de klant.    " + e.getMessage());
        }
        return -1;
    }

    /**
     * Zoekt een geschikt nieuw factuurnummer en voegt de factuur toe aan de database als de variabelen kloppen.
     * Ook wordt het aantal van de betreffende onderdelen in de database verlaagt met het aantal te bestellen van dat onderdeel.
     * @param klantId Het ID van de klant die de factuur aanvraagt.
     * @param onderdelen Lijst met onderdelen die besteld worden: Array van int[], waarvan int[0] de onderdeelcode is en int[1] het aantal te bestellen.
     * @return Als de factuur correct wordt toegevoegd wordt het factuurnummer gereturneerd, anders wordt -1 gereturneerd.
     */
    public int VoegFactuurToe(int klantId, ArrayList<IFactuurRegel> onderdelen) {
        try {
            ArrayList<IFactuur> facturen = db.GetFacturen();
            int factuurNr = 0;
            for (IFactuur fc : facturen) {
                if (fc.getFactuurId() > factuurNr) {
                    factuurNr = fc.getFactuurId();
                }
            }

            ArrayList<IKlant> klanten = db.GetKlanten();
            IKlant klant = null;
            for (int i = 0; i < klanten.size(); i++) {
                if (klanten.get(i).getId() == klantId) {
                    klant = klanten.get(i);
                }
            }
            if (klant == null) {
                return -1;
            }

            ArrayList<IOnderdeel> ond = db.GetOnderdelen();
            for (IFactuurRegel fr : onderdelen) {
                if (fr.getOnderdeelCode() < 1) {
                    return -1;
                }
                IOnderdeel odd = null;
                for (int i = 0; i < ond.size(); i++) {
                    if (ond.get(i).getCode() == fr.getOnderdeelCode()) {
                        odd = ond.get(i);
                    }
                }
                if (odd != null) {
                    if (odd.getAantal() < fr.getAantal()) {
                        return -1;
                    }
                } else {
                    return -1;
                }
            }

            for(IFactuurRegel fr: onderdelen){
                IOnderdeel on = null;
                for(IOnderdeel ondr:ond){
                    if(ondr.getCode() == fr.getOnderdeelCode()){
                        int temp = ondr.getAantal() - fr.getAantal();
                        on = new Onderdeel(ondr.getCode(),ondr.getOmschrijving(),temp,ondr.getPrijs());
                    }
                }
                if (!db.VeranderOnderdeel(on.getCode(), on)) {
                    System.out.println("Er is iets fout gegaan bij db.VeranderOnderdeel");
                }
            }
//
//            for (IFactuurRegel fr : onderdelen) {
//                int temp = ond.get(fr.getOnderdeelCode()-1).getAantal() - fr.getAantal();
//                IOnderdeel on = ond.get(fr.getOnderdeelCode()-1);
//                on.setAantal(temp);
//                if (!db.VeranderOnderdeel(on.getCode(), on)) {
//                    System.out.println("Er is iets fout gegaan bij db.VeranderOnderdeel");
//                }
//            }
            Calendar cal = new GregorianCalendar();
            int year = cal.get(Calendar.YEAR);
            int month  = cal.get(Calendar.MONTH) +1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String date = Integer.toString(day) + "-" + Integer.toString(month) + "-" + Integer.toString(year);

            if (db.VoegFactuurToe(new Factuur(date, klantId, (factuurNr+1), onderdelen))) {
                return factuurNr;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Er is iets fout gegaan met het toevoegen van de factuur.   " + e.getMessage());
        }
        return -1;
    }
//</editor-fold>

    //<editor-fold desc="Verwijder-Methoden">
    /**
     * Controleert of er een klant is met ingevoerde klantID, zoja wordt deze verwijdert.
     * @param klantId Het ID van de klant die verwijdert moet worden.
     * @return True als de klant correct verwijdert is. False als dit niet het geval is.
     */
    public boolean VerwijderKlant(int klantId) {
        if (klantId >= 0) {
            try {
                ArrayList<IKlant> klanten = db.GetKlanten();
                for (IKlant kl : klanten) {
                    if (kl.getId() == klantId) {
                        if (db.VerwijderKlant(kl.getId())) {
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Er is iets fout gegaan met het verwijderen van de klant.   " + e.getMessage());
                return false;
            }
        }
        return false;
    }

    /**
     * Controleert of er een onderdeel bestaat met ingevoerde OnderdeelCode, zoja wordt deze verwijdert.
     * @param onderdeelCode De Code die hoort bij het onderdeel wat verwijdert moet worden.
     * @return True als het onderdeel correct verwijdert is. False als dit niet geval is.
     */
    public boolean VerwijderOnderdeel(int onderdeelCode) {
        if (onderdeelCode >= 0) {
            try {
                ArrayList<IOnderdeel> onderdelen = db.GetOnderdelen();
                for (IOnderdeel odd : onderdelen) {
                    if (odd.getCode() == onderdeelCode) {
                        if (db.VerwijderOnderdeel(odd.getCode())) {
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Er is iets fout gegaan met het verwijderen van het onderdeel.    " + e.getMessage());
                return false;
            }
        }
        return false;
    }
//</editor-fold>
    
    //<editor-fold desc="Verander-Methoden">
    /**
     * (LET OP! De KlantID kan niet worden aangepast, enkel de naam en het adres!!)
     * Controleert of het ingevoerde IKlant-object correct is. Als dit zo is worden de veranderen aangebracht in de database.
     * Zowel de naam als het adres moet ingevoerd worden, als deze niet veranderd hoeven worden moeten de oude waardes ingevoerd worden.
     * @param klant Het IKlant-object met ingevoerde nieuwe waardes.
     * @return True als de Klantgegevens correct veranderd zijn, anders false.
     */
    public boolean VeranderKlant(IKlant klant) {
        if (klant != null && klant.getId() > 0 && klant.getAdres() != null && !klant.getAdres().equals("") && klant.getNaam() != null && !klant.getNaam().equals("")) {
            try {
                ArrayList<IKlant> klanten = db.GetKlanten();
                for (IKlant kl : klanten) {
                    if (kl.getId() == klant.getId()) {
                        if (db.VeranderKlant(klant.getId(), klant)) {
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Er is iets fout gegaan met het veranderen van de klant.    " + e.getMessage());
                return false;
            }
        }
        return false;
    }

    /**
     * (LET OP! De onderdeelCode kan niet worden aangepast, enkel de omschrijving het aantal en de prijs!)
     * Controleert of het ingevoerde IOnderdeel-obejct correct is. Als dit zo is worden de veranderingen aangebracht in de database.
     * De omschrijving, het aantal en de prijs moeten ingevoerd worden, als er niets veranderd hoeft te worden, voer dan de oude waardes in.
     * @param onderdeel Het IOnderdeel-object met ingevoerde nieuwe waardes.
     * @return True als de klantgegevens correct veranderd zijn, anders false.
     */
    public boolean VeranderOnderdeel(IOnderdeel onderdeel) {
        if (onderdeel.getCode() > 0 && onderdeel.getOmschrijving() != null && !onderdeel.getOmschrijving().equals("") && onderdeel.getAantal() > 0 && onderdeel.getPrijs() > 0) {
            try {
                ArrayList<IOnderdeel> onderdelen = db.GetOnderdelen();
                for (IOnderdeel odd : onderdelen) {
                    if (odd.getCode() == onderdeel.getCode()) {
                        if (db.VeranderOnderdeel(onderdeel.getCode(), onderdeel)) {
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Er is iets fout gegaan met het veranderen van het onderdeel.    " + e.getMessage());
                return false;
            }
        }
        return false;
    }
    //</editor-fold>

    //<editor-fold desc="API-Interface Methoden">
    /**
     * Haalt de omschrijving, prijs en het aantal van een onderdeel op.
     *
     * Deze methode wordt gebruikt door de APInterface.
     * @param onderdeelCode De code van het onderdeel waarvan de informatie moet worden opgehaald.
     * @return Een Object[] met de volgende waardes:
     * [0] String:  De omschrijving van het onderdeel.
     * [1] int:     De prijs van het onderdeel in centen.
     * [2] int:     Het aantal van het onderdeel.
     */
    public Object[] VraagOnderdeelOp(int onderdeelCode){
        Object[] temp = null;
        try{
            temp = db.VraagOnderdeelOp(onderdeelCode);
        }
        catch(Exception e){
            System.out.println("Er is iets fout gegaan bij het ophalen van het onderdeel.   " + e.getMessage());
        }
        return temp;
    }

    /**
     * Zoekt een geschikt nieuw factuurnummer en voegt de factuur toe als de variabelen kloppen, als de variabelen niet kloppen wordt -1 gereturneerd.
     * Ook wordt het aantal van de betreffende onderdelen in de database verlaagt met het aantal te bestellen van dat onderdeel,
     * als er niet genoeg onderdelen over zijn wordt ook -1 gereturneerd.
     * @param klantId Het ID van de klant die de factuur aanvraagt.
     * @param onderdelen Lijst met onderdelen die besteld worden:
     * De ArrayList heeft int[] objecten, en elke int[] staat voor een onderdeel:
     * int[0]: De code van het onderdeel.
     * int[1]: Het aantal van dat onderdeel dat besteld moet worden.
     * @return Als de factuur correct wordt toegevoegd wordt het factuurnummer gereturneerd, anders wordt -1 gereturneerd.
     */
    public int FactuurToeVoegen(int klantId, ArrayList<int[]> onderdelen) {        
        try{
            ArrayList<IFactuur> facturen = db.GetFacturen();
            int factuurNr = 0;
            for (IFactuur fc : facturen) {
                if (fc.getFactuurId() > factuurNr) {
                    factuurNr = fc.getFactuurId();
                }
            }

            ArrayList<IKlant> klanten = db.GetKlanten();
            IKlant klant = null;
            for (int i = 0; i < klanten.size(); i++) {
                if (klanten.get(i).getId() == klantId) {
                    klant = klanten.get(i);
                }
            }
            if (klant == null) {
                return -1;
            }

            ArrayList<IOnderdeel> ond = db.GetOnderdelen();
            for (int[] fr : onderdelen) {
                if (fr[0] < 1) {
                    return -1;
                }
                IOnderdeel odd = null;
                for (int i = 0; i < ond.size(); i++) {
                    if (ond.get(i).getCode() == fr[0]) {
                        odd = ond.get(i);
                    }
                }
                if (odd != null) {
                    if (odd.getAantal() < fr[1]) {
                        return -1;
                    }
                } else {
                    return -1;
                }
            }

            for(int[] fr: onderdelen){
                IOnderdeel on = null;
                for(IOnderdeel ondr:ond){
                    if(ondr.getCode() == fr[0]){
                        int temp = ondr.getAantal() - fr[1];
                        on = new Onderdeel(ondr.getCode(),ondr.getOmschrijving(),temp,ondr.getPrijs());
                    }
                }
                if (!db.VeranderOnderdeel(on.getCode(), on)) {
                    System.out.println("Er is iets fout gegaan bij db.VeranderOnderdeel");
                }
            }

//            for (int[] fr : onderdelen) {
//                int temp = ond.get(fr[0]).getAantal() - fr[1];
//                IOnderdeel on = ond.get(fr[0]);
//                on.setAantal(temp);
//                if (!db.VeranderOnderdeel(on.getCode(), on)) {
//                    System.out.println("Er is iets fout gegaan bij db.VeranderOnderdeel");
//                }
//            }

            Calendar cal = new GregorianCalendar();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH)+1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String date = Integer.toString(day) + "-" + Integer.toString(month) + "-" + Integer.toString(year);

            if (db.FactuurToevoegen(factuurNr+1, klantId, date, onderdelen)) {
                return factuurNr;
            }
        }
        catch(Exception e){
            System.out.println("Er is iets fout gegaan met het toevoegen van de factuur.   " + e.getMessage());
        }
        return -1;
    }
    //</editor-fold>

    //</editor-fold>
}
