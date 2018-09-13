package com.kodilla.jdbc;

import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;

public class StoredProcTestSuite {

    @Test
    public void testUpdateVipLevels() throws SQLException {
        //given
        DbManager dbManager = DbManager.getInstance();
        String sqlUpdate = "Update readers set vip_level=\"not set\"";
        Statement statement = dbManager.getConnection().createStatement();
        statement.executeUpdate(sqlUpdate);
        //when
        String sqlProcedureCall = "call updateVipLevels()";
        statement.execute(sqlProcedureCall);
        //then
        String sqlCheckTable = "select count(*) as how_many" +
                " from readers where vip_level =\"not set\"";
        ResultSet rs = statement.executeQuery(sqlCheckTable);
        int howMany = -1;
        if (rs.next()) {
            howMany = rs.getInt("how_many");
        }
        assertEquals(0, howMany);
    }

    @Test
    public void testUpdateBestsellers() throws SQLException {
        //given
        final DbManager dbManager = DbManager.getInstance();
        final Statement statement = dbManager.getConnection().createStatement();
        final String sqlSetNull = "update books set bestseller = null";
        final String sqlUpdateBestsellers = "call updateBestsellers()";
        final String getBestsellerQuantityWithFalse =
                "select count(*) as qua " +
                        "from books " +
                        "where bestseller = false ";
        final String getBestsellerQuantityWithTrue =
                "select count(*) as qua " +
                        "from books " +
                        "where bestseller = true";
        final String getBestsellerQuantityWithNotNull =
                "select count(*) as qua " +
                        "from books " +
                        "where bestseller = false or bestseller = true";
        int testFalseResult = -1;
        int testTrueResult = -1;
        int testNotNullResult = -1;
        statement.executeUpdate(sqlSetNull);
        //when
        statement.execute(sqlUpdateBestsellers);
        final ResultSet resultSetFalse = statement.executeQuery(getBestsellerQuantityWithFalse);
        if (resultSetFalse.next()) {
            testFalseResult = resultSetFalse.getInt("qua");
        }
        final ResultSet resultSetTrue = statement.executeQuery(getBestsellerQuantityWithTrue);
        if (resultSetTrue.next()) {
            testTrueResult = resultSetTrue.getInt("qua");
        }
        final ResultSet resultSetNotNull = statement.executeQuery(getBestsellerQuantityWithNotNull);
        if (resultSetNotNull.next()) {
            testNotNullResult = resultSetNotNull.getInt("qua");
        }
        //then
        assertEquals(3, testFalseResult);
        assertEquals(1, testTrueResult);
        assertEquals(4, testNotNullResult);
    }
}