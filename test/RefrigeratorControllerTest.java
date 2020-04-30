import Controller.RefrigeratorController;
import Model.Food;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

public class RefrigeratorControllerTest {
    RefrigeratorController container;
    @BeforeEach
    void setup(){
        container = new RefrigeratorController("test");
        container.addFoodTo("CELL00" ,new Food("Egg", "EGG", "Dozen", 1, LocalDate.of(2020, 3, 10), true));
        container.addFoodTo("CELL21" ,new Food("Chicken", "Meat", "kg", 1, LocalDate.of(2020, 3, 12), true));
        container.addFoodTo("CELL10" ,new Food("Milk", "Dairy", "ml", 1, LocalDate.of(2020, 3, 11), true));
        container.addFoodTo("CELL10" ,new Food("Ice", "Frozen", "Pack", 1, LocalDate.of(2020, 2, 5), true));
        container.addFoodTo("CELL10" ,new Food("Toy", "Other", "item", 1, LocalDate.of(2020, 4, 22), true));
    }
    @Test
    void sortedExpireDate() {
        assertEquals("Ice,Milk,Toy", container.foodNameListToStringFrom("CELL10"));
    }


}
