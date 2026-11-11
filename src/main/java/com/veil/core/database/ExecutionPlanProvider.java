package com.veil.core.database;

public interface ExecutionPlanProvider {

    DatabaseType getDatabaseType();

    String getExecutionPlan(String sql);
}