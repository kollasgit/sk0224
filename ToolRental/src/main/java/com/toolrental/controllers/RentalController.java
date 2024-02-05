package com.toolrental.controllers;

import com.toolrental.factory.InventoryManager;
import com.toolrental.factory.RentalManager;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/tools")
public class RentalController {

    /*
    POST
    http://localhost:8080/api/tools/rent?toolCode=JAKD&date=2024-02-02&numberOfDays=10&discount=10

    Or just send the code, defaults will be applied
    POST
    http://localhost:8080/api/tools/rent?toolCode=CHNS

     */
    @PostMapping("/rent")
    public String rentTool(@RequestParam String toolCode
            , @RequestParam(required = false,defaultValue = "#{T(java.time.LocalDate).now()}") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
            , @RequestParam(required = false,defaultValue = "1") int numberOfDays
            , @RequestParam(required = false,defaultValue = "0") int discount) {

        return RentalManager.getInstance().rentTool(toolCode,date,numberOfDays, discount);
    }


    /*
    GET
    http://localhost:8080/api/tools/display
     */
    @GetMapping("/display")
    public String displayToolList() {
        return InventoryManager.getInstance().displayToolList();
    }

    /*
    POST
    http://localhost:8080/api/tools/cancel?rentalID=<rental id returned in the checkout summary>
     */
    @PostMapping("/cancel")
    public boolean cancelRental(@RequestParam String rentalID) {
        return RentalManager.getInstance().cancelRental(rentalID);
    }

    /*
    POST
    http://localhost:8080/api/tools/extend?rentalID=<rental id returned in the checkout summary>&date=<new due date>
     */
    @PostMapping("/extend")
    public String extendRental(@RequestParam String rentalID
            , @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        return RentalManager.getInstance().extendRental(rentalID,date);
    }

    /*
    POST
    http://localhost:8080/api/tools/return?rentalID=<rental id returned in the checkout summary>
     */
    @PostMapping("/return")
    public String returnTool(@RequestParam String rentalID) {
        return RentalManager.getInstance().returnTool(rentalID);
    }
}
