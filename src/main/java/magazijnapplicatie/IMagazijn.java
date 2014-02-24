/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magazijnapplicatie;

import java.util.ArrayList;

/**
 *
 * @author Rob Maas
 */
public interface IMagazijn {

    /**
     * Haalt een lijst op van alle onderdelen uit de database.
     * @return Een lijst van IOnderdeel objecten (Interface).
     * Als er iets fout gegaan is wordt een lege ArrayList gereturneerd.
     * (Let dan wel op, want het kan ook zijn dat de query geen row teruggeeft!)
     */
    public ArrayList<IOnderdeel> GetOnderdelen();

    /**
     * Haalt een onderdeel op uit de database.
     * @param code De code van het op te halen onderdeel.
     * @return Het bijbehorende IOnderdeel-object (Interface).
     */
    public IOnderdeel GetOnderdeel(int code);

    /**
     * Haalt een lijst op van alle klanten uit de database.
     * @return Een lijst van IKlant objecten (Interface).
     * Als er iets fout gegaan is wordt een lege ArrayList gereturneerd.
     * (Let dan wel op, want het kan ook zijn dat de query geen row teruggeeft!)
     */
    public ArrayList<IKlant> GetKlanten();

    /**
     * Haalt een lijst op van alle facturen uit de database.
     * @return Een lijst van IFactuur objecten (Interface).
     * Als er iets fout gegaan is wordt een lege ArrayList gereturneerd.
     * (Let dan wel op, want het kan ook zijn dat de query geen row teruggeeft!)
     */
    public ArrayList<IFactuur> GetFacturen();

    /**
     * Creert een Onderdeel object en deze wordt toegevoegd in de database.
     * Als het aantal of de prijs minder dan 0 is, de omschrijving null of gelijk is aan een lege string wordt de actie onderbroken: Returns -1.
     * @param code De code van het Onderdeel (zoals deze in de database wordt opgeslagen).
     * @param omschrijving De omschrijving van het Onderdeel (zoals deze in de database wordt opgeslagen).
     * @param aantal Het x aantal keer dat het product in het magazijn wordt toegevoegd.
     * @param prijs De prijs van het Onderdeel (zoals deze in de database wordt opgeslagen).
     * @return True als het object correct is toegevoegd, false als dit niet het geval is.
     */
    public int VoegOnderdeelToe(String omschrijving, int aantal, int prijs);

    /**
     * Zoekt een geschikt nieuw klantnummer en voegt de nieuwe klant toe aan de database.
     * Als de parameters gelijk zijn aan null of gelijk zijn aan een lege string wordt de actie afgebroken: Returns -1.
     * @param naam De naam van de Klant (Zoals deze in de database wordt opgeslagen).
     * @param adres Het aders van de Klant (Zoals deze in de database wordt opgeslagen).
     * @return Het klantnummer als de klant correct is toegevoegd, returneert -1 als dit niet het geval is.
     */
    public int VoegKlantToe(String naam, String adres);

    /**
     * Zoekt een geschikt nieuw factuurnummer en voegt de factuur toe aan de database als de variabelen kloppen.
     * Ook wordt het aantal van de betreffende onderdelen in de database verlaagt met het aantal te bestellen van dat onderdeel.
     * @param klantId Het ID van de klant die de factuur aanvraagt.
     * @param onderdelen Lijst met onderdelen die besteld worden: Array van int[], waarvan int[0] de onderdeelcode is en int[1] het aantal te bestellen.
     * @return Als de factuur correct wordt toegevoegd wordt het factuurnummer gereturneerd, anders wordt -1 gereturneerd.
     */
    public int VoegFactuurToe(int klantId, ArrayList<IFactuurRegel> onderdelen);

    /**
     * Controleert of er een klant is met ingevoerde klantID, zoja wordt deze verwijdert.
     * @param klantId Het ID van de klant die verwijdert moet worden.
     * @return True als de klant correct verwijdert is. False als dit niet het geval is.
     */
    public boolean VerwijderKlant(int klantId);

    /**
     * Controleert of er een onderdeel bestaat met ingevoerde OnderdeelCode, zoja wordt deze verwijdert.
     * @param onderdeelCode De Code die hoort bij het onderdeel wat verwijdert moet worden.
     * @return True als het onderdeel correct verwijdert is. False als dit niet geval is.
     */
    public boolean VerwijderOnderdeel(int onderdeelCode);

    /**
     * (LET OP! De KlantID kan niet worden aangepast, enkel de naam en het adres!!)
     * Controleert of de ingevoerde waardes correct zijn. Als dit zo is worden de veranderen aangebracht in de database.
     * Zowel de naam als het adres moet ingevoerd worden, als deze niet veranderd hoeven worden moeten de oude waardes ingevoerd worden.
     * @param klant Het IKlant-object met de nieuwe waardes.
     * @return True als de veranderingen correct zijn toegepast, anders False.
     */
    public boolean VeranderKlant(IKlant klant);

    /**
     * (LET OP! De onderdeelCode kan niet worden aangepast, enkel de omschrijving het aantal en de prijs!)
     * Controleert of ingevoerde waardes correct zijn. Als dit zo is worden de veranderingen aangebracht in de database.
     * De omschrijving, het aantal en de prijs moeten ingevoerd worden, als er niets veranderd hoeft te worden, voer dan de oude waardes in.
     * @param onderdeel Het IOnderdeel-object met de nieuwe waardes.
     * @return True als de veranderingen correct zijn toegepast, anders false.
     */
    public boolean VeranderOnderdeel(IOnderdeel onderdeel);
}
