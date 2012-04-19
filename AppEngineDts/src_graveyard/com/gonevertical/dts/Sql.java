package com.gonevertical.dts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


import com.gonevertical.dts.EntityType.Type;
import com.google.appengine.api.datastore.Entity;

public class Sql {
//
//  /**
//   * client factory utilities
//   */
//  private ClientFactory cf;
//
//  private TransformLib transLib;
//  private QueryLib queryLib;
//  private ColumnLib colLib;
//  
//  /**
//   * 
//   * @param clientFactory
//   */
//  public Sql(ClientFactory clientFactory) {
//    this.cf = clientFactory;
//    initSqlLibs();
//  }
//  
//  private void initSqlLibs() {
//    queryLib = QueryLibFactory.getLib(cf.getDatabaseData().getDatabaseType());
//    colLib = ColumnLibFactory.getLib(cf.getDatabaseData().getDatabaseType());
//    transLib = TransformLibFactory.getLib(cf.getDatabaseData().getDatabaseType());
//  }
//  
//  public String getTableName(String appId, Entity entity) {
//    String table = cf.getTableBase() + "" + appId + "_" + entity.getKind();
//    return table;
//  }
//  
//  private boolean doesTableExist(String appId, Entity entity) {
//    String table = getTableName(appId, entity);
//    return transLib.doesTableExist(cf.getDatabaseData(), table);
//  }
//  
//  public ArrayList<String> createTableAndColumns(String appId, Entity entity) {
//    String table = createTable(appId, entity);
//    if (table == null) {
//      return null;
//    }
//    return createColumns(table, entity);
//  }
//  
//  private String createTable(String appId, Entity entity) {
//    if (doesTableExist(appId, entity) == true) {
//      return null;
//    }
//    String table = getTableName(appId, entity);
//    String key = entity.getKey().getName();
//    transLib.createTable(cf.getDatabaseData(), table, key);
//    return table;
//  }
//  
//  /**
//   * 
//   * @param table
//   * @param entity
//   * @return
//   */
//  private ArrayList<String> createColumns(String table, Entity entity) {
//    Map<String, Object> p = entity.getProperties();
//    Set<String> l = p.keySet();
//    Iterator<String> itr = l.iterator();
//    ArrayList<String> a = new ArrayList<String>();
//    while(itr.hasNext()) {
//      String propertyName = itr.next();
//      Object o = entity.getProperty(propertyName);
//      createColumn(table, propertyName, o);
//    }
//    return a;
//  }
//  
//  public String createColumn(String table, String propertyName, Object o) {
//    String name = getColumnName(propertyName, o);
//    Type entityType = EntityType.getType(o);
//    ColumnData.Type columnType = ColumnData.Type.fromValue(entityType.getMySqlType());
//    ColumnData columnData = ColumnData.newInstance(table, name, columnType);
//    transLib.createColumn(cf.getDatabaseData(), columnData);
//    return name;
//  }
//  
//  public String getColumnName(String propertyName, Object o) {
//    Type entityType = EntityType.getType(o);
//    String name = propertyName + "_" + entityType.name();
//    return name;
//  }
  
}
