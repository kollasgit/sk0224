package com.toolrental.factory;

import java.time.LocalDate;

public interface IRentalManager {
    public String rentTool(String toolCode, LocalDate startDate, int numberOfDays, int discount);

    public boolean cancelRental(String rentalID);

    public String returnTool(String rentalID);

    public String extendRental(String rentalID, LocalDate newDueDate);

}
