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
public interface APInterface {

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
    public Object[] VraagOnderdeelOp(int onderdeelCode);

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
    public int FactuurToeVoegen(int klantId, ArrayList<int[]> onderdelen);


}
