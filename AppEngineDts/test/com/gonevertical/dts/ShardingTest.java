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

public class ShardingTest {

  private String configfilepath = "/Users/branflake2267/Documents/workspace/AppEngineDts/parameters/options.properties";
  
  @Before
  public void setUp() throws Exception {
    PropertyConfigurator.configure("/Users/branflake2267/Documents/workspace/AppEngineDts/parameters/log4j.properties");
  }

  @After
  public void tearDown() throws Exception {
  }
  
  @Test
  public void testSetLimit() {
    int offset = 0;
    int limit = 5;
    Long finish = 100l;
    long total = 213;
    Sharding d = new Sharding();
    d.setLimit(offset, limit, finish);
    d.setCounts(total);
    assertEquals(offset, d.getOffset());
    assertEquals(limit, d.getLimit());
    assertEquals(finish, d.getFinish());
    assertEquals(total, d.getTotal());
  }

  /**
   * goal 0-100 (impose finish at 100)
   */
  @Test
  public void testGetEnd() {
    int offset = 0;
    int limit = 5;
    Long finish = 100l;
    long total = 213;
    Sharding d = new Sharding();
    d.setLimit(offset, limit, finish);
    d.setCounts(total);
    long end = d.getEnd();
    assertEquals(finish.longValue(), end);
  }
  
  /**
   * goal query 0-7 (b/c toal is 7)
   */
  @Test
  public void testGetEnd2() {
    int offset = 0;
    int limit = 10;
    Long finish = 100l;
    long total = 7;
    Sharding d = new Sharding();
    d.setLimit(offset, limit, finish);
    d.setCounts(total);
    long end = d.getEnd();
    assertEquals(7, end);
  }
  
  /**
   * goal query 0-7 (impose finish of 6)
   */
  @Test
  public void testGetEnd3() {
    int offset = 0;
    int limit = 10;
    Long finish = 100l;
    long total = 6;
    Sharding d = new Sharding();
    d.setLimit(offset, limit, finish);
    d.setCounts(total);
    long end = d.getEnd();
    assertEquals(6, end);
  }
  
  /**
   * goal query 0-55 (no finish)
   */
  @Test
  public void testGetEnd4() {
    int offset = 0;
    int limit = 10;
    Long finish = null;
    long total = 55;
    Sharding d = new Sharding();
    d.setLimit(offset, limit, finish);
    d.setCounts(total);
    long end = d.getEnd();
    assertEquals(55, end);
  }
  
  /**
   * goal 0-100 (impose finish at 100), means that should be 20 pages or loops
   */
  @Test
  public void testGetPages1() {
    int offset = 0;
    int limit = 5;
    Long finish = 100l;
    long total = 213;
    Sharding d = new Sharding();
    d.setLimit(offset, limit, finish);
    d.setCounts(total);
    long end = d.getEnd();
    int shards = d.getShards();
    assertEquals(finish.longValue(), end);
    assertEquals(20, shards);
  }
  
  /**
   * goal 0-100 (no finish), means that should be 23 shards
   */
  @Test
  public void testGetPages2() {
    int offset = 0;
    int limit = 5;
    Long finish = null;
    long total = 213;
    Sharding d = new Sharding();
    d.setLimit(offset, limit, finish);
    d.setCounts(total);
    long end = d.getEnd();
    int shards = d.getShards();
    assertEquals(total, end);
    assertEquals(43, shards); // 42.6 shards
  }
  
  /**
   * goal 0-23 (no finish), means that should be 23 shards
   */
  @Test
  public void testGetPages3() {
    int offset = 0;
    int limit = 5;
    Long finish = null;
    long total = 23;
    Sharding d = new Sharding();
    d.setLimit(offset, limit, finish);
    d.setCounts(total);
    long end = d.getEnd();
    int shards = d.getShards();
    assertEquals(total, end);
    assertEquals(5, shards); // 23.6 is 5 shards
  }
  
  /**
   * goal get shard range 
   * 0,4, 
   * 5,9, 
   * 10,14, 
   * 15,19, 
   * 20,22
   */
  @Test
  public void testGetRange() {
    int offset = 0;
    int limit = 5;
    Long finish = null;
    long total = 23;
    Sharding d = new Sharding();
    d.setLimit(offset, limit, finish);
    d.setCounts(total);
    int shards = d.getShards();
    
    assertEquals(5, shards);
    
    long[] l1 = {0l,4l};
    long[] r1 = d.getRange(0);
    assertEquals(l1[0], r1[0]);
    assertEquals(l1[1], r1[1]);
    
    long[] l2 = {5l,9l};
    long[] r2 = d.getRange(1);
    assertEquals(l2[0], r2[0]);
    assertEquals(l2[1], r2[1]);
    
    long[] l3 = {10l,14l};
    long[] r3 = d.getRange(2);
    assertEquals(l3[0], r3[0]);
    assertEquals(l3[1], r3[1]);
    
    long[] l4 = {15l,19l};
    long[] r4 = d.getRange(3);
    assertEquals(l4[0], r4[0]);
    assertEquals(l4[1], r4[1]);
    
    long[] l5 = {20l,22l};
    long[] r5 = d.getRange(4);
    assertEquals(l5[0], r5[0]);
    assertEquals(l5[1], r5[1]);
  }
  
  /**
   * goal get shard range 
   * 10,14, 
   * 15,19, 
   * 20,22
   */
  @Test
  public void testGetRange2() {
    int offset = 11;
    int limit = 5;
    Long finish = null;
    long total = 23;
    Sharding d = new Sharding();
    d.setLimit(offset, limit, finish);
    d.setCounts(total);
    int shards = d.getShards();
    
    assertEquals(3, shards);
    
    long[] l3 = {10l,14l};
    long[] r3 = d.getRange(0);
    assertEquals(l3[0], r3[0]);
    assertEquals(l3[1], r3[1]);
    
    long[] l4 = {15l,19l};
    long[] r4 = d.getRange(1);
    assertEquals(l4[0], r4[0]);
    assertEquals(l4[1], r4[1]);
    
    long[] l5 = {20l,22l};
    long[] r5 = d.getRange(2);
    assertEquals(l5[0], r5[0]);
    assertEquals(l5[1], r5[1]);
  }
  
  /**
   * goal get shard range 
   * 10,14, 
   * 15,19, 
   * 20,22
   */
  @Test
  public void testGetRange3() {
    int offset = 11;
    int limit = 5;
    Long finish = null;
    long total = 23;
    Sharding d = new Sharding();
    d.setLimit(offset, limit, finish);
    d.setCounts(total);
    int shards = d.getShards();
    
    assertEquals(3, shards);
    
    long[] l3 = {10l,14l};
    long[] r3 = d.getRange(0);
    assertEquals(l3[0], r3[0]);
    assertEquals(l3[1], r3[1]);
    
    long[] l4 = {15l,19l};
    long[] r4 = d.getRange(1);
    assertEquals(l4[0], r4[0]);
    assertEquals(l4[1], r4[1]);
    
    long[] l5 = {20l,22l};
    long[] r5 = d.getRange(2);
    assertEquals(l5[0], r5[0]);
    assertEquals(l5[1], r5[1]);
  }
  
  /**
   * goal get shard range 
   * 10,14, 
   * 15,19, 
   * 20,22
   */
  @Test
  public void testGetRange4() {
    int offset = 11;
    int limit = 5;
    Long finish = 21l;
    long total = 23;
    Sharding d = new Sharding();
    d.setLimit(offset, limit, finish);
    d.setCounts(total);
    int shards = d.getShards();
    
    assertEquals(3, shards);
    
    long[] l3 = {10l,14l};
    long[] r3 = d.getRange(0);
    assertEquals(l3[0], r3[0]);
    assertEquals(l3[1], r3[1]);
    
    long[] l4 = {15l,19l};
    long[] r4 = d.getRange(1);
    assertEquals(l4[0], r4[0]);
    assertEquals(l4[1], r4[1]);
    
    long[] l5 = {20l,20l}; // b/c of finish @21
    long[] r5 = d.getRange(2);
    assertEquals(l5[0], r5[0]);
    assertEquals(l5[1], r5[1]);
  }
  
  /**
   * goal get shard range 
   * 0,4
   */
  @Test
  public void testGetRange5() {
    int offset = 0;
    int limit = 5;
    Long finish = null;
    long total = 4;
    Sharding d = new Sharding();
    d.setLimit(offset, limit, finish);
    d.setCounts(total);
    int shards = d.getShards();
    assertEquals(1, shards);
    long[] l3 = {0l,3l};
    long[] r3 = d.getRange(0);
    assertEquals(l3[0], r3[0]);
    assertEquals(l3[1], r3[1]);
  }
  
  /**
   * goal get shard range 
   * 2,3
   */
  @Test
  public void testGetRange6() {
    int offset = 3;
    int limit = 5;
    Long finish = null;
    long total = 4;
    Sharding d = new Sharding();
    d.setLimit(offset, limit, finish);
    d.setCounts(total);
    int shards = d.getShards();
    assertEquals(1, shards);
    long[] l3 = {2l,3l};
    long[] r3 = d.getRange(0);
    assertEquals(l3[0], r3[0]);
    assertEquals(l3[1], r3[1]);
  }
  
  /**
   * goal get shard range 
   * 10,14, 
   * 15,19, 
   * 20,22
   */
  @Test
  public void testGetRange7() {
    int offset = 10;
    int limit = 5;
    Long finish = 21l;
    long total = 23;
    Sharding d = new Sharding();
    d.setLimit(offset, limit, finish);
    d.setCounts(total);
    int shards = d.getShards();
    assertEquals(3, shards);
    
    long[] l3 = {9l,13l};
    long[] r3 = d.getRange(0);
    assertEquals(l3[0], r3[0]);
    assertEquals(l3[1], r3[1]);
    
    long[] l4 = {14l,18l};
    long[] r4 = d.getRange(1);
    assertEquals(l4[0], r4[0]);
    assertEquals(l4[1], r4[1]);
    
    long[] l5 = {19,20l};
    long[] r5 = d.getRange(2);
    assertEquals(l5[0], r5[0]);
    assertEquals(l5[1], r5[1]);
  }
  
}
