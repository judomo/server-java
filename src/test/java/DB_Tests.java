import DB.DB;
import DB.DB_API;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.sql.SQLException;
import java.util.Objects;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DB_Tests {

    public final static String dbName = "database.db";
    public final static String tableName = "Product";

    @Order(1)
    @Test
    void testCreate() throws SQLException {

        DB.connect();

        DB_API.create(tableName);

        assert (DB_API.checkIfExist(tableName));

        DB.close();

    }

    @Order(2)
    @Test
    void testInsertByName() throws SQLException {

        DB.connect();

        DB_API.insert(tableName, "Wheat Classic");

        assert (DB_API.selectOneByTitle(tableName, "Wheat Classic").next());

        DB.close();

    }

    @Order(3)
    @Test
    void testInsertDuplicateName() throws SQLException {

        DB.connect();

        assert (DB_API.insert(tableName, "Wheat Classic").equals(-1));

        DB.close();

    }

    @Order(4)
    @Test
    void testInsertById() throws SQLException {

        DB.connect();

        DB_API.insert(tableName, 234, "Wheat Gold");

        assert (DB_API.findById(tableName, 234).next());

        DB.close();

    }

    @Order(5)
    @Test
    void testInsertByBookedId() throws SQLException {

        DB.connect();

        assert (DB_API.insert(tableName, 234, "Wheat Premium").equals("Id exists in table"));

        DB.close();

    }


    @Order(6)
    @Test
    void testUpdate() throws SQLException {

        DB.connect();

        DB_API.update(tableName, 234, "Wheat Premium");

        assert (Objects.requireNonNull(DB_API.findById(tableName, 234)).getString("product_name").equals("Wheat Premium"));

        DB.close();

    }

    @Order(7)
    @Test
    void testDelete() throws SQLException {

        DB.connect();

        DB_API.insert(tableName, 287, "Wheat Generic");

        DB_API.delete(tableName, 287);

        assert (!DB_API.findById(tableName, 287).next());

        DB.close();

    }

    @Order(8)
    @Test
    void testList() throws SQLException {

        DB.connect();

        DB_API.printResultSet("Test", Objects.requireNonNull(DB_API.listMoreThanId(tableName, 200)));

        DB.close();

    }


}



