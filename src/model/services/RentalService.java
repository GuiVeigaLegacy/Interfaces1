package model.services;

import model.entities.CarRental;
import model.entities.Invoice;

public class RentalService {
    private Double pricePerDay;
    private Double pricePerHour;

    private BrazilTaxService taxService;

    public RentalService(Double pricePerDay, Double pricePerHour, BrazilTaxService taxService) {
        this.pricePerDay = pricePerDay;
        this.pricePerHour = pricePerHour;
        this.taxService = taxService;
    }

    public void processInvoice(CarRental carRental) {
        long t1 = carRental.getStart().getTime();                   //  gets the first rental time in milliseconds
        long t2 = carRental.getFinish().getTime();                  //  gets the last rental time in milliseconds
        double hours = (double) (t2 - t1) / 1000 / 60 / 60;         //  difference converted in order this: seconds, minutes, days

        double basicPayment;
        if (hours <= 12.0) {
            basicPayment = Math.ceil(hours) * pricePerHour;
        } else {
            basicPayment = Math.ceil(hours / 24) * pricePerDay;
        }

        double tax = taxService.tax(basicPayment);                  //  Calculates the tax

        carRental.setInvoice(new Invoice(basicPayment, tax));
    }
}
