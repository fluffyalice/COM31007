package uk.ac.shef.oak.com4510.mydatabase;

/**
 * DatabaseSession.java
 * @author Feng Li, Ruiqing Xu
 */

public final class DatabaseSession {

    private DatabaseSession(){}

    public static DatabaseConfig get() {
        return DatabaseInitialize.getDatabaseConfig();
    }
}   