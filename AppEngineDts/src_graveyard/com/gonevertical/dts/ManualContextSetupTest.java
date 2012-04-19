package com.gonevertical.dts;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gonevertical.backup.data.EntityStat;
import com.gonevertical.dts.AppEngineUtils;

public class ManualContextSetupTest {

  private AppEngineUtils utils;

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testManualContextSetup() {
    
    String configurationFile = "/Users/branflake2267/Documents/workspace/AppEngineBackup/parameters/options.properties";
    PropertiesConfiguration config = null;
    try {
      config = new PropertiesConfiguration(configurationFile);
    } catch (ConfigurationException e) {
      e.printStackTrace();
    }
    
//    String[] arr = config.getStringArray("mysqlserver.param");
//    HashMap<String, String> properties = StringUtil.createHashMap(arr, "=");
//    DatabaseData databaseData = new DatabaseData(DatabaseData.TYPE_MYSQL, properties);
//    
//    String tmpPath = config.getString("mysqlserver.pooling.tmppath");
//    ManualSetupOfInitialPoolingContext.registerContext(tmpPath, databaseData);
//    
//    Connection conn = databaseData.getConnetionByConext();
//    
//    try {
//      assertEquals(false, conn.isClosed());
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//    
//    String query = "Select 1+1";
//    String s = new MySqlQueryLib().queryString(databaseData, query);
//    assertEquals("2", s);
    
    System.out.println("test");
  }


}
