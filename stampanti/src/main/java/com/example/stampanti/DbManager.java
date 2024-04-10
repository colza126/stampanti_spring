package com.example.stampanti;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                    boolean permessi_admin;
                    if (rs.getString("ruolo") == "admin") {
                        permessi_admin = true;
                    } else {
                        permessi_admin = false;
                    }

                    return new MySession(rs.getInt("ID"), permessi_admin);

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


    public List<stampa> getCoda(){
        List<stampa> risultati = new ArrayList<>();
        String query = "SELECT * FROM stampa JOIN log_stampante ON log_stampante.ID_stampa = stampa.ID WHERE stato = 'coda'";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(query)) {
            // Esegui la query


            try (ResultSet rs = stmt.executeQuery()) {
                // Controlla se ci sono risultati
                while (rs.next()) {
                    // Se ci sono risultati, restituisci true
                    risultati.add(new stampa(rs.getString("fronte"), rs.getString("retro"), rs.getString("ID"), rs.getBoolean("colorata")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // In caso di eccezione, restituisci false
            return null;
        }
        return risultati;
    }

    public boolean inserisci_coda(String fronte, String retro, boolean color) {
        String insert = "INSERT INTO stampa (fronte, retro, colorata) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(insert)) {
            // Imposta i parametri della query
            stmt.setString(1, fronte);
            stmt.setString(2, retro);
            stmt.setBoolean(3, color);

            // Esegui la query
            int result = stmt.executeUpdate();
            return result == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            // In caso di eccezione, restituisci false
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

    public boolean registerUser(String codice, String pass, String nome, String cognome, String email, String permessi) {
        String insert = "INSERT INTO user (codice, password, nome, cognome, email, ruolo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(insert)) {
            // Imposta i parametri della query
            stmt.setString(1, codice);
            stmt.setString(2, convertToMD5(pass));
            stmt.setString(3, nome);
            stmt.setString(4, cognome);
            stmt.setString(5, email);
            stmt.setString(6, permessi);

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

    public boolean stampa(String id) {
        String update = "UPDATE stampa JOIN log_stampante ON log_stampante.ID_stampa = stampa.ID SET stato = 'stampato' WHERE ID = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(update)) {
            // Imposta i parametri della query
            stmt.setString(1, id);

            // Esegui la query
            int result = stmt.executeUpdate();
            return result == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            // In caso di eccezione, restituisci false
            return false;
        }
    }

}
