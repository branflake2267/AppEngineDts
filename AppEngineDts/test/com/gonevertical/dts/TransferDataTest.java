package com.gonevertical.dts;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gonevertical.dts.data.EntityStat;
import com.gonevertical.dts.utils.Sharding;

public class TransferDataTest {



  @Before
  public void setUp() throws Exception {

  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testSetEntitiesProperties() {

    String log4jPath = "/Users/branflake2267/Documents/workspace/AppEngineDts/parameters/log4j.properties";

    String optionsPath = "/Users/branflake2267/Documents/workspace/AppEngineDts/parameters/test/testentities.properties";

    String direction = "download";

    Transfer dt = new Transfer();
    dt.setLog4Jproperties(log4jPath);
    dt.setOptions(optionsPath);
    dt.setDirection(direction);
    dt.test();

    ClientFactory cf = dt.getClientFactory();

    assertEquals(100, cf.getFinish().intValue());

    ArrayList<String> entities = cf.getDownloadEntities();

    if (entities.contains("CedarsJdo") == false) {
      fail("no cedars");
    }

    if (entities.contains("PeopleJdo") == false) {
      fail("no people");
    }

  }
  
  @Test
  public void testSetEntitiesPropertiesAll() {

    String log4jPath = "/Users/branflake2267/Documents/workspace/AppEngineDts/parameters/log4j.properties";

    String optionsPath = "/Users/branflake2267/Documents/workspace/AppEngineDts/parameters/test/allentities.properties";

    String direction = "download";

    Transfer dt = new Transfer();
    dt.setLog4Jproperties(log4jPath);
    dt.setOptions(optionsPath);
    dt.setDirection(direction);
    dt.test();

    ClientFactory cf = dt.getClientFactory();

    assertEquals(100, cf.getFinish().intValue());

    ArrayList<String> entities = cf.getDownloadEntities();

    if (entities.contains("all") == false) {
      fail("no all");
    }
    
  }

  @Test
  public void testTestinSLPAll() {

    String log4jPath = "/Users/branflake2267/Documents/workspace/AppEngineDts/parameters/log4j.properties";

    String optionsPath = "/Users/branflake2267/Documents/workspace/AppEngineDts/parameters/options.properties";

    String direction = "download";

    String[] args = new String[3];
    args[0] = "-d=" + direction;
    args[1] = "-l=" + log4jPath;;
    args[2] = "-o=" + optionsPath;
    
    Transfer.main(args);
    

   
    
  }
}
