package com.gonevertical.dts;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.PropertyConfigurator;
import org.gonevertical.dts.lib.StringUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gonevertical.dts.data.EntityStat;
import com.gonevertical.dts.utils.AppEngineUtils;

public class PropertiesTest {

  private String configfilepath = "/Users/branflake2267/Documents/workspace/AppEngineDts/parameters/options.properties";
  
  private AppEngineUtils utils;

  @Before
  public void setUp() throws Exception {
    PropertyConfigurator.configure("/Users/branflake2267/Documents/workspace/AppEngineDts/parameters/log4j.properties");
  
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testNewInstance() {
  
    PropertiesConfiguration config = null;
    try {
       config = new PropertiesConfiguration(configfilepath);
    } catch (ConfigurationException e) {
      e.printStackTrace();
    }
    
    String[] arr = config.getStringArray("mysqlserver.param");
    HashMap<String, String> hm = StringUtil.createHashMap(arr, "=");
    
    assertEquals("jdbc/conn", hm.get("name"));
    assertEquals("192.168.10.79", hm.get("host"));
    assertEquals("3306", hm.get("port"));
    assertEquals("test", hm.get("username"));
    assertEquals("test", hm.get("password"));
    assertEquals("test", hm.get("database"));
    assertEquals("Container", hm.get("auth"));
    assertEquals("javax.sql.DataSource", hm.get("type"));
    assertEquals("jdbc:mysql://192.168.10.79:3306/test?autoReconnect=true", hm.get("url")); 
    
    
  }



}
