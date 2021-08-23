package com.zenglinhui.test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test JDBC
 * @author zenglh
 * @date 2021/8/23 17:17
 */
public class JdbcTest {

    public static void main(String[] args) throws SQLException {
        DataSource dataSource = JdbcDataSourceUtil.jdbcDataSource();
        String sql = "select order_id from t_order o where o.user_id = ? and o.order_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, 10);
            ps.setInt(2, 1000);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.println(rs.getInt(1));
                }
            }
        }
    }

}
