package com.toolrental.model;

import com.toolrental.utils.FormatUtilities;

import java.time.LocalDate;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RentalItem implements IRentalAgreement {


    private String rentalId;
    private Tool tool;
    private int quantity;
    private int numberOfDays;
    private int chargeableDays;
    private LocalDate startDate;
    private LocalDate dueDate;
    private double totalAmount;
    private int discountPercentage;
    private RentalItemStatus status;

    public String getRentalId() {
        return rentalId;
    }

    public void setRentalId(String rentalId) {
        this.rentalId = rentalId;
    }

    public int getChargeableDays() {
        return chargeableDays;
    }

    public void setChargeableDays(int chargeableDays) {
        this.chargeableDays = chargeableDays;
    }

    public double getDiscountAmount() {
        var preDiscountAmount = getPreDiscountAmount();
        if(getDiscountPercentage()!=0) {
            return (preDiscountAmount*getDiscountPercentage())/100;
        }
        else {
            return 0;
        }
    }


    public RentalItemStatus getStatus() {
        return status;
    }

    public void setStatus(RentalItemStatus status) {
        this.status = status;
    }

    public RentalItem(Tool tool, LocalDate startDate, int numberOfDays, int discount) {
        this.tool = tool;
        this.startDate = startDate;
        this.numberOfDays = numberOfDays;
        rentalId = UUID.randomUUID().toString();
        discountPercentage = discount;
        quantity = 1;
    }

    public Tool getTool() {
        return tool;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }


    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate aDate) {
        this.dueDate = aDate;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public double getPreDiscountAmount() {
        return chargeableDays * getDailyPrice();
    }

    public double getPayableAmount() {
        //subtract the discount
        return getPreDiscountAmount() - getDiscountAmount();
    }

    public double getDailyPrice() {
        return tool.getRentalPrice() * quantity;
    }

    @Override
    public String generateRentalAgreement() {
        return this.printRentalAgreement();
    }

    private String printRentalAgreement() {
        Logger logger = Logger.getLogger("Rental");
        String methodName = "printRentalAgreement " + getRentalId();
        if(logger.isLoggable(Level.INFO)) {
            logger.info("Start " + methodName);
        }
        StringBuilder aBuilder = new StringBuilder();
        aBuilder.append("Rental Agreement:");
        aBuilder.append("\n==============================================");
        aBuilder.append("\n\tRental Agreement ID: " + getRentalId());
        aBuilder.append("\n\tTool Code: " + getTool().getCode());
        aBuilder.append("\n\tTool Type: " + getTool().getToolType().getType());
        aBuilder.append("\n\tTool Brand: " + getTool().getBrand());
        aBuilder.append("\n\tRental days: "+ getNumberOfDays());
        aBuilder.append("\n\tCheckout date: "+ FormatUtilities.printDate(getStartDate()));
        aBuilder.append("\n\tDue date: "+ FormatUtilities.printDate(getDueDate()));
        aBuilder.append("\n\tDaily Charge: "+ FormatUtilities.printAmount(getDailyPrice()));
        aBuilder.append("\n\tChargeable days: "+ getChargeableDays());
        aBuilder.append("\n\tPreDiscount Charge: " + FormatUtilities.printAmount(getPreDiscountAmount()));
        aBuilder.append("\n\tDiscount percent: " + FormatUtilities.printPercent(getDiscountPercentage()));
//        aBuilder.append("\n\tDiscount percent: " + String.format("%d%%", rentalItem.getDiscountPercentage()));
        aBuilder.append("\n\tDiscount Amount: " + FormatUtilities.printAmount(getDiscountAmount()));
        aBuilder.append("\n\tFinal Amount: " + FormatUtilities.printAmount(getPayableAmount()));
        aBuilder.append("\n==============================================");
        if(logger.isLoggable(Level.INFO)) {
            logger.info(aBuilder.toString());
            logger.info("End " + methodName);
        }
        return aBuilder.toString();
    }}