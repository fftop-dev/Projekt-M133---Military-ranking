package ch.bzz.militaryranking.data;

import ch.bzz.militaryranking.model.Vehicle;
import ch.bzz.militaryranking.model.Country;
import ch.bzz.militaryranking.model.Weapon;
import ch.bzz.militaryranking.service.Config;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * reads and writes the data in the JSON-files
 */
public class DataHandler {
    private static DataHandler instance = null;
    private static List<Country> countryList;
    private static List<Vehicle> vehicleList;
    private static List<Weapon> weaponList;

    /**
     * private constructor defeats instantiation
     */
    private DataHandler() {}

    /**
     * reads all countries
     * @return list of countries
     */
    public static List<Country> readAllCountries() {
        return getCountryList();
    }
    /**
     * reads all vehicles
     * @return list of vehicles
     */
    public static List<Vehicle> readAllVehicles() {
        return getVehicleList();
    }
    /**
     * reads all weapons
     * @return list of weapons
     */
    public static List<Weapon> readAllWeapons() {
        return getWeaponList();
    }

    /**
     * reads a vehicle by its id
     * @param vehicleID
     * @return vehicle (null=not found)
     */
    public static Vehicle readVehicleByID(String vehicleID) {
        Vehicle vehicle = null;
        for (Vehicle entry : getVehicleList()) {
            if (entry.getVehicleID() == Integer.parseInt(vehicleID)) {
                vehicle = entry;
            }
        }
        return vehicle;
    }
    /**
     * reads a weapon by its id
     * @param weaponID
     * @return weapon (null=not found)
     */
    public static Weapon readWeaponByID(String weaponID) {
        Weapon weapon = null;
        for (Weapon entry : getWeaponList()) {
            if (entry.getWeaponID() == Integer.parseInt(weaponID)) {
                weapon = entry;
            }
        }
        return weapon;
    }

    /**
     * reads a country by its name
     * @param countryID
     * @return country (null=not found)
     */
    public static Country readCountryByID(String countryID) {
        Country country = null;
        for (Country entry : getCountryList()) {
            if (entry.getCountryID() == Integer.parseInt(countryID)) {
                country = entry;
            }
        }
        return country;
    }
    /**
     * writes the countryList to the JSON-file
     */
    private static void writeCountryJSON() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer(new DefaultPrettyPrinter());
        FileOutputStream fileOutputStream = null;
        Writer fileWriter;

        String countryPath = Config.getProperty("countryJSON");
        try {
            fileOutputStream = new FileOutputStream(countryPath);
            fileWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8));
            objectWriter.writeValue(fileWriter, getCountryList());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * reads the country from the JSON-file
     */
    private static void readCountryJSON() {
        try {
            byte[] jsonData = Files.readAllBytes(
                    Paths.get(
                            Config.getProperty("countryJSON")
                    )
            );
            ObjectMapper objectMapper = new ObjectMapper();
            Country[] countries = objectMapper.readValue(jsonData, Country[].class);
            for (Country country : countries) {
                getCountryList().add(country);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * writes the vehicleList to the JSON-file
     */
    private static void writeVehicleJSON() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer(new DefaultPrettyPrinter());
        FileOutputStream fileOutputStream = null;
        Writer fileWriter;

        String vehiclePath = Config.getProperty("vehicleJSON");
        try {
            fileOutputStream = new FileOutputStream(vehiclePath);
            fileWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8));
            objectWriter.writeValue(fileWriter, getVehicleList());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * reads the vehicle from the JSON-file
     */
    private static void readVehicleJSON() {
        try {
            byte[] jsonData = Files.readAllBytes(
                    Paths.get(
                            Config.getProperty("vehicleJSON")
                    )
            );
            ObjectMapper objectMapper = new ObjectMapper();
            Vehicle[] vehicles = objectMapper.readValue(jsonData, Vehicle[].class);
            for (Vehicle vehicle : vehicles) {
                getVehicleList().add(vehicle);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * writes the weaponList to the JSON-file
     */
    private static void writeWeaponJSON() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer(new DefaultPrettyPrinter());
        FileOutputStream fileOutputStream = null;
        Writer fileWriter;

        String weaponPath = Config.getProperty("weaponJSON");
        try {
            fileOutputStream = new FileOutputStream(weaponPath);
            fileWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8));
            objectWriter.writeValue(fileWriter, getWeaponList());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * reads the weapons from the JSON-file
     */
    private static void readWeaponJSON() {
        try {
                String path = Config.getProperty("weaponJSON");
            byte[] jsonData = Files.readAllBytes(
                    Paths.get(path)
            );
            ObjectMapper objectMapper = new ObjectMapper();
            Weapon[] weapons = objectMapper.readValue(jsonData, Weapon[].class);
            for (Weapon weapon : weapons) {
                getWeaponList().add(weapon);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * inserts a new country into the countryList
     *
     * @param country the country to be saved
     */
    public static void insertCountry(Country country) {
        getCountryList().add(country);
        writeCountryJSON();
    }

    /**
     * inserts a new vehicle into the vehicleList
     *
     * @param vehicle the vehicle to be saved
     */
    public static void insertVehicle(Vehicle vehicle) {
        getVehicleList().add(vehicle);
        writeVehicleJSON();
    }

    /**
     * inserts a new weapon into the weaponList
     *
     * @param weapon the weapon to be saved
     */
    public static void insertWeapon(Weapon weapon) {
        getWeaponList().add(weapon);
        writeWeaponJSON();
    }

    /**
     * deletes a vehicle identified by the vehicleID
     * @param vehicleID  the key
     * @return  success=true/false
     */
    public static boolean deleteVehicle(String vehicleID) {
        Vehicle vehicle = readVehicleByID(vehicleID);
        if (vehicle != null) {
            getVehicleList().remove(vehicle);
            writeVehicleJSON();
            return true;
        } else {
            return false;
        }
    }

    /**
     * deletes a weapon identified by the weaponID
     * @param weaponID  the key
     * @return  success=true/false
     */
    public static boolean deleteWeapon(String weaponID) {
        Weapon weapon = readWeaponByID(weaponID);
        if (weapon != null) {
            getWeaponList().remove(weapon);
            writeWeaponJSON();
            return true;
        } else {
            return false;
        }
    }

    /**
     * deletes a country identified by the countryID
     * @param countryID  the key
     * @return  success=true/false
     */
    public static boolean deleteCountry(String countryID) {
        Country country = readCountryByID(countryID);
        if (country != null) {
            getCountryList().remove(country);
            writeCountryJSON();
            return true;
        } else {
            return false;
        }
    }



    /**
     * updates the vehicleList
     */
    public static void updateVehicle() {
        writeVehicleJSON();
    }

    /**
     * updates the weaponList
     */
    public static void updateWeapon() {
        writeWeaponJSON();
    }

    /**
     * updates the countryList
     */
    public static void updateCountry() {
        writeVehicleJSON();
    }


    /**
     * gets country list
     *
     * @return value of country
     */
    private static List<Country> getCountryList(){
        if (countryList == null){
            setCountryLists(new ArrayList<>());
            readCountryJSON();
        }
        return countryList;
    }

    /**
     * sets country list
     *
     * @param countryList the value to set
     */
    private static void setCountryLists(List<Country> countryList) {
        DataHandler.countryList = countryList;
    }

    /**
     * gets vehicle list
     *
     * @return value of vehicle
     */
    private static List<Vehicle> getVehicleList(){
        if (vehicleList == null){
            setVehicleList(new ArrayList<>());
            readVehicleJSON();
        }
        return vehicleList;
    }

    /**
     * sets vehicle list
     *
     * @param vehicleList the value to set
     */
    private static void setVehicleList(List<Vehicle> vehicleList) {
        DataHandler.vehicleList = vehicleList;
    }

    /**
     * gets weapon list
     *
     * @return value of weapon
     */
    private static List<Weapon> getWeaponList(){
        if (weaponList == null){
            setWeaponList(new ArrayList<>());
            readWeaponJSON();
        }
        return weaponList;
    }

    /**
     * sets weapon list
     *
     * @param weaponList the value to set
     */
    private static void setWeaponList(List<Weapon> weaponList) {
        DataHandler.weaponList = weaponList;
    }

    /**
     * gets weapon count
     */
    public static int getWeaponCount(){
        int maxID = 0;
        for (int i = 0; i < DataHandler.getWeaponList().size(); i++){
            if (maxID < DataHandler.getWeaponList().get(i).getWeaponID()){
                maxID = DataHandler.getWeaponList().get(i).getWeaponID();
            }
        }
        return maxID;
    }

    /**
     * gets vehicle count
     */
    public static int getVehicleCount(){
        int maxID = 0;
        for (int i = 0; i < DataHandler.getVehicleList().size(); i++){
            if (maxID < DataHandler.getVehicleList().get(i).getVehicleID()){
                maxID = DataHandler.getVehicleList().get(i).getVehicleID();
            }
        }
        return maxID;
    }

    /**
     * gets country count
     */
    public static int getCountryCount(){
        int maxID = 0;
        for (int i = 0; i < DataHandler.getCountryList().size(); i++){
            if (maxID < DataHandler.getCountryList().get(i).getCountryID()){
                maxID = DataHandler.getCountryList().get(i).getCountryID();
            }
        }
        return maxID;
    }
}