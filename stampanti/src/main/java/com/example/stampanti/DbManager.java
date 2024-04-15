package com.example.stampanti;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class DbManager {
    final String JDBC_URL = "jdbc:mysql://localhost:3306/gestione_stampe";
    final String USERNAME = "root";
    final String PASSWORD = "";

    public String convertToMD5(String input) {
        try {
            // Crea un oggetto MessageDigest con l'algoritmo MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Aggiungi i byte della stringa di input al MessageDigest
            md.update(input.getBytes());

            // Calcola il digest MD5
            byte[] digest = md.digest();

            // Converte il digest in una rappresentazione esadecimale
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                hexString.append(String.format("%02x", b & 0xff));
            }
            return hexString.toString(); // Restituisce l'hash MD5 come stringa esadecimale
        } catch (NoSuchAlgorithmException e) {
            // Gestisci il caso in cui l'algoritmo MD5 non è disponibile
            e.printStackTrace();
            return null;
        }
    }

    public MySession loginUser(String codice, String pw) {
        String select = "SELECT * FROM user WHERE codice = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(select)) {
            // Imposta i parametri della query
            stmt.setString(1, codice);
            stmt.setString(2, convertToMD5(pw));

            // Esegui la query
            try (ResultSet rs = stmt.executeQuery()) {
                // Controlla se ci sono risultati
                if (rs.next()) {
                    // Se ci sono risultati, restituisci true
                    return new MySession(rs.getInt("ID"), rs.getString("ruolo"));

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // In caso di eccezione, restituisci false
            return null;
        }
        // Se non ci sono risultati o in caso di eccezione, restituisci false
        return null;
    }

    public List<stampa> getCoda(int id) {
        List<stampa> risultati = new ArrayList<>();
        String query = "SELECT * FROM stampa JOIN log_stampante ON log_stampante.ID_stampa = stampa.ID WHERE log_stampante.stato = ? AND log_stampante.ID_user = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(query)) {
            // Imposta il valore del parametro della query con il valore dell'enum
            stmt.setString(1, "in coda"); // Supponendo che 'coda' sia l'istanza dell'enum
            stmt.setInt(2, id); // Supponendo che 'coda' sia l'istanza dell'enum
            
            // Esegui la query
            try (ResultSet rs = stmt.executeQuery()) {
                // Controlla se ci sono risultati
                while (rs.next()) {
                    // Aggiungi il risultato alla lista
                    risultati.add(new stampa(rs.getString("fronte"), rs.getString("retro"), rs.getInt("ID"), rs.getBoolean("colorata")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // In caso di eccezione, restituisci null
            return null;
        }
        return risultati;
    }
    
    public List<stampa> getStats(){
        List<stampa> risultati = new ArrayList<>();
        String query = "SELECT * FROM stampa JOIN log_stampante ON log_stampante.ID_stampa = stampa.ID WHERE log_stampante.stato = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(query)) {
            // Imposta il valore del parametro della query con il valore dell'enum
            stmt.setString(1, "stampata"); // Supponendo che 'coda' sia l'istanza dell'enum
            
            // Esegui la query
            try (ResultSet rs = stmt.executeQuery()) {
                // Controlla se ci sono risultati
                while (rs.next()) {
                    // Aggiungi il risultato alla lista
                    risultati.add(new stampa(rs.getString("fronte"), rs.getString("retro"), rs.getInt("ID"), rs.getBoolean("colorata")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // In caso di eccezione, restituisci null
            return null;
        }
        return risultati;
    }

    public String sistemaPath(String origin) {
        String[] val = origin.split("\\\\");

        return "imgs\\" + val[val.length - 1];
    }

    public boolean inserisci_coda(String fronte, String retro, boolean color, int id_user) {
        String insertStampa = "INSERT INTO stampa (fronte, retro, colorata) VALUES (?, ?, ?)";
        String insertLogStampante = "INSERT INTO log_stampante (ID_user, data_ora, ID_stampa, stato) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                PreparedStatement stmtStampa = conn.prepareStatement(insertStampa);
                PreparedStatement stmtLogStampante = conn.prepareStatement(insertLogStampante)) {
            // Inserisci nella tabella 'stampa'
            stmtStampa.setString(1, sistemaPath(fronte));
            stmtStampa.setString(2, sistemaPath(retro));
            stmtStampa.setBoolean(3, color);
            stmtStampa.executeUpdate();

            // Ottieni l'ID della stampa appena inserita
            int id_stampa = 0;
            String query = "SELECT MAX(ID) AS max_id FROM stampa";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                    ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    id_stampa = rs.getInt("max_id");
                }   
            }

            // Inserisci nella tabella 'log_stampante'
            LocalDateTime currentDateTime = LocalDateTime.now();
            stmtLogStampante.setInt(1, id_user);
            stmtLogStampante.setTimestamp(2, Timestamp.valueOf(currentDateTime));
            stmtLogStampante.setInt(3, id_stampa);
            stmtLogStampante.setString(4, "in coda");
            int result = stmtLogStampante.executeUpdate();

            return result == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getGeneri() {
        List<String> risultati = new ArrayList<>();

        String query = "SELECT * FROM elenco_generi";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(query)) {

            // Esegui la query
            try (ResultSet rs = stmt.executeQuery()) {
                // Controlla se ci sono risultati
                while (rs.next()) {
                    // Se ci sono risultati, restituisci true
                    risultati.add(rs.getString("genere"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // In caso di eccezione, restituisci false
            return null;
        }
        return risultati;
    }

    public boolean registerUser(String codice, String pass, String nome, String cognome,String permessi) {
        String insert = "INSERT INTO user (codice, password, nome, cognome, ruolo,fondi) VALUES (?, ?, ?, ?, ?,0)";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(insert)) {
            // Imposta i parametri della query
            stmt.setString(1, codice);
            stmt.setString(2, convertToMD5(pass));
            stmt.setString(3, nome);
            stmt.setString(4, cognome);
            stmt.setString(5, permessi);

            // Esegui la query
            int result = stmt.executeUpdate();
            return result == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            // In caso di eccezione, restituisci false
            return false;
        }
    }

    public boolean controllaPermessi(String codice) {
        String select = "SELECT * FROM utente WHERE codice = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(select)) {
            // Imposta i parametri della query
            stmt.setString(1, codice);

            // Esegui la query
            try (ResultSet rs = stmt.executeQuery()) {
                // Controlla se ci sono risultati
                if (rs.next()) {
                    // Se ci sono risultati, restituisci true
                    if (rs.getInt("permessi_admin") == 1) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // In caso di eccezione, restituisci false
            return false;
        }
        // Se non ci sono risultati o in caso di eccezione, restituisci false
        return false;
    }

    public boolean userExists(String codice) {
        String query = "SELECT * FROM user WHERE codice = ?";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, codice);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean checkFondi(int id , double costo){
        String query = "SELECT fondi FROM user WHERE ID = ?";
        int fondi = 0;

        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    fondi = rs.getInt("fondi");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (fondi >= costo) {
            return true;
        } else {
            return false;
        }

    }

    public double checkCosto(int id){
        List<stampa> coda = getCoda(id);
        double costo = 0;
        double costoFoglio = 0.1;
        double colorato = 2;
        for (int i = 0; i < coda.size(); i++) {
            if (coda.get(i).colorata == true) {
                if(coda.get(i).retro != null){
                    costo += colorato * 2;
                } else {
                costo += colorato;
                }
            } else {
                costo += costoFoglio;
            }
        }
        return costo;
    }

    public boolean stampa(int id) {
        if(checkFondi(id, checkCosto(id))){
            String update = "UPDATE stampa JOIN log_stampante ON log_stampante.ID_stampa = stampa.ID SET stato = 'stampata' WHERE ID_user = ?";
            try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                    PreparedStatement stmt = conn.prepareStatement(update)) {
                // Imposta i parametri della query
                stmt.setInt(1, id);
    
                // Esegui la query
                int result = stmt.executeUpdate();
                return result == 1;
            } catch (SQLException e) {
                e.printStackTrace();
                // In caso di eccezione, restituisci false
                return false;
            }
    
        }
        return false;

        
    }

    public boolean updateFondi(String codice, int costo){
        String update = "UPDATE user SET fondi = fondi + ? WHERE codice = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(update)) {
            // Imposta i parametri della query
            stmt.setDouble(1, costo);
            stmt.setString(2, codice);

            // Esegui la query
            int result = stmt.executeUpdate();
            return result == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            // In caso di eccezione, restituisci false
            return false;
        }
    }

    public Utente cercaUtente(String codice){
        String query = "SELECT * FROM user WHERE codice = ?";
        Utente utente = null;

        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, codice);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    utente = new Utente(rs.getString("nome"), rs.getString("cognome"), rs.getString("ID"), rs.getString("codice"), rs.getString("ruolo"), rs.getInt("fondi"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return utente;
    }
}
