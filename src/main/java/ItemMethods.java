import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemMethods implements GoodsMethods {

    private Statement statement;
    private String url;
    private String user;
    private String password;

    public ItemMethods(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }


    @Override
    public Item loadItemById(Integer id) {
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            Statement statement = con.createStatement();
            statement.executeQuery("SELECT * FROM items WHERE iditems = " + id);
            ResultSet result = statement.getResultSet();
            if(result.next()) {
                return createItem(result);
            } else {
                System.out.println("Item not found");
                return null;
            }
        }
        catch (SQLException e) {
            System.out.println("Error when loading Item: " + e);
            return null;
        }
    }

    @Override
    public void deleteAllOutOfStockItems() {
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            Statement statement = con.createStatement();
            statement.executeUpdate("DELETE FROM items WHERE numberInStock = 0;");
        }
        catch (SQLException e) {
            System.out.println("Error when deleting items out of stock: " + e);
        }
    }

    @Override
    public List<Item> loadAllAvailableItems() {
        List<Item> listOfItems = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            Statement statement = con.createStatement();
            statement.executeQuery("SELECT * FROM items WHERE numberInStock > 0;");
            ResultSet result = statement.getResultSet();
            while (result.next()) {
                listOfItems.add(createItem(result));
            }
            return listOfItems;
        }
        catch (SQLException e) {
            System.out.println("Error when loading list of Items: " + e);
            return null;
        }
    }

    @Override
    public void saveItem(Item item) {
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            Statement statement = con.createStatement();
            String insertData = "INSERT INTO items("
                    + "iditems, partNo, serialNo, name, description, numberInStock, price) VALUES "
                    + "( " + item.getId() + ", '" + item.getPartNo() + "', '" + item.getSerialNo()
                    + "', '" + item.getName() + "', '" + item.getDescription() + "', "
                    +item.getNumberInStock() + ", " + item.getPrice() + ")";
            statement.executeUpdate(insertData);
        }
        catch (SQLException e) {
            System.out.println("Error when saving the Items: " + e);
        }
    }

    @Override
    public void updatePrice(Integer id, BigDecimal newPrice) {
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            Statement statement = con.createStatement();
            String insertData = "UPDATE items SET price = " + newPrice + "WHERE iditems = " + id;
            statement.executeUpdate(insertData);
        }
        catch (SQLException e) {
            System.out.println("Error when updating price: " + e);
        }
    }

    private Item createItem(ResultSet result) throws SQLException {
        return new Item(result.getInt("idItems"),
                result.getString("partNo"),
                result.getString("serialNo"),
                result.getString("name"),
                result.getString("description"),
                result.getInt("numberInStock"),
                result.getBigDecimal("price"));
    }
}