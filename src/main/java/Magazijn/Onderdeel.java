/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Magazijn;

import magazijnapplicatie.IOnderdeel;

/**
 *
 * @author Rob Maas
 */
public class Onderdeel implements IOnderdeel{
    /**
     * De code(ID) van het onderdeel.
     */
    private int code;
    /**
     * De omschrijving van het onderdeel.
     */
    private String omschrijving;
    /**
     * Het aantal van het onderdeel.
     */
    private int aantal;
    /**
     * De prijs van het object in centen.
     */
    private int prijs;

    /**
     * Creert een nieuw onderdeel-object met de parameters.
     * @param code De code van het onderdeel.
     * @param omschrijving De omschrijving van het onderdeel.
     * @param aantal Het aantal van het onderdeel.
     * @param prijs De prijs van het onderdeel in centen.
     */
    public Onderdeel(int code, String omschrijving, int aantal, int prijs){
        this.code = code;
        this.omschrijving = omschrijving;
        this.aantal = aantal;
        this.prijs = prijs;
    }

    /**
     * Geeft de code(ID) van het desbetreffende Onderdeel-object.
     * @return De code(ID) van het desbetreffende Onderdeel-object.
     */
    public int getCode(){
        return this.code;
    }

    /**
     * Geeft de omschrijving van het desbetreffende Onderdeel-object.
     * @return De omschrijving van het desbetreffende Onderdeel-object.
     */
    public String getOmschrijving(){
        return this.omschrijving;
    }

    /**
     * Geeft het aantal van het desbetreffende Onderdeel-object.
     * @return Het aantal van het desbetreffende Onderdeel-object.
     */
    public int getAantal(){
        return this.aantal;
    }

    /**
     * Geeft de prijs van het desbetreffende Onderdeel-object.
     * @return De prijs van het desbetreffende Onderdeel-object.
     */
    public int getPrijs(){
        return this.prijs;
    }

    /**
     * Set het aantal van het desbetreffende Onderdeel-object.
     * @param aantal De nieuwe waarde voor het aantal van het onderdeel-object.
     */
    public void setAantal(int aantal){
        this.aantal = aantal;
    }
}
