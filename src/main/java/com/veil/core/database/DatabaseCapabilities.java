package com.veil.core.database;

public class DatabaseCapabilities {

    private final boolean supportsSql;
    private final boolean supportsExplain;
    private final boolean supportsIndexes;
    private final boolean supportsTransactions;
    private final boolean supportsSchemaInspection;

    public DatabaseCapabilities(
            boolean supportsSql,
            boolean supportsExplain,
            boolean supportsIndexes,
            boolean supportsTransactions,
            boolean supportsSchemaInspection) {

        this.supportsSql = supportsSql;
        this.supportsExplain = supportsExplain;
        this.supportsIndexes = supportsIndexes;
        this.supportsTransactions = supportsTransactions;
        this.supportsSchemaInspection = supportsSchemaInspection;
    }

    public boolean isSupportsSql() {
        return supportsSql;
    }

    public boolean isSupportsExplain() {
        return supportsExplain;
    }

    public boolean isSupportsIndexes() {
        return supportsIndexes;
    }

    public boolean isSupportsTransactions() {
        return supportsTransactions;
    }

    public boolean isSupportsSchemaInspection() {
        return supportsSchemaInspection;
    }
}