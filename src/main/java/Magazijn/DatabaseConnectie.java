/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Magazijn;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import magazijnapplicatie.IFactuur;
import magazijnapplicatie.IFactuurRegel;
import magazijnapplicatie.IKlant;
import magazijnapplicatie.IOnderdeel;

/**
 *
 * @author Rob Maas
 */
public class DatabaseConnectie {

    /**
     * Het connectie-object voor de database.
     */
    private Connection conn = null;
    /**
     * De connectie-url voor de database.
     */
    private String url;
    /**
     * De username van de database.
     */
    private String username;
    /**
     * Het password van de database.
     */
    private String password;

    /**
     * Het server-IP en de poort nummer, bijvoorbeeld in deze vorm:
     * 127.0.0.1:1521
     */
    private String serverEnPort;

    /**
     * Maakt een nieuw databaseConnectie-object.
     * Zoekt connectie met de database met de properties die op worden gehaald uit de file: db.properties.
     */
    public DatabaseConnectie() {
        try {
            Properties pr = new Properties();
            pr.load(new FileInputStream("db.properties"));
            serverEnPort = pr.getProperty("ServerEnPort");
            username = pr.getProperty("Username");
            password = pr.getProperty("Password");
            url = "jdbc:oracle:thin:@" + serverEnPort + ":xe";
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(url, this.username, this.password);
            conn.setAutoCommit(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "De database connectie is niet goed verlopen, fout bij IP:Port, Username of Password! \r\n" + ex.getMessage(), "Database - Error!", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    //<editor-fold desc="Opvraag-methoden">
    /**
     * Haalt zo mogelijk een lijst van onderdelen op uit de database.
     * @return Een lijst met IOnderdeel-objecten, als het fout gaat of de tabel leeg is wordt een lege lijst gereturneerd.
     */
    public ArrayList<IOnderdeel> GetOnderdelen() {
        ArrayList<IOnderdeel> ond = new ArrayList<IOnderdeel>();
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            String sql = "SELECT * FROM ONDERDEEL";
            rs = st.executeQuery(sql);
            while (rs.next()) {
                int code = rs.getInt("Code");
                String omschr = rs.getString("Omschrijving");
                int aantal = rs.getInt("Aantal");
                int prijs = rs.getInt("Prijs");
                ond.add(new Onderdeel(code, omschr, aantal, prijs));
            }
        } catch (Exception e) {
            System.out.println("FOUT!" + e.getMessage());
        } finally {
            try {
                st.close();
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnectie.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ond;
    }

    /**
     * Haalt zo mogelijk een lijst van factuurregels op uit de database.
     * @return Een lijst met IFactuurRegel-objecten, als het fout gaat of de tabel leeg is wordt een lege lijst gereturneerd.
     */
    public ArrayList<IFactuurRegel> GetFactuurRegels() {
        ArrayList<IFactuurRegel> frs = new ArrayList<IFactuurRegel>();
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            String sql = "SELECT * FROM FACTUURREGEL";
            rs = st.executeQuery(sql);
            while (rs.next()) {
                int factuurid = rs.getInt("FactuurId");
                int onderdeelcode = rs.getInt("OnderdeelCode");
                int aantal = rs.getInt("Aantal");
                frs.add(new FactuurRegel(factuurid, onderdeelcode, aantal));
            }
        } catch (Exception e) {
            System.out.println("FOUT!!  " + e.getMessage());
        } finally {
            try {
                rs.close();
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnectie.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return frs;
    }

    /**
     * Haalt zo mogelijk een lijst van facturen op uit de database.
     * @return Een lijst met IFactuur-objecten, als het fout gaat of de tabel leeg is wordt een lege lijst gereturneerd.
     */
    public ArrayList<IFactuur> GetFacturen() {
        ArrayList<IFactuur> facturen = new ArrayList<IFactuur>();
        ArrayList<IFactuurRegel> regels = GetFactuurRegels();
        ResultSet rs = null;
        Statement st = null;
        try {
            st = conn.createStatement();
            String sql = "SELECT * FROM FACTUUR";
            rs = st.executeQuery(sql);
            int count = 0;
            while (rs.next()) {
                int code = rs.getInt("FactuurCode");
                int klantid = rs.getInt("KlantId");
                String datum = rs.getString("Datum");
                ArrayList<IFactuurRegel> regelz = new ArrayList<IFactuurRegel>();
                for (IFactuurRegel rgl : regels) {
                    if (rgl.getFactuurId() == code) {
                        regelz.add(rgl);
                    }
                }
                facturen.add(new Factuur(datum, klantid, code, regelz));
                count++;
            }
        } catch (Exception e) {
            System.out.println("FOUT!  " + e.getMessage());
        } finally {
            try {
                st.close();
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnectie.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return facturen;
    }

    /**
     * Haalt zo mogelijk een lijst van klanten op uit de database.
     * @return Ee lijst met IKlant-objecten, als het fout gaat of de tabel leeg is wordt een lege lijst gereturneerd.
     */
    public ArrayList<IKlant> GetKlanten() {
        ArrayList<IKlant> klanten = new ArrayList<IKlant>();
        ResultSet rs = null;
        Statement st = null;
        try {
            st = conn.createStatement();
            String sql = "SELECT * FROM KLANT";
            rs = st.executeQuery(sql);
            while (rs.next()) {
                int code = rs.getInt("Id");
                String naam = rs.getString("Naam");
                String adres = rs.getString("Adres");
                IKlant kl = new Klant(code, naam, adres);
                klanten.add(kl);
            }

        } catch (Exception e) {
            System.out.println("FOUT!  " + e.getMessage());
        } finally {
            try {
                st.close();
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnectie.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return klanten;
    }

    /**
     * Haalt zo mogelijk het onderdeel op uit de database die hoort bij de ingevoerde onderdeelcode.
     * @param onderdeelCode De code van het onderdeel dat opgehaald moet worden.
     * @return Een IOnderdeel-object dat de waardes heeft van een row uit de Onderdeel-tabel.
     */
    public IOnderdeel GetOnderdeel(int onderdeelCode) {
        IOnderdeel od = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            String sql = "SELECT * FROM ONDERDEEL WHERE CODE = " + Integer.toString(onderdeelCode);
            rs = st.executeQuery(sql);
            while (rs.next()) {
                int code = rs.getInt("Code");
                String omschr = rs.getString("Omschrijving");
                int aantal = rs.getInt("Aantal");
                int prijs = rs.getInt("Prijs");
                od = new Onderdeel(code, omschr, aantal, prijs);
            }
        } catch (Exception e) {
            System.out.println("FOUT!!   " + e.getMessage());
        } finally {
            try {
                rs.close();
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnectie.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return od;
    }
    //</editor-fold>

    //<editor-fold desc="Toevoeg-Methoden">
    /**
     * Voegt een Onderdeel toe in de database.
     * @param onderdeel Het onderdeel dat moet worden toegevoegd in de database.
     * @return True als het onderdeel correct is toegevoegd, anders false.
     */
    public boolean VoegOnderdeelToe(IOnderdeel onderdeel) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("INSERT INTO ONDERDEEL VALUES(?,?,?,?)");
            st.setInt(1, onderdeel.getCode());
            st.setString(2, onderdeel.getOmschrijving());
            st.setInt(3, onderdeel.getAantal());
            st.setInt(4, onderdeel.getPrijs());
            st.executeUpdate();
            return true;
        } catch (Exception ex) {
            System.out.println("FOUT!!!   " + ex.getMessage());
            return false;
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnectie.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Voegt een klant toe aan de database.
     * @param klant De klant die moet worden toegevoegd in de database.
     * @return True als de klant correct is toegevoegd, anders false.
     */
    public boolean VoegKlantToe(IKlant klant) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("INSERT INTO KLANT VALUES (?,?,?)");
            st.setInt(1, klant.getId());
            st.setString(2, klant.getNaam());
            st.setString(3, klant.getAdres());
            st.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("FOUT!!  " + e.getMessage());
            return false;
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnectie.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Voegt een row toe aan de factuurregel tabel met bijbehorende factuurID, OnderdeelID en het aantal van het Onderdeel.
     * (Deze methode wordt alleen gebruikt door de methode VoegFactuurToe in deze klasse).
     * @param fr De regel die moet worden toegevoegd in de database.
     */
    public void VoegFactuurRegelToe(int factuurid,IFactuurRegel fr){
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("INSERT INTO FACTUURREGEL VALUES (?,?,?)");
            st.setInt(1, factuurid);
            st.setInt(2, fr.getOnderdeelCode());
            st.setInt(3, fr.getAantal());
            st.executeUpdate();
        }
        catch(Exception ex){
            System.out.println("FOUT!!!    " + ex.getMessage());
        }
        finally{
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnectie.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Voegt een factuur toe aan de database, met bijbehorende ingevoerde waardes.
     * Voegt ook de factuurregels toe aan de database, waarin staat: FactuurID, OnderdeelID en aantal van het onderdeel.
     * @param factuur De factuur die moet worden toegevoegd.
     * @return True als de factuur correct is toegevoegd, anders false.
     */
    public boolean VoegFactuurToe(IFactuur factuur) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("INSERT INTO FACTUUR VALUES (?,?,?)");
            st.setInt(1, factuur.getFactuurId());
            st.setInt(2, factuur.getKlantId());
            st.setString(3, factuur.getDatum());
            st.executeUpdate();

            for(IFactuurRegel fr : factuur.getOnderdelen()){
                VoegFactuurRegelToe(factuur.getFactuurId(),fr);
            }
            return true;
        }
        catch(Exception e){
            System.out.println("FOUT!!!   " + e.getMessage());
            return false;
        }
        finally
        {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnectie.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
//</editor-fold>

    //<editor-fold desc="Verwijder-methoden">
    /**
     * Verwijdert het onderdeel dat hoort bij het ingevoerde ID.
     * @param id Het ID van het onderdeel dat moet worden verwijdert.
     * @return True als het onderdeel correct is verwijdert, anders false.
     */
    public boolean VerwijderOnderdeel(int id) {
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement("DELETE FROM ONDERDEEL WHERE CODE = ?");
            pst.setInt(1, id);
            pst.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("FOUT!!  " + e.getMessage());
        } finally {
            try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnectie.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    /**
     * Verwijdert de klant uit de database die hoort bij de ingevoerde ID.
     * @param id Het ID van de klant die verwijdert moet worden.
     * @return True als de klant correct is verwijdert, anders false.
     */
    public boolean VerwijderKlant(int id) {
        PreparedStatement pst = null;
        try{
            pst = conn.prepareStatement("DELETE FROM KLANT WHERE ID = ?");
            pst.setInt(1, id);
            pst.executeUpdate();
            return true;
        }
        catch(Exception e){
            System.out.println("FOUT!!!    " + e.getMessage());
        }
        finally{
            try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnectie.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
//</editor-fold>

    //<editor-fold desc="Verander-Methoden">
    /**
     * Verandert het onderdeel dat hoort bij de ingoeverde code, de nieuwe waardes zitten in het IOnderdeel-object.
     * (Alleen de omschrijving, het aantal en de prijs kan veranderd worden, de code blijft hetzelfde!).
     * @param onderdeelCode De code die hoort bij het onderdeel dat verandert moet worden.
     * @param nieuweGegevens De nieuwe gegevens van het onderdeel.
     * @return True als het onderdeel correct is veranderd, anders false.
     */
    public boolean VeranderOnderdeel(int onderdeelCode, IOnderdeel nieuweGegevens) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("UPDATE ONDERDEEL SET OMSCHRIJVING = ?, AANTAL = ?, PRIJS = ? WHERE CODE = ?");
            st.setString(1, nieuweGegevens.getOmschrijving());
            st.setInt(2, nieuweGegevens.getAantal());
            st.setInt(3, nieuweGegevens.getPrijs());
            st.setInt(4, onderdeelCode);
            st.executeUpdate();
            return true;
        }
        catch(Exception e){
            System.out.println("FOUT!!!  " + e.getMessage());
            return false;
        }
        finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnectie.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Verandert de klant die hoort bij de ingevoerde ID, de nieuwe waardes zitten in het IKlant-object.
     * (Alleen de naam en het adres kan veranderd worden, de ID blijft hetzelfde!).
     * @param klantcode Het ID van de klant die veranderd moet worden.
     * @param nieuweGegevens De nieuwe gegevens van de klant.
     * @return True als de klant correct is veranderd, anders false.
     */
    public boolean VeranderKlant(int klantcode, IKlant nieuweGegevens) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("UPDATE KLANT SET NAAM = ?, ADRES = ? WHERE ID = ?");
            st.setString(1, nieuweGegevens.getNaam());
            st.setString(2, nieuweGegevens.getAdres());
            st.setInt(3, klantcode);
            st.executeUpdate();
            return true;
        }
        catch(Exception e){
            System.out.println("FOUT!!!   " +e.getMessage());
            System.out.println(e.toString());
            return false;
        }
        finally{
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnectie.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="APInterface-Methoden">

    /**
     * Haalt zo mogelijk het onderdeel op uit de database die hoort bij de ingevoerde onderdeelcode.
     *
     * Deze methode wordt gebruikt door de APInterface
     * @param onderdeelCode De code van het onderdeel dat opgehaald moet worden.
     * @return Een Object[] dat de volgende waardes heeft:
     * [0] String:  De omschrijving van het onderdeel.
     * [1] int:     De prijs van het onderdeel in centen.
     * [2] int:     Het aantal van het onderdeel.
     */
    public Object[] VraagOnderdeelOp(int onderdeelCode) {
        Object[] od = new Object[3];
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            String sql = "SELECT * FROM ONDERDEEL WHERE CODE = " + Integer.toString(onderdeelCode);
            rs = st.executeQuery(sql);
            while (rs.next()) {
                int code = rs.getInt("Code");
                String omschr = rs.getString("Omschrijving");
                int aantal = rs.getInt("Aantal");
                int prijs = rs.getInt("Prijs");
                od[0] = omschr;
                od[1] = prijs;
                od[2] = aantal;
            }
        } catch (Exception e) {
            System.out.println("FOUT!!   " + e.getMessage());
        } finally {
            try {
                rs.close();
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnectie.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return od;
    }

    /**
     * Voegt een factuur toe aan de Factuur-tabel.
     * @param factuurid Het id van de factuur.
     * @param klantid Het ID van de klant.
     * @param datum De datum: dd-mm-yyyy.
     */
    public boolean FactuurToevoegen(int factuurid,int klantid, String datum, ArrayList<int[]> onderdelen){
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("INSERT INTO FACTUUR VALUES (?,?,?)");
            st.setInt(1, factuurid);
            st.setInt(2, klantid);
            st.setString(3, datum);
            st.executeUpdate();

            for(int[] ob : onderdelen){
                FactuurRegelToevoegen(factuurid,ob[0],ob[1]);
            }
            return true;
        }
        catch(Exception e){
            System.out.println("FOUT!!!   " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        finally
        {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnectie.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Voegt een regel toe aan de FactuurRegel-tabel.
     * @param factuurid Het ID van de Factuur.
     * @param onderdeelcode De code van het onderdeel.
     * @param aantal Het aantal van desbetreffende onderdeel.
     */
    public void FactuurRegelToevoegen(int factuurid,int onderdeelcode, int aantal){
    PreparedStatement st = null;
        try{
            st = conn.prepareStatement("INSERT INTO FACTUURREGEL VALUES (?,?,?)");
            st.setInt(1, factuurid);
            st.setInt(2, onderdeelcode);
            st.setInt(3, aantal);
            st.executeUpdate();
        }
        catch(Exception ex){
            System.out.println("FOUT!!!    " + ex.getMessage());
        }
        finally{
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnectie.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    //</editor-fold>
}
