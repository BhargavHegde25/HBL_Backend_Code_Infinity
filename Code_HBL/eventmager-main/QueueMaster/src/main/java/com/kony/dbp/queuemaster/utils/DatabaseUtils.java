package com.kony.dbp.queuemaster.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public abstract class DatabaseUtils {

  // Different connection pool types.
  public enum ConnectionPoolType {
    INTERNAL,
    REQUESTS
  }

  private static HikariDataSource cpInternal;
  private static HikariDataSource cpRequests;

  // Method to close one or more connections.
  public static void close(Connection ...connections) {
    for (Connection connection : connections) {
      if (connection != null) {
        try {
          connection.close();
        }
        catch (SQLException sqlex) { }
      }
    }
  }

  // Method to close one or more result sets.
  public static void close(ResultSet ...resultSets) {
    for (ResultSet resultSet : resultSets) {
      if (resultSet != null) {
        try {
          resultSet.close();
        }
        catch (SQLException sqlex) { }
      }
    }
  }

  // Method to close one or more statements.
  public static void close(Statement ...statements) {
    for (Statement statement : statements) {
      if (statement != null) {
        try {
          statement.close();
        }
        catch (SQLException sqlex) { }
      }
    }
  }

  // Method to obtain a database connection from the pool.
  public static Connection getDatabaseConnectionFromPool(ConnectionPoolType poolType) throws SQLException {
    HikariDataSource cp;
    boolean internal;
    synchronized (DatabaseUtils.class) {
      switch (poolType) {
      case INTERNAL: internal = true; cp = cpInternal; break;
      case REQUESTS: internal = false; cp = cpRequests; break;
      default: return null;
      }
      if (cp == null) {
        String databaseDriver = Config.getValue("QUEUEMASTER_DATABASE_DRIVER");
        String databaseURL = Config.getValue("QUEUEMASTER_DATABASE_URL");
        String databaseUser = Config.getValue("QUEUEMASTER_DATABASE_USER");
        String databasePassword = Config.getValue("QUEUEMASTER_DATABASE_PASSWORD");
        String connectionPoolSizeKeyword = internal ?
            "QUEUEMASTER_DATABASE_INTERNAL_CONNECTION_POOL_SIZE" :
            "QUEUEMASTER_DATABASE_CONNECTION_POOL_SIZE";
        int databaseConnectionPoolSize = Config.getIntValue(connectionPoolSizeKeyword, 5);
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(databaseDriver);
        config.setJdbcUrl(databaseURL);
        config.setUsername(databaseUser);
        config.setPassword(databasePassword);
        config.setAutoCommit(true);
        config.setMaximumPoolSize(databaseConnectionPoolSize);
        config.setMinimumIdle(1);
        config.setIdleTimeout(300000);
        config.setPoolName("QueueMaster-" + (internal ? "Internal" : "Requests"));
        cp = new HikariDataSource(config);
        if (internal) {
          cpInternal = cp;
        } else {
          cpRequests = cp;
        }
      }
    }
    return cp.getConnection();
  }

  // Method to close all database connections currently held in the connection pool.
  public static void flushDatabaseConnectionPool(ConnectionPoolType ...poolTypes) {
    synchronized (DatabaseUtils.class) {
      for (ConnectionPoolType poolType : poolTypes) {
        switch (poolType) {
        case INTERNAL: if(cpInternal!=null)cpInternal.close(); cpInternal = null; break;
        case REQUESTS: if (cpRequests!=null)cpRequests.close(); cpRequests = null; break;
        }
      }
    }
  }

  // Method to return a connection to the database to the connection pool. The connection will release
  // itself back to the relevant pool when it is "closed", so that's all we need to do here.
  public static void returnDatabaseConnectionToPool(Connection connection) {
    if (connection != null) {
      try {
        connection.close();
      }
      catch (SQLException sqlex) { }
    }
  }

  // Check whether a connection has autocommit disabled, and if so rollback and reset it.
  public static void rollbackAndReenableAutocommit(Connection connection) {
    if (connection != null) {
      try {
        if (!connection.getAutoCommit()) {
          try {
            connection.rollback();
          }
          catch (SQLException sqlex) { }
          try {
            connection.setAutoCommit(true);
          }
          catch (SQLException sqlex) { }
        }
      }
      catch (SQLException sqlex) { }
    }
  }
}
