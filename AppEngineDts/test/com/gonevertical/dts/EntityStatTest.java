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

public class EntityStatTest {

  private String configfilepath = "/Users/branflake2267/Documents/workspace/AppEngineDts/parameters/options.properties";

  @Before
  public void setUp() throws Exception {
    PropertyConfigurator.configure("/Users/branflake2267/Documents/workspace/AppEngineDts/parameters/log4j.properties");
    
  }

  @After
  public void tearDown() throws Exception {
  }
  
  @Test
  public void testSetData() {
    
    String kind = "kind";
    long count = 12034;
    long bytes = 4301;
    Date timestamp = new Date();
    
    EntityStat es = new EntityStat();
    es.setData(kind, count, bytes, timestamp);
    
    assertEquals(kind, es.getKind());
    assertEquals(count, es.getCount());
    assertEquals(bytes, es.getBytes());
    assertEquals(timestamp, es.getTimestamp());
  }

}
