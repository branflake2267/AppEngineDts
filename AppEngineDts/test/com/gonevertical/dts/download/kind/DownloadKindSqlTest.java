package com.gonevertical.dts.download.kind;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gonevertical.dts.ClientFactory;
import com.gonevertical.dts.data.EntityStat;
import com.gonevertical.dts.download.kind.DownloadKind;
import com.gonevertical.dts.utils.AppEngineUtils;
import com.gonevertical.dts.utils.SqlUtils;

public class DownloadKindSqlTest {

  private String configfilepath = "/Users/branflake2267/Documents/workspace/AppEngineDts/parameters/options.properties";

  @Before
  public void setUp() throws Exception {
    PropertyConfigurator.configure("/Users/branflake2267/Documents/workspace/AppEngineDts/parameters/log4j.properties");
  }

  @After
  public void tearDown() throws Exception {
  }
  
  @Test
  public void testDownloadCedars() {
    
    ClientFactory cf = null;
    try {
      cf = new ClientFactory(new File(configfilepath));
    } catch (ConfigurationException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (cf == null) {
      fail("no client factory");
    }
    
//    Calendar calendar = Calendar.getInstance();
//    calendar.set(Calendar.MONTH, 12);
//    calendar.set(Calendar.DAY_OF_MONTH, 1);
//    calendar.set(Calendar.YEAR, 2011);
//    calendar.set(Calendar.HOUR, 1);
//    calendar.set(Calendar.MINUTE, 0);
//    calendar.set(Calendar.SECOND, 0);
//    calendar.set(Calendar.MILLISECOND, 0);
//    cf.setTime(calendar);
    
    String kind = "CedarsJdo";
    EntityStat es = cf.getAppEngineUtils().getStat(kind);
    long offset = 0;
    int limit = 10;
    Long finish = 100l;
    Long total = es.getCount();
    
    String sql = "";
    int t = 0;
    boolean exists = cf.getSql().getTransLib().doesTableExist(cf.getDatabaseData(), cf.getSql().getTableName(kind));
      if (exists == true) {
      // delete for testing
      String sqlD = "DELETE FROM " + cf.getSql().getTableName(kind) + "";
      cf.getSql().getQueryLib().update(cf.getDatabaseData(), sqlD);
      
      
      // total count = 0
      sql = "SELECT COUNT(*) FROM " + cf.getSql().getTableName(kind) + "";
      t = cf.getSql().getQueryLib().queryInteger(cf.getDatabaseData(), sql);
      assertEquals(0, t);
    }
    

    // download 100 of 
    DownloadKind dk = new DownloadKind(cf);
    dk.setLimit(offset, limit, finish, total);
    dk.setKind(kind);
    dk.run();
    
    
    // test 100 where inserted for date
    sql = "SELECT COUNT(*) FROM " + cf.getSql().getTableName(kind) + " WHERE " + SqlUtils.COLUMNDATE + "='" + cf.getDateTime() + "'";
    t = cf.getSql().getQueryLib().queryInteger(cf.getDatabaseData(), sql);
    //System.out.println(sql + " = " + t);
    assertEquals(finish.intValue(), t);
    
    // update 100
    dk = new DownloadKind(cf);
    dk.setLimit(offset, limit, finish, total);
    dk.setKind(kind);
    dk.run();
    
    // test 100 where inserted for date
    sql = "SELECT COUNT(*) FROM " + cf.getSql().getTableName(kind) + " WHERE " + SqlUtils.COLUMNDATE + "='" + cf.getDateTime() + "'";
    t = cf.getSql().getQueryLib().queryInteger(cf.getDatabaseData(), sql);
    assertEquals(finish.intValue(), t);
   
    // tet total count = 100
    sql = "SELECT COUNT(*) FROM " + cf.getSql().getTableName(kind) + "";
    t = cf.getSql().getQueryLib().queryInteger(cf.getDatabaseData(), sql);
    assertEquals(100, t);
    
    System.out.println("finished test");
  }
  
  @Test
  public void testDownloadPeopleJdo() {
    
    ClientFactory cf = null;
    try {
      cf = new ClientFactory(new File(configfilepath));
    } catch (ConfigurationException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (cf == null) {
      fail("no client factory");
    }
    
//    Calendar calendar = Calendar.getInstance();
//    calendar.set(Calendar.MONTH, 12);
//    calendar.set(Calendar.DAY_OF_MONTH, 1);
//    calendar.set(Calendar.YEAR, 2011);
//    calendar.set(Calendar.HOUR, 1);
//    calendar.set(Calendar.MINUTE, 0);
//    calendar.set(Calendar.SECOND, 0);
//    calendar.set(Calendar.MILLISECOND, 0);
//    cf.setTime(calendar);
    
    String kind = "PeopleJdo";
    EntityStat es = cf.getAppEngineUtils().getStat(kind);
    long offset = 0;
    int limit = 10;
    Long finish = 100l;
    Long total = es.getCount();
    
    String sql = "";
    int t = 0;
    boolean exists = cf.getSql().getTransLib().doesTableExist(cf.getDatabaseData(), cf.getSql().getTableName(kind));
      if (exists == true) {
      // delete for testing
      String sqlD = "DELETE FROM " + cf.getSql().getTableName(kind) + "";
      cf.getSql().getQueryLib().update(cf.getDatabaseData(), sqlD);
      
      
      // total count = 0
      sql = "SELECT COUNT(*) FROM " + cf.getSql().getTableName(kind) + "";
      t = cf.getSql().getQueryLib().queryInteger(cf.getDatabaseData(), sql);
      assertEquals(0, t);
    }
    
    
    // download 100 of 
    DownloadKind dk = new DownloadKind(cf);
    dk.setLimit(offset, limit, finish, total);
    dk.setKind(kind);
    dk.run();
    
    
    // test 100 where inserted for date
    sql = "SELECT COUNT(*) FROM " + cf.getSql().getTableName(kind) + " WHERE " + SqlUtils.COLUMNDATE + "='" + cf.getDateTime() + "'";
    t = cf.getSql().getQueryLib().queryInteger(cf.getDatabaseData(), sql);
    //System.out.println(sql + " = " + t);
    assertEquals(finish.intValue(), t);
    
    // update 100
    dk = new DownloadKind(cf);
    dk.setLimit(offset, limit, finish, total);
    dk.setKind(kind);
    dk.run();
    
    // test 100 where inserted for date
    sql = "SELECT COUNT(*) FROM " + cf.getSql().getTableName(kind) + " WHERE " + SqlUtils.COLUMNDATE + "='" + cf.getDateTime() + "'";
    t = cf.getSql().getQueryLib().queryInteger(cf.getDatabaseData(), sql);
    assertEquals(finish.intValue(), t);
   
    // tet total count = 100
    sql = "SELECT COUNT(*) FROM " + cf.getSql().getTableName(kind) + "";
    t = cf.getSql().getQueryLib().queryInteger(cf.getDatabaseData(), sql);
    assertEquals(100, t);
    
    System.out.println("finished test");
  }
  
  

}
