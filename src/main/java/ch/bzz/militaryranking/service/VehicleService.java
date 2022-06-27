package ch.bzz.militaryranking.service;

import ch.bzz.militaryranking.data.DataHandler;
import ch.bzz.militaryranking.model.Country;
import ch.bzz.militaryranking.model.Vehicle;
import ch.bzz.militaryranking.model.Weapon;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.crypto.Data;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

/**
 * vehicle service
 */
@Path("vehicle")
public class VehicleService {

    private static int cntr = DataHandler.getVehicleCount();
    /**
     * lists all vehicles from vehicles.json
     */
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listVehicle(){
        List<Vehicle> vehicleList = DataHandler.readAllVehicles();
        return Response
                .status(200)
                .entity(vehicleList)
                .build();
    }

    /**
     * lists the corresponding vehicle to given id
     */
    @GET
    @Path("read")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readVehicle(
            @QueryParam("vehicleID") String vehicleID
    ){
        Vehicle vehicle = DataHandler.readVehicleByID(vehicleID);
        int httpStatus;
        if (vehicle == null){
            httpStatus = 404;
        }
        else{
            httpStatus = 200;
        }
        return Response
                .status(httpStatus)
                .entity(vehicle)
                .build();
    }

    /**
     * sorts the vehicle list by vehicleName or battlePoints or quantity
     */
    @GET
    @Path("sortList")
    @Produces(MediaType.APPLICATION_JSON)
    public Response sortWeapon(
            @QueryParam("sortBy") String sort
    ){
        List<Vehicle> vehicleList = DataHandler.readAllVehicles();
        if(sort != null && sort.equals("vehicleName")){
            Collections.sort(vehicleList, new Comparator<Vehicle>() {
                @Override
                public int compare(Vehicle vehicle, Vehicle t1) {
                    return vehicle.getVehicleName().compareTo(t1.getVehicleName());
                }
            });
        }
        else if (sort!= null && sort.equals("battlepoints")){
            Collections.sort(vehicleList, new Comparator<Vehicle>() {
                @Override
                public int compare(Vehicle vehicle, Vehicle t1) {
                    return vehicle.getBattlepoints()-(t1.getBattlepoints());
                }
            });
        }
        else if (sort!= null && sort.equals("quantity")){
            Collections.sort(vehicleList, new Comparator<Vehicle>() {
                @Override
                public int compare(Vehicle vehicle, Vehicle t1) {
                    return vehicle.getQuantity()-(t1.getQuantity());
                }
            });
        }
        if (sort.contains("vehicleName") || sort.contains("battlepoints") || sort.contains("quantity")){
            return Response
                    .status(200)
                    .entity(vehicleList)
                    .build();
        }
        else{
            return Response
                    .status(404)
                    .entity(null)
                    .build();
        }
    }

    /**
     * Inserts a new vehicle
     * @param vehicle the vehicle
     * @return Response
     */
    @POST
    @Path("create")
    @Produces(MediaType.TEXT_PLAIN)
    public Response insertVehicle(
            @Valid @BeanParam Vehicle vehicle
    ){
        vehicle.setVehicleID(++cntr);
        int httpStatus = 200;
        if (getWeaponsFromID(vehicle) != null){
            vehicle.setWeapons(getWeaponsFromID(vehicle));
            DataHandler.insertVehicle(vehicle);
        }
        else{
            httpStatus = 400;
        }

        return Response
                .status(httpStatus)
                .entity("")
                .build();
    }

    /**
     * deletes a vehicle identified by its id
     * @param vehicleID the key
     * @return Response
     */
    @DELETE
    @Path("delete")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteVehicle(
            @QueryParam("vehicleID") String vehicleID
    ){
        int httpStatus = 200;
        if (!DataHandler.deleteVehicle(vehicleID)){
            httpStatus = 410;
        }
        DataHandler.updateCountry();
        return Response
                .status(httpStatus)
                .entity("")
                .build();
    }

    /**
     * updates a new vehicle
     * @param vehicle the vehicle
     * @return Response
     */
    @POST
    @Path("update")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateVehicle(
            @Valid @BeanParam Vehicle vehicle
    ){
        int httpStatus = 200;
        Vehicle oldVehicle = DataHandler.readVehicleByID(Integer.toString(vehicle.getVehicleID()));
        if (oldVehicle != null && getWeaponsFromID(vehicle) != null){
            oldVehicle.setVehicleName(vehicle.getVehicleName());
            oldVehicle.setQuantity(vehicle.getQuantity());
            oldVehicle.setBattlepoints(vehicle.getBattlepoints());
            oldVehicle.setRegistrationDate(vehicle.getRegistrationDate());
            oldVehicle.setWeapons(getWeaponsFromID(vehicle));
            DataHandler.updateVehicle();
        }
        else if (oldVehicle == null){
            httpStatus = 404;
        }
        else{
            httpStatus = 400;
        }
        return Response
                .status(httpStatus)
                .entity("")
                .build();
    }

    /**
     * return list of weapon object from weaponIds
     * @param vehicle
     * @return
     */
    public Vector<Weapon> getWeaponsFromID(Vehicle vehicle){
        String[] weaponIDs = vehicle.getWeaponIDs().split("\\s+");
        Vector<Weapon> weapons = new Vector<Weapon>();
        for (int i = 0; i < weaponIDs.length; i++){
            if (DataHandler.readWeaponByID(weaponIDs[i]) == null){
                return null;
            }
            Weapon weapon = new Weapon();
            weapon.setWeaponID(Integer.parseInt(weaponIDs[i]));
            weapons.add(weapon);
        }
        return weapons;
    }

}
