/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Magazijn;

import magazijnapplicatie.IKlant;

/**
 *
 * @author Rob Maas
 */
public class Klant implements IKlant{
    /**
     * Het ID van de klant.
     */
    private int id;
    /**
     * De naam van de klant.
     */
    private String naam;
    /**
     * Het adres van de klant.
     */
    private String adres;

    /**
     * Er wordt een nieuw Klant-object aangemaakt met ingevoerde waardes.
     * @param id Het ID van de klant.
     * @param naam De naam van de klant.
     * @param adres Het adres van de klant.
     */
    public Klant(int id, String naam, String adres){
        this.id = id;
        this.naam = naam;
        this.adres = adres;
    }

    /**
     * De naam van de klant.
     * @return De naam van de klant.
     */
    public String getNaam()
    {
        return this.naam;
    }

    /**
     * Geeft het ID van de klant.
     * @return Het ID van de klant.
     */
    public int getId()
    {
        return this.id;
    }

    /**
     * Geeft het adres van de klant.
     * @return Het adres van de klant.
     */
    public String getAdres()
    {
        return this.adres;
    }
}
