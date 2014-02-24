/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Magazijn;

import java.util.ArrayList;
import magazijnapplicatie.IFactuur;
import magazijnapplicatie.IFactuurRegel;

/**
 *
 * @author Rob Maas
 */
public class Factuur implements IFactuur{
    /**
     * De datum van het factuur.
     */
    private String datum;
    /**
     * Het ID van de klant.
     */
    private int klantId;
    /**
     * Het ID van de factuur.
     */
    private int factuurId;
    /**
     * De lijst met IFactuurRegel-objecten (bevat Onderdeel + aantal).
     */
    private ArrayList<IFactuurRegel> onderdelen;

    /**
     * Nieuw Factuur-object met ingevoerde waardes.
     * @param datum De datum van de factuur.
     * @param klantId Het ID van de klant.
     * @param factuurId Het ID van de factuur.
     * @param onderdelen Lijst met IFactuurRegel objecten die bij de factuur horen.
     */
    public Factuur(String datum, int klantId, int factuurId, ArrayList<IFactuurRegel> onderdelen){
        this.datum = datum;
        this.klantId = klantId;
        this.factuurId = factuurId;
        if(onderdelen != null)
        {
            this.onderdelen = onderdelen;
        }
        else
        {
            this.onderdelen = new ArrayList<IFactuurRegel>();
        }
    }

    /**
     * Geeft de datum van de factuur.
     * @return De datum van de factuur.
     */
    public String getDatum(){
        return this.datum;
    }

    /**
     * Geeft de ID van de klant.
     * @return De ID van de klant.
     */
    public int getKlantId(){
        return this.klantId;
    }

    /**
     * Geeft de ID van de factuur.
     * @return De ID van de factuur.
     */
    public int getFactuurId(){
        return this.factuurId;
    }

    /**
     * Geeft een lijst met IFactuurRegel-objecten.
     * @return Lijst met IFactuurRegel-objecten.
     */
    public ArrayList<IFactuurRegel> getOnderdelen(){
        return this.onderdelen;
    }
}
