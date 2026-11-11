package com.veil.database.postgres;

import com.veil.core.database.DatabaseType;
import com.veil.core.database.ExecutionPlanProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PostgreSQLExecutionPlanProvider
        implements ExecutionPlanProvider {

    private final JdbcTemplate jdbcTemplate;

    public PostgreSQLExecutionPlanProvider(
            JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.POSTGRESQL;
    }

    @Override
    public String getExecutionPlan(String sql) {

        String explainSql = "EXPLAIN (FORMAT JSON) " + sql;

        return jdbcTemplate.queryForObject(
                explainSql,
                String.class
        );
    }
}