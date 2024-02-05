package com.toolrental.factory;

import com.toolrental.exception.RentalException;
import com.toolrental.exception.InventoryException;
import com.toolrental.model.Tool;
import com.toolrental.model.RentalItem;
import com.toolrental.holiday.HolidayCalendar;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RentalManager implements IRentalManager {
    private HashMap<String, RentalItem> rentalItemRepository;
    private HolidayCalendar calendar = HolidayCalendar.getInstance();
    private InventoryManager inventoryManager = InventoryManager.getInstance();

    private static Logger LOGGER = Logger.getLogger("Rental");

    private static RentalManager instance;


    // Static method to return the singleton instance
    public static RentalManager getInstance() {
        if (instance == null) {
            synchronized (RentalManager.class) {
                if (instance == null) {
                    instance = new RentalManager();
                }
            }
        }
        return instance;
    }
    public RentalManager() {
        rentalItemRepository = new HashMap<String, RentalItem>();
    }



    public String rentTool(String toolCode, LocalDate startDate, int numberOfDays, int discount) {
        String methodName = "rentTool " + toolCode + " " + startDate + " " + numberOfDays + " " + discount;
        if(LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("Start " + methodName);
        }
        if(numberOfDays<1) {
            throw new RentalException("Number of days have to be 1 or more");
        }
        if(discount>100 || discount < 0) {
            throw new RentalException("Discount percentage have to be between 0 to 100");
        }
        if(startDate.isBefore(LocalDate.now())) {
            throw new RentalException("Start date can not be before today");
        }

        Tool selectedTool = inventoryManager.getTool(toolCode);
        if (selectedTool !=null) {
            RentalItem rentalItem = new RentalItem(selectedTool, startDate, numberOfDays, discount);
            LocalDate dueDate = startDate.plusDays(numberOfDays);
            rentalItem.setDueDate(dueDate);
            rentalItem.setChargeableDays(calculateChargeableDays(rentalItem));
            System.out.println("Tool rented: " + selectedTool.getCode() + " x " + numberOfDays);
            // If InventoryManager throws an exception, it can be handled here and more
            // meaningful exception can be shown to the end user
            if(inventoryManager.checkOutTool(selectedTool)) {
                addRentalToRepository(rentalItem);
                if(LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info("End " + methodName);
                }
                return rentalItem.generateRentalAgreement();
            } else {

                LOGGER.severe(methodName + "Tool can not be checked out, please try again later");
                throw new InventoryException("Tool can not be checked out, please try again later");
            }
        } else {
                LOGGER.severe(methodName + "Tool not found, please check the code");
                throw new InventoryException("Tool not found, please check the code");
                // throw an exception
        }
    }
    // Throw an exception if rental cannot be found in the repository
    // Various validations can be done here like making sure rental hasn't already started
    public boolean cancelRental(String rentalID) {
        String methodName = "cancelRental " + rentalID;
        if(LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("Start " + methodName);
        }
        RentalItem rentalItem = rentalItemRepository.get(rentalID);
        //here, notify inventory manager of the tool status
        //here, update rental item status
        if(LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("End " + methodName);
        }
        return true;
    }

    // Throw an exception if rental cannot be found in the repository
    // Recalculate the numbers based on the current date and show rental summary
    // various validations can be done here, if its already past due display appropriate message
    // or ask user to extend before returning
    public String returnTool(String rentalID) {
        String methodName = "returnTool " + rentalID;
        if(LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("Start " + methodName);
        }
        RentalItem rentalItem = rentalItemRepository.get(rentalID);
        LocalDate newDueDate = LocalDate.now();
        rentalItem.setDueDate(newDueDate);
        //calculate the new numbers based on the current return date
        rentalItem.setChargeableDays(calculateChargeableDays(rentalItem));
        //set number of calendar days on rental item here
        int newNumberOfDays = (int) ChronoUnit.DAYS.between(rentalItem.getStartDate(), newDueDate);
        rentalItem.setNumberOfDays(newNumberOfDays);
        //here, notify inventory manager of the tool status
        //here, update rental item status
        if(LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("End " + methodName);
        }
        return rentalItem.generateRentalAgreement();
    }

    // Throw an exception if rental cannot be found in the repository
    // Recalculate the numbers based on the new due date and show rental summary
    // various validations can be done here, like ensure newDueDate is after the current due date etc.
    public String extendRental(String rentalID, LocalDate newDueDate) {
        String methodName = "extendRental " + rentalID;
        if(LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("Start " + methodName);
        }
        RentalItem rentalItem = rentalItemRepository.get(rentalID);
        //set number of calendar days on rental item here
        int newNumberOfDays = (int) ChronoUnit.DAYS.between(rentalItem.getStartDate(), newDueDate);
        rentalItem.setNumberOfDays(newNumberOfDays);
        rentalItem.setDueDate(newDueDate);
        rentalItem.setChargeableDays(calculateChargeableDays(rentalItem));
        //here, notify inventory manager of the tool status
        //here, update rental item status
        if(LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("End " + methodName);
        }
        return rentalItem.generateRentalAgreement();
    }

    private int calculateChargeableDays(RentalItem rentalItem) {
        return calculateChargeableDays(rentalItem, rentalItem.getDueDate());
    }

    private int calculateChargeableDays(RentalItem rentalItem, LocalDate returnDate) {
        int chargeableDays = 0;
        for(LocalDate aDate = rentalItem.getStartDate(); aDate.isBefore(returnDate) ; aDate = aDate.plusDays(1)) {
            if(calendar.isHoliday(aDate)) {
                //holiday, but only if holiday change is true then add the fee
                if(rentalItem.getTool().getToolType().isHolidayCharge()) {
                    chargeableDays++;
                }
            } else if(calendar.isWeekend(aDate)){
                //weekend, but only if weekend change is true then add the fee
                if(rentalItem.getTool().getToolType().isWeekendCharge()) {
                    chargeableDays++;
                }
            } else {
                //weekday, only if weekday charge is true then add the fee
                if(rentalItem.getTool().getToolType().isWeekdayCharge()) {
                    chargeableDays++;
                }
            }
        }
        rentalItem.setChargeableDays(chargeableDays);
        return chargeableDays;
    }


    // In reality, this method will store the rental in the DB
    // Appropriate exceptions should be caught and meaningful messages should be sent back
    private void addRentalToRepository(RentalItem rentalItem) {
        try{
            rentalItem.setRentalId(UUID.randomUUID().toString());
            this.rentalItemRepository.put(rentalItem.getRentalId(), rentalItem);
        } catch (Exception e) {
            throw new RentalException("Not able to save the rental, please try again later");
        }
        if(LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("End " + " addRentalToRepository " + rentalItem.getRentalId());
        }
    }

}