package webshop.dao.implementation.db;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;

public class EasyDb{
    private String dbName;
    private String password;
    private String user;
    private static EasyDb instance;

    private EasyDb(){
        dbName = "boss";
        user = System.getenv("DB_USER_NAME");
        password = System.getenv("DB_PASSWORD");
    }

    public static EasyDb getInstance(){
        if (instance == null){
            instance = new EasyDb();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        String url = "jdbc:h2:file:" + System.getProperty("user.dir") + "/buy-our-stuff-saved-stuffs/" + dbName;
        //String url = "jdbc:h2:file:~" + "/buy-our-stuff-saved-stuffs/" + dbName;
        Class.forName("org.h2.Driver");
        return DriverManager.getConnection(url, this.user, this.password);
    }

    public Integer executeUpdate(String SQL, Object... params){
        Integer generatedKey = null;
        try (Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS);

            for (int i = 0; i < params.length; i++){
                setParameterInStatement(i + 1, params[i], statement);
            }

            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            resultSet.next();
            if (resultSet.getMetaData().getColumnCount() > 0) generatedKey = resultSet.getInt(1);

            statement.close();
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return generatedKey;
    }
    
    public EasyResultSet executeQuery(String SQL, Object... params){
        EasyResultSet easyResultSet = null;
        try (Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(SQL);

            for (int i = 0; i < params.length; i++){
                setParameterInStatement(i + 1, params[i], statement);
            }

            ResultSet resultSet = statement.executeQuery();
            easyResultSet = new EasyResultSet(resultSet);

            statement.close();
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            easyResultSet = new EasyResultSet();
        }
        return easyResultSet;
    }

    private void setParameterInStatement(int parameterIndex, Object parameter, PreparedStatement statement) throws SQLException{
        if (parameter instanceof String) statement.setString(parameterIndex, (String) parameter);
        else if (parameter instanceof Integer) statement.setInt(parameterIndex, (Integer) parameter);
        else if (parameter instanceof Short) statement.setShort(parameterIndex, (Short) parameter);
        else if (parameter instanceof Long) statement.setLong(parameterIndex, (Long) parameter);
        else if (parameter instanceof Float) statement.setFloat(parameterIndex, (Float) parameter);
        else if (parameter instanceof Double) statement.setDouble(parameterIndex, (Double) parameter);
        else if (parameter instanceof BigDecimal) statement.setBigDecimal(parameterIndex, (BigDecimal) parameter);
        else if (parameter instanceof Boolean) statement.setBoolean(parameterIndex, (Boolean) parameter);
        else if (parameter instanceof Time) statement.setTime(parameterIndex, (Time) parameter);
        else if (parameter instanceof Timestamp) statement.setTimestamp(parameterIndex, (Timestamp) parameter);
        else if (parameter instanceof Date) statement.setDate(parameterIndex, (Date) parameter);
        else if (parameter instanceof URL) statement.setURL(parameterIndex, (URL) parameter);
        else throw new SQLFeatureNotSupportedException("EasyDb error - Data type not implemented yet...");
    }
}