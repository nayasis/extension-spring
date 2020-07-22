package com.github.nayasis.spring.extension.query.sql.mapper;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeMapperIF<T> {

    void setParameter( PreparedStatement ps, int index, T param ) throws SQLException;

    void setOutParameter( CallableStatement cs, int index ) throws SQLException;

    T getResult( ResultSet rs, String columnName ) throws SQLException;

    T getResult( ResultSet rs, int columnIndex ) throws SQLException;

    T getResult( CallableStatement cs, int columnIndex ) throws SQLException;

}