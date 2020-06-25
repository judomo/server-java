package DB;

import java.util.Objects;

public class Main {

    public final static String dbName = "database.db";
    public final static String tableName = "Product";

    public static void main(String[] args) {

        DB.connect();

        DB_API.printResultSet("Test", Objects.requireNonNull(DB_API.selectAll(tableName)));

        DB.close();

    }


}
