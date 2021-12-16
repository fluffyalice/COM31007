package uk.ac.shef.oak.com4510.mydatabase;

public final class DatabaseSession {

    private DatabaseSession(){}

    public static DatabaseConfig get() {
        return DatabaseInitialize.getDatabaseConfig();
    }
}   