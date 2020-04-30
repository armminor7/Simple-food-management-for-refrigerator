package Model;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit.*;

import static java.time.temporal.ChronoUnit.DAYS;

public class Food {
    private String name;
    private String type;
    private String unit;
    private LocalDate expireDate;
    private LocalDate storedDate;
    private String imagePath;
    private boolean frozen;
    private boolean expire;
    private int amount;
    private DateTimeFormatter dateFormatter;

    public Food(String name, String type, String unit, int amount, LocalDate expireDate, boolean isFrozen) {
        this.name = name;
        this.type = type;
        this.unit = unit;
        this.amount = amount;
        this.storedDate = LocalDate.now();
        this.expireDate = expireDate;
        this.frozen = isFrozen;
        this.imagePath= "";
        dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getUnit() {
        return unit;
    }

    public int getAmount(){ return amount;}

//    public void addAmount(int num) {
//        this.amount += num;
//    }

    public void setAmount(int num){
        this.amount = num;
    }

    public String getExpireDate() {
        return expireDate.format(dateFormatter);
    }

    public String getStoredDate() { return storedDate.format(dateFormatter);}

    public boolean isFrozen() {
        return frozen;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


    public int getDayInRefrigerator() {
        int day = (int) DAYS.between(storedDate, LocalDate.now()) + 1;
        return Math.max(day, 0);
    }

    public int getShelfLife(){
        return (int)DAYS.between(LocalDate.now(), expireDate);
    }

    public boolean isExpire(){
        return (getShelfLife() <= 0);
    }

    public String toString(){
        return name + "," + type + "," + unit + "," + amount + "," + getExpireDate() + "," + getStoredDate() + "," + frozen + "," + imagePath;
    }

    // For testing
    public void setStoredDate(LocalDate date){
        this.storedDate = date;
    }
}
