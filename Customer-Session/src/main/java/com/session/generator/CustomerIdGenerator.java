package com.session.generator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.jdbc.Work;
import static com.session.constant.ConstantUsage.PREFIX_CUSTOMER;
import static com.session.constant.ConstantUsage.FORMAT;
import static com.session.constant.ConstantUsage.PREFIX_FORMAT;
import static com.session.constant.ConstantUsage.QUERY_CUSTOMER;

import org.hibernate.Session;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        Session currentSession = (Session) session;

        final String[] generatedId = new String[1];

        currentSession.doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_CUSTOMER)) {
                    ResultSet rs = preparedStatement.executeQuery();
                    if (rs.next()) {
                        int nextValue = rs.getInt(1) + 1;
                        generatedId[0] = PREFIX_CUSTOMER + String.format(FORMAT, nextValue);
                    } else {
                        generatedId[0] = PREFIX_CUSTOMER + PREFIX_FORMAT;
                    }
                }
            }
        });

        return generatedId[0];
    }
}
