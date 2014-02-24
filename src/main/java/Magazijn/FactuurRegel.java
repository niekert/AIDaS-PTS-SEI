/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Magazijn;

import magazijnapplicatie.IFactuurRegel;

/**
 *
 * @author Rob Maas
 */
public class FactuurRegel implements IFactuurRegel{

    /**
     * Het ID van de factuur.
     */
    private int factuurId;
    /**
     * Het ID van het onderdeel.
     */
    private int onderdeelCode;
    /**
     * Het aantal van het onderdeel.
     */
    private int aantal;

    /**
     * Een nieuw FactuurRegel object met ingevoerde waardes.
     * @param factuurId
     * @param onderdeelCode
     * @param aantal
     */
    public FactuurRegel(int factuurId,int onderdeelCode, int aantal){
        this.factuurId = factuurId;
        this.onderdeelCode = onderdeelCode;
        this.aantal = aantal;
    }

    /**
     * Geeft het ID van de factuur.
     * @return Het ID van de factuur.
     */
    public int getFactuurId(){
        return this.factuurId;
    }

    /**
     * Geeft het ID van het onderdeel.
     * @return Het ID van het onderdeel.
     */
    public int getOnderdeelCode(){
        return this.onderdeelCode;
    }

    /**
     * Geeft het aantal van het onderdeel.
     * @return Het aantal van het onderdeel.
     */
    public int getAantal(){
        return this.aantal;
    }
}
