import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {
        try {
            ItemMethods items = new ItemMethods("jdbc:mysql://localhost:3306/ukol_10",
                    "ukol_10",
                    "ukol_10FTW");
            for (int i = 0 ; i <5; i++) {
                Item tyc = new Item("01", "0001", "Bar",
                        "straight bar", 5, BigDecimal.valueOf(125.5));
                items.saveItem(tyc);
            }
            Item netyc = new Item( "02", "0002", "NoBar",
                    "smt", 0, BigDecimal.valueOf(12.5));
            System.out.println("Load list: " + items.loadAllAvailableItems());
            items.saveItem(netyc);
            System.out.println("Load list: " + items.loadAllAvailableItems());
            System.out.println("Load: " + items.loadItemById(1));
            items.deleteAllOutOfStockItems();
            System.out.println("Load: " + items.loadItemById(11));
            items.updatePrice(1, BigDecimal.valueOf(500.5));
            System.out.println("Load: " + items.loadItemById(13));
        } catch (ItemMethodsExceptions e) {
            System.err.println("There is an error in database: " + e.getLocalizedMessage());
        }
    }
}