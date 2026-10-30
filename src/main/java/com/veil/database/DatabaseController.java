
package com.veil.database;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/database")
public class DatabaseController {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public Map<String, Object> getDatabaseMetadata() {
        String database = jdbcTemplate.queryForObject(
                "SELECT current_database()",
                String.class
        );

        List<Map<String, Object>> tables = jdbcTemplate.queryForList("""
                SELECT table_name
                FROM information_schema.tables
                WHERE table_schema = 'public'
                  AND table_type = 'BASE TABLE'
                ORDER BY table_name
                """);

        List<Map<String, Object>> indexes = jdbcTemplate.queryForList("""
                SELECT indexname, tablename
                FROM pg_indexes
                WHERE schemaname = 'public'
                ORDER BY tablename, indexname
                """);

        return Map.of(
                "database", database,
                "tables", tables,
                "indexes", indexes
        );
    }
}
