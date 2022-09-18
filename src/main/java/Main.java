import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {
        ItemMethods items = new ItemMethods("jdbc:mysql://localhost:3306/ukol_10",
                "ukol_10",
                "ukol_10FTW");

        Item tyc = new Item(1, "01", "0001", "Bar",
                "straight bar", 5, BigDecimal.valueOf(125.5));
        items.saveItem(tyc);
        Item netyc = new Item(2, "02", "0002", "NoBar",
                "smt", 0, BigDecimal.valueOf(12.5));
        System.out.println("Load list: " + items.loadAllAvailableItems());
        items.saveItem(netyc);
        System.out.println("Load list: " + items.loadAllAvailableItems());
        System.out.println("Load id = 2: " + items.loadItemById(2));
        items.deleteAllOutOfStockItems();
        System.out.println("Load id = 2: " + items.loadItemById(2));
        items.updatePrice(1, BigDecimal.valueOf(500.5));
        System.out.println("Load id = 1: " + items.loadItemById(1));
    }
}