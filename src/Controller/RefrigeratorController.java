package Controller;

import Model.Food;
import Util.FileController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class RefrigeratorController {
    private Map<String, List<Food>> foods;
    private FileController foodFile;
    private String activeUser;

    public RefrigeratorController(String username) {
        this.activeUser = username;
        this.foods = new HashMap<>();
        try {
            foodFile = new FileController("FreezerApp", activeUser+"_"+"refrigerator.csv");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Can't load refrigerator.csv");
        }
    }

    public void loadFoodData() {
        if (foodFile.isExistData()) {
            String[] lines = foodFile.getContent().split(System.lineSeparator());
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for (String line : lines) {
                String[] data = line.split(",");
                String cell = data[0];
                Food food = new Food(
                        data[1],
                        data[2],
                        data[3],
                        Integer.parseInt(data[4]),
                        LocalDate.parse(data[5], dateFormat),
                        Boolean.parseBoolean(data[7]));
                food.setStoredDate(LocalDate.parse(data[6], dateFormat));
                food.setImagePath(data[8]);
                addFoodTo(cell, food);
            }
        }

    }

    public void addFoodTo(String cell, Food newFood) {
        List<Food> container = foods.get(cell);
        if (container == null) {
            container = new ArrayList<>();
            foods.put(cell, container);
            container.add(newFood);
        } else {
            for (Food existFood : container) {
                if (
                        existFood.getName().equals(newFood.getName()) &&
                                existFood.getType().equals(newFood.getType()) &&
                                existFood.getUnit().equals(newFood.getUnit()) &&
                                existFood.getExpireDate().equals(newFood.getExpireDate()) &&
                                existFood.getStoredDate().equals(newFood.getStoredDate()) &&
                                existFood.isFrozen() == newFood.isFrozen()) {
                    newFood.setAmount(newFood.getAmount() + existFood.getAmount());
                    updateFoodData(cell+","+existFood.toString(), cell+","+newFood.toString());
                    container.remove(existFood);
                    break;
                }
            }
            container.add(newFood);

        }
        container.sort(new SortExpireDate());
    }

    public void getFoodOut(String cell) {
        List<Food> container = foods.get(cell);
        container.remove(0);
    }

    public void saveFoodData(String selectedCell, Food food) {
        foodFile.appendWithNewLine(selectedCell + "," + food.toString());
        try {
            foodFile.save();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Can't save food data");
        }
    }

    public void deleteFoodData(String selectedCell, Food food) {
        String deleteStr = selectedCell + "," + food.toString();
        foodFile.delete(deleteStr);
        try {
            foodFile.save();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Can't delete food data");
        }

    }

    public void updateFoodData(String oldStr, String newStr) {
        foodFile.update(oldStr, newStr);
        try {
            foodFile.save();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Can't update food data");
        }
    }

    public List<Food> getFoodListFrom(String cell) {
        if (foods.isEmpty() || !foods.containsKey(cell)) return new ArrayList<>();
        return foods.get(cell);
    }

    public String foodNameListToStringFrom(String cell) {
        if (foods.isEmpty() || !foods.containsKey(cell)) return null;
        List<Food> foodList = foods.get(cell);
        List<String> foodNameList = new ArrayList<>();
        for (Food food : foodList) {
            foodNameList.add(food.getName());
        }
        return String.join(",", foodNameList);
    }

    public Set<Map.Entry<String, List<Food>>> getEntrySet() {
        return foods.entrySet();
    }

    private static class SortExpireDate implements Comparator<Food> {
        @Override
        public int compare(Food o1, Food o2) {
            if (o1.getShelfLife() - o2.getShelfLife() == 0)
                return o2.getDayInRefrigerator() - o1.getDayInRefrigerator();
            else return o1.getShelfLife() - o2.getShelfLife();
        }
    }

}
