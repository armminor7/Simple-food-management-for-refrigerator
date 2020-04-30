import Model.Food;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class FoodTest {
    Food food, food1;

    @BeforeEach
    void setup(){
        food = new Food("Egg", "EGG", "pieces", 1, LocalDate.of(2020, 3, 10), false);
        food1 = new Food("Egg", "EGG", "pieces", 1, LocalDate.of(2020, 3 , 8), false);
    }

    @Test
    void testGetExpireDate(){
        assertEquals("29/03/2020", food.getExpireDate());
    }

    @Test
    void testGetDayInRefrigerator() {
        // tested date is 8/3/2020
        food.setStoredDate(LocalDate.of(2020, 3,1));
        assertEquals(8, food.getDayInRefrigerator());
    }

    @Test
    void testGetShelfLife() {
        // tested date is 8/3/2020
        // food is expire in 10/3/2020
        // food1 is expire in 8/3/2020
        assertEquals(2, food.getShelfLife());
        assertEquals(0, food1.getShelfLife());
    }

    @Test
    void testIsExpire(){
        Food expireFood = new Food ("Milk", "Diary", "L", 2, LocalDate.of(2020, 3,1), false);
        assertTrue(expireFood.isExpire());
        assertTrue(food1.isExpire());
        assertFalse(food.isExpire());
    }

    @Test
    void testToString(){
        assertEquals("Egg,EGG,pieces,1,10/03/2020,10/03/2020,false,",food.toString());
    }

}
