package com.gonevertical.dts;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gonevertical.dts.data.EntityStat;
import com.gonevertical.dts.utils.AppEngineUtils;

public class AppEngineUtilsTest {

  private String configfilepath = "/Users/branflake2267/Documents/workspace/AppEngineDts/parameters/options.properties";
  
  private AppEngineUtils utils;

  @Before
  public void setUp() throws Exception {
    PropertyConfigurator.configure("/Users/branflake2267/Documents/workspace/AppEngineDts/parameters/log4j.properties");
    
    login();
  }

  @After
  public void tearDown() throws Exception {
  }

  private void login() {
    String username = "branflake2267@gmail.com";
    String password = "tmqpekarxopekynf";
    String appid = "studentlearningplan.appspot.com";
    try {
      utils = AppEngineUtils.newInstance(username, password, appid);
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }

  @Test
  public void testNewInstance() {
    String username = "branflake2267@gmail.com";
    String password = "tmqpekarxopekynf";
    String appid = "studentlearningplan.appspot.com";
    try {
      utils = AppEngineUtils.newInstance(username, password, appid);
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }

  @Test
  public void testGetKinds() {
    boolean withSystemKinds = false;
    ArrayList<String> kinds = utils.getKinds(withSystemKinds);

    boolean hasSystem = false;
    Iterator<String> itr = kinds.iterator();
    while(itr.hasNext()) {
      String e = itr.next();
      if (e != null && e.contains("__") == true) {
        hasSystem = true;
      }
      assertEquals(false, hasSystem);
    }
  }

  @Test
  public void testGetGlobalStat() {
    EntityStat es = utils.getStatGlobal();
    String left = "total_entity_usage";
    assertEquals(left, es.getKind());
    if (es.getCount() == 0) {
      fail("why no count?");
    }
    if (es.getBytes() == 0) {
      fail("why no bytes?");
    }
    if (es.getTimestamp() == null) {
      fail("why no timestamp?");
    }
  }

  @Test
  public void testGetStat() {
    ArrayList<EntityStat> es = utils.getStats();
    assertNotNull(es);
    if (es.size() == 1) {
      fail("there should be more than one here.");
    }
    Iterator<EntityStat> itr = es.iterator();
    while(itr.hasNext()) {
      EntityStat stat = itr.next();
      assertNotNull(stat.getKind());
      if (stat.getCount() == 0) {
        fail("why no count?");
      }
      if (stat.getBytes() == 0) {
        fail("why no bytes?");
      }
      if (stat.getTimestamp() == null) {
        fail("why no timestamp?");
      }
    }
  }

  @Test
  public void testGetStats() {
    EntityStat es = utils.getStat("ClassJdo");
    assertNotNull(es);
    assertNotNull(es.getKind());
    if (es.getCount() == 0) {
      fail("why no count?");
    }
    if (es.getBytes() == 0) {
      fail("why no bytes?");
    }
    if (es.getTimestamp() == null) {
      fail("why no timestamp?");
    }
  }



}
