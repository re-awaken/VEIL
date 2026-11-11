package com.veil.core.database;

import java.util.List;
import java.util.Map;

// common contract for database-specific implementations
public interface DatabaseAdapter {

    DatabaseType getDatabaseType();

    DatabaseCapabilities getCapabilities();

    boolean testConnection();

    String getDatabaseName();

    List<Map<String, Object>> getTables();

    List<Map<String, Object>> getIndexes();

}