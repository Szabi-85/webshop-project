package webshop.dao.implementation.db;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EasyResultSet{
    private List<String> columnTypes = new ArrayList<>();
    private List<String> columnLabels = new ArrayList<>();
    private List<ArrayList<Object>> columns = new ArrayList<>();

    private Integer rowIterator = null;

    public EasyResultSet(){

    }
    public EasyResultSet(ResultSet resultSet){
        try{
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 0; i < columnCount; i++){
                if (metaData.getColumnTypeName(i + 1).equals("CLOB")) columnTypes.add("String");
                else if (metaData.getColumnTypeName(i + 1).equals("VARCHAR")) columnTypes.add("String");
                else if (metaData.getColumnTypeName(i + 1).equals("LONGVARCHAR")) columnTypes.add("String");
                else if (metaData.getColumnTypeName(i + 1).equals("TEXT")) columnTypes.add("String");
                else if (metaData.getColumnTypeName(i + 1).equals("INTEGER")) columnTypes.add("Integer");
                else if (metaData.getColumnTypeName(i + 1).equals("SMALLINT")) columnTypes.add("Short");
                else if (metaData.getColumnTypeName(i + 1).equals("BIGINT")) columnTypes.add("Long");
                else if (metaData.getColumnTypeName(i + 1).equals("DECIMAL")) columnTypes.add("BigDecimal");
                else if (metaData.getColumnTypeName(i + 1).equals("FLOAT")) columnTypes.add("Float");
                else if (metaData.getColumnTypeName(i + 1).equals("DOUBLE")) columnTypes.add("Double");
                else if (metaData.getColumnTypeName(i + 1).equals("BOOLEAN")) columnTypes.add("Boolean");
                else if (metaData.getColumnTypeName(i + 1).equals("TIME")) columnTypes.add("Time");
                else if (metaData.getColumnTypeName(i + 1).equals("TIMESTAMP")) columnTypes.add("Timestamp");
                else if (metaData.getColumnTypeName(i + 1).equals("DATE")) columnTypes.add("Date");

                columnLabels.add(metaData.getColumnName(i + 1));
            }
            while (resultSet.next()){
                ArrayList<Object> row = new ArrayList<>();
                for (int i = 0; i < columnCount; i++){
                    if (columnTypes.get(i).equals("String")) row.add(resultSet.getString(i + 1));
                    else if (columnTypes.get(i).equals("Integer")) row.add(resultSet.getInt(i + 1));
                    else if (columnTypes.get(i).equals("Short")) row.add(resultSet.getShort(i + 1));
                    else if (columnTypes.get(i).equals("Long")) row.add(resultSet.getLong(i + 1));
                    else if (columnTypes.get(i).equals("Float")) row.add(resultSet.getFloat(i + 1));
                    else if (columnTypes.get(i).equals("Double")) row.add(resultSet.getDouble(i + 1));
                    else if (columnTypes.get(i).equals("Boolean")) row.add(resultSet.getBoolean(i + 1));
                    else if (columnTypes.get(i).equals("Time")) row.add(resultSet.getTime(i + 1));
                    else if (columnTypes.get(i).equals("Timestamp")) row.add(resultSet.getTimestamp(i + 1));
                    else if (columnTypes.get(i).equals("Date")) row.add(resultSet.getDate(i + 1));
                    else if (columnTypes.get(i).equals("BigDecimal")) row.add(resultSet.getBigDecimal(i + 1));
                }
                if (row.size() > 0) columns.add(row);
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }

    public String getString(int columnIndex){
        if (columnIndex < 1 || columnIndex > columnTypes.size()) throw new ArrayIndexOutOfBoundsException("Invalid column index");

        if (!columnTypes.get(columnIndex - 1).equals("String")) throw new ClassCastException("Column type is not String");

        return (String) columns.get(rowIterator).get(columnIndex - 1);
    }
    public String getString(String columnLabel){
        for (int i = 0; i < columnLabel.length(); i++){
            if (columnLabels.get(i).equals(columnLabel.toUpperCase())) return getString(i + 1);
        }
        return null;
    }

    public Integer getInt(int columnIndex){
        if (columnIndex < 1 || columnIndex > columnTypes.size()) throw new ArrayIndexOutOfBoundsException("Invalid column index");
        if (!columnTypes.get(columnIndex - 1).equals("Integer")) throw new ClassCastException("Column type is not Integer");

        return (Integer) columns.get(rowIterator).get(columnIndex - 1);
    }
    public Integer getInt(String columnLabel){
        for (int i = 0; i < columnLabel.length(); i++){
            if (columnLabels.get(i).equals(columnLabel.toUpperCase())) return getInt(i + 1);
        }
        return null;
    }

    public Short getShort(int columnIndex){
        if (columnIndex < 1 || columnIndex > columnTypes.size()) throw new ArrayIndexOutOfBoundsException("Invalid column index");
        if (!columnTypes.get(columnIndex - 1).equals("Short")) throw new ClassCastException("Column type is not Short");

        return (Short) columns.get(rowIterator).get(columnIndex - 1);
    }
    public Short getShort(String columnLabel){
        for (int i = 0; i < columnLabel.length(); i++){
            if (columnLabels.get(i).equals(columnLabel.toUpperCase())) return getShort(i + 1);
        }
        return null;
    }

    public Long getLong(int columnIndex){
        if (columnIndex < 1 || columnIndex > columnTypes.size()) throw new ArrayIndexOutOfBoundsException("Invalid column index");
        if (!columnTypes.get(columnIndex - 1).equals("Long")) throw new ClassCastException("Column type is not Long");

        return (Long) columns.get(rowIterator).get(columnIndex - 1);
    }
    public Long getLong(String columnLabel){
        for (int i = 0; i < columnLabel.length(); i++){
            if (columnLabels.get(i).equals(columnLabel.toUpperCase())) return getLong(i + 1);
        }
        return null;
    }

    public BigDecimal getBigDecimal(int columnIndex){
        if (columnIndex < 1 || columnIndex > columnTypes.size()) throw new ArrayIndexOutOfBoundsException("Invalid column index");
        if (!columnTypes.get(columnIndex - 1).equals("BigDecimal")) throw new ClassCastException("Column type is not BigDecimal");

        return (BigDecimal) columns.get(rowIterator).get(columnIndex - 1);
    }
    public BigDecimal getBigDecimal(String columnLabel){
        for (int i = 0; i < columnLabel.length(); i++){
            if (columnLabels.get(i).equals(columnLabel.toUpperCase())) return getBigDecimal(i + 1);
        }
        return null;
    }

    public Float getFloat(int columnIndex){
        if (columnIndex < 1 || columnIndex > columnTypes.size()) throw new ArrayIndexOutOfBoundsException("Invalid column index");
        if (!columnTypes.get(columnIndex - 1).equals("Float")) throw new ClassCastException("Column type is not Float");

        return (Float) columns.get(rowIterator).get(columnIndex - 1);
    }
    public Float getFloat(String columnLabel){
        for (int i = 0; i < columnLabel.length(); i++){
            if (columnLabels.get(i).equals(columnLabel.toUpperCase())) return getFloat(i + 1);
        }
        return null;
    }

    public Double getDouble(int columnIndex){
        if (columnIndex < 1 || columnIndex > columnTypes.size()) throw new ArrayIndexOutOfBoundsException("Invalid column index");
        if (!columnTypes.get(columnIndex - 1).equals("Double")) throw new ClassCastException("Column type is not Double");

        return (Double) columns.get(rowIterator).get(columnIndex - 1);
    }
    public Double getDouble(String columnLabel){
        for (int i = 0; i < columnLabel.length(); i++){
            if (columnLabels.get(i).equals(columnLabel.toUpperCase())) return getDouble(i + 1);
        }
        return null;
    }

    public Boolean getBoolean(int columnIndex){
        if (columnIndex < 1 || columnIndex > columnTypes.size()) throw new ArrayIndexOutOfBoundsException("Invalid column index");
        if (!columnTypes.get(columnIndex - 1).equals("Boolean")) throw new ClassCastException("Column type is not Boolean");

        return (Boolean) columns.get(rowIterator).get(columnIndex - 1);
    }
    public Boolean getBoolean(String columnLabel){
        for (int i = 0; i < columnLabel.length(); i++){
            if (columnLabels.get(i).equals(columnLabel.toUpperCase())) return getBoolean(i + 1);
        }
        return null;
    }

    public Time getTime(int columnIndex){
        if (columnIndex < 1 || columnIndex > columnTypes.size()) throw new ArrayIndexOutOfBoundsException("Invalid column index");
        if (!columnTypes.get(columnIndex - 1).equals("Time")) throw new ClassCastException("Column type is not Time");

        return (Time) columns.get(rowIterator).get(columnIndex - 1);
    }
    public Time getTime(String columnLabel){
        for (int i = 0; i < columnLabel.length(); i++){
            if (columnLabels.get(i).equals(columnLabel.toUpperCase())) return getTime(i + 1);
        }
        return null;
    }

    public Timestamp getTimestamp(int columnIndex){
        if (columnIndex < 1 || columnIndex > columnTypes.size()) throw new ArrayIndexOutOfBoundsException("Invalid column index");
        if (!columnTypes.get(columnIndex - 1).equals("Timestamp")) throw new ClassCastException("Column type is not Timestamp");

        return (Timestamp) columns.get(rowIterator).get(columnIndex - 1);
    }
    public Timestamp getTimestamp(String columnLabel){
        for (int i = 0; i < columnLabel.length(); i++){
            if (columnLabels.get(i).equals(columnLabel.toUpperCase())) return getTimestamp(i + 1);
        }
        return null;
    }

    public Date getDate(int columnIndex){
        if (columnIndex < 1 || columnIndex > columnTypes.size()) throw new ArrayIndexOutOfBoundsException("Invalid column index");
        if (!columnTypes.get(columnIndex - 1).equals("Date")) throw new ClassCastException("Column type is not Date");

        return (Date) columns.get(rowIterator).get(columnIndex - 1);
    }
    public Date getDate(String columnLabel){
        for (int i = 0; i < columnLabel.length(); i++){
            if (columnLabels.get(i).equals(columnLabel.toUpperCase())) return getDate(i + 1);
        }
        return null;
    }


    public Integer columnCount(){
        return columnTypes.size();
    }
    public String getLabel(int columnIndex){
        if (columnIndex < 1 || columnIndex > columnTypes.size()) throw new ArrayIndexOutOfBoundsException("Invalid column index");
        return columnLabels.get(columnIndex - 1);
    }

    public boolean next(){
        if (rowIterator == null){
            if (columns.size() > 0){
                rowIterator = 0;
                return true;
            }else return false;
        }
        if (rowIterator < columns.size() - 1){
            rowIterator++;
            return true;
        }
        rowIterator = null;
        return false;
    }
}