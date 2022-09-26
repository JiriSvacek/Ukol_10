import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemMethods implements GoodsMethods {

    private final String url;
    private final String user;
    private final String password;

    public ItemMethods(String url, String user, String password) throws ItemMethodsExceptions {
        this.url = url;
        this.user = user;
        this.password = password;
        try (Connection ignored = DriverManager.getConnection(url, user, password)) {
            System.out.println("Login data are OK");
        } catch (SQLException e) {
            throw new ItemMethodsExceptions("Connection to SQL was not successful: " + e.getLocalizedMessage());
        }
    }


    @Override
    public Item loadItemById(Integer id) throws ItemMethodsExceptions {
        try (Connection con = connect()) {
            Statement statement = con.createStatement();
            statement.executeQuery("SELECT * FROM items WHERE iditems = " + id);
            ResultSet result = statement.getResultSet();
            if(result.next()) {
                return createItem(result);
            } else {
                System.out.print("Item not found, ");
                return null;
            }
        }
        catch (SQLException e) {
            throw new ItemMethodsExceptions("Error when loading Item: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void deleteAllOutOfStockItems() throws ItemMethodsExceptions {
        String errorMsgDeleteOutOfStock = "Error when deleting items out of stock: ";
        String insertData = "DELETE FROM items WHERE numberInStock = 0";
        updateTable(insertData, errorMsgDeleteOutOfStock);
    }

    @Override
    public List<Item> loadAllAvailableItems() throws ItemMethodsExceptions {
        List<Item> listOfItems = new ArrayList<>();
        try (Connection con = connect()) {
            Statement statement = con.createStatement();
            statement.executeQuery("SELECT * FROM items WHERE numberInStock > 0;");
            ResultSet result = statement.getResultSet();
            while (result.next()) {
                listOfItems.add(createItem(result));
            }
            return listOfItems;
        }
        catch (SQLException e) {
            throw new ItemMethodsExceptions("Error when loading list of Items: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void saveItem(Item item) throws ItemMethodsExceptions {
        String insertData = "INSERT INTO items("
                + "partNo, serialNo, name, description, numberInStock, price) VALUES "
                + "('" + item.getPartNo() + "', '" + item.getSerialNo()
                + "', '" + item.getName() + "', '" + item.getDescription() + "', "
                +item.getNumberInStock() + ", " + item.getPrice() + ")";

        try (Connection con = connect()) {
            Statement statement = con.createStatement();
            int rows = statement.executeUpdate(insertData, Statement.RETURN_GENERATED_KEYS);
            if (rows == 0) {
                throw new ItemMethodsExceptions("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    item.setId(generatedKeys.getInt(1));}
                else {
                    throw new ItemMethodsExceptions("Creating an item failed, no ID obtained.");
                }
            }
        }
        catch (SQLException e) {
            throw new ItemMethodsExceptions("Error when saving the item: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void updatePrice(Integer id, BigDecimal newPrice) throws ItemMethodsExceptions {
        String insertData = "UPDATE items SET price = " + newPrice + "WHERE iditems = " + id;
        String errorMsgUpdatePrice = "Error when updating price: ";
        updateTable(insertData, errorMsgUpdatePrice);
    }

    private Item createItem(ResultSet result) throws SQLException {
        Item item = new Item(
                result.getString("partNo"),
                result.getString("serialNo"),
                result.getString("name"),
                result.getString("description"),
                result.getInt("numberInStock"),
                result.getBigDecimal("price"));
        item.setId(result.getInt("idItems"));
        return item;
    }

    private Connection connect() throws SQLException {
        return  DriverManager.getConnection(url, user, password);
    }

    private void updateTable(String sqlCommand, String errorMessage) throws ItemMethodsExceptions {
        try (Connection con = connect()) {
            Statement statement = con.createStatement();
            statement.executeUpdate(sqlCommand);
        }
        catch (SQLException e) {
            throw new ItemMethodsExceptions(errorMessage + e.getLocalizedMessage());
        }
    }
}
