package com.gonevertical.dts.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.gonevertical.dts.data.ColumnData;
import org.gonevertical.dts.lib.sql.columnlib.ColumnLib;
import org.gonevertical.dts.lib.sql.columnmulti.ColumnLibFactory;
import org.gonevertical.dts.lib.sql.querylib.MySqlQueryLib;
import org.gonevertical.dts.lib.sql.querylib.QueryLib;
import org.gonevertical.dts.lib.sql.querymulti.QueryLibFactory;
import org.gonevertical.dts.lib.sql.transformlib.MySqlTransformLib;
import org.gonevertical.dts.lib.sql.transformlib.TransformLib;
import org.gonevertical.dts.lib.sql.transformmulti.TransformLibFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gonevertical.dts.ClientFactory;
import com.gonevertical.dts.data.EntityType;
import com.gonevertical.dts.data.EntityType.Type;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityTranslator;
import com.google.storage.onestore.v3.OnestoreEntity.EntityProto;

public class SqlUtils {

  private static final Logger log = LoggerFactory.getLogger(SqlUtils.class);

  public static final String COLUMNALLDATA = "DKDATA_BLOB";
  public static final String COLUMNDATE = "DKDATE_DATETIME";
  public static final String COLUMNKEY = "DKKEY_VARCHAR";
  
  /**
   * client factory utilities
   */
  private ClientFactory cf;

  private TransformLib transLib;
  private QueryLib queryLib;
  private ColumnLib colLib;
  
  /**
   * 
   * @param clientFactory
   */
  public SqlUtils(ClientFactory clientFactory) {
    this.cf = clientFactory;
    initSqlLibs();
  }
  
  private void initSqlLibs() {
    try {
      queryLib = QueryLibFactory.getLib(cf.getDatabaseData().getDatabaseType());
      colLib = ColumnLibFactory.getLib(cf.getDatabaseData().getDatabaseType());
      transLib = TransformLibFactory.getLib(cf.getDatabaseData().getDatabaseType());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public String getTableName(Entity entity) {
    String table = cf.getTableBase() + "" + cf.getAppId() + "_" + entity.getKind().toLowerCase();
    return table;
  }
  
  public String getTableName(String kind) {
    String table = cf.getTableBase() + "" + cf.getAppId() + "_" + kind.toLowerCase();
    return table;
  }
  
  private boolean doesTableExist(Entity entity) {
    String table = getTableName(entity);
    return transLib.doesTableExist(cf.getDatabaseData(), table);
  }
  
  public HashSet<String> createTableAndColumns(Entity entity) {
    String table = createTable(entity);
    if (table == null) {
      return null;
    }
    return createColumns(table, entity);
  }
  
  private String createTable(Entity entity) {
    if (doesTableExist(entity) == true) {
      return getTableName(entity);
    }
    String table = getTableName(entity);
    transLib.createTable(cf.getDatabaseData(), table, "id");
    return table;
  }
  
  /**
   * 
   * @param table
   * @param entity
   * @return
   */
  private HashSet<String> createColumns(String table, Entity entity) {
    Map<String, Object> p = entity.getProperties();
    Set<String> l = p.keySet();
    Iterator<String> itr = l.iterator();
    
    HashSet<String> a = new HashSet<String>();
    a.add(createDateColumn(table, entity));
    a.add(createKeyColumn(table, entity));
    a.add(createAllDataColumn(table, entity));
    
    while(itr.hasNext()) {
      String propertyName = itr.next();
      Object o = entity.getProperty(propertyName);
      createColumn(entity, table, propertyName, o);
    }
    return a;
  }
  
  private String createDateColumn(String table, Entity entity) {
    String name = COLUMNDATE;
    ColumnData.Type columnType = ColumnData.Type.DATETIME;
    ColumnData columnData = ColumnData.newInstance(table, name, columnType);
    columnData.setIdentity(true);
    transLib.createColumn(cf.getDatabaseData(), columnData);
    return name;
  }
  
  private String createKeyColumn(String table, Entity entity) {
    String name = COLUMNKEY;
    ColumnData.Type columnType = ColumnData.Type.VARCHAR;
    ColumnData columnData = ColumnData.newInstance(table, name, columnType);
    columnData.setCharLength(255);
    columnData.setIdentity(true);
    transLib.createColumn(cf.getDatabaseData(), columnData);
    return name;
  }
  
  /**
   * create a main column
   * @param table
   * @param entity
   */
  private String createAllDataColumn(String table, Entity entity) {
    String name = COLUMNALLDATA;
    ColumnData.Type columnType = ColumnData.Type.BLOB;
    ColumnData columnData = ColumnData.newInstance(table, name, columnType);
    transLib.createColumn(cf.getDatabaseData(), columnData);
    return name;
  }

  public String createColumn(Entity e, String table, String propertyName, Object o) {
    if (o == null) {
      // skip it here and pick it up on a double check.
      return null;
    }
    String name = getColumnName(propertyName, o);
    ColumnData.Type type = getColumnType(o);
    ColumnData columnData = ColumnData.newInstance(table, name, type);
    if (type.getLeft() != null) {
      columnData.setCharLength(type.getLeft());
    } 
    if (type.getRight() != null) {
      columnData.setDecimalPositionLeft(type.getLeft());
      columnData.setDecimalPositionRight(type.getRight());
    }
    transLib.createColumn(cf.getDatabaseData(), columnData);
    return name;
  }
  
  public ColumnData.Type getColumnType(Object o) {
    Type entityType = EntityType.getType(o);
    ColumnData.Type columnType = ColumnData.Type.fromValue(entityType.getMySqlType());
    return columnType;
  }
  
  public String getColumnName(String propertyName, Object o) {
    Type entityType = EntityType.getType(o);
    String name = propertyName + "_" + entityType.name();
    return name;
  }

  public void saveColumnData(long index, Entity entity, ArrayList<ColumnData> cs) {
    ColumnData[] columnData = new ColumnData[cs.size()];
    cs.toArray(columnData);
    
    String whereSql = colLib.getSql_IdentitiesWhere(columnData);
    String columnsql = colLib.getSql(columnData);
    
    String sql = null;
    if (exist(columnData, whereSql) == true) {
      sql = "UPDATE " + columnData[0].getTable() + " SET " + columnsql + " WHERE " + whereSql;
    } else {
      sql = "INSERT INTO " + columnData[0].getTable() + " SET " + columnsql; 
    }
    log.info(index + ". Key=" + entity.getKey().toString() + " -> sql=" + sql);
    queryLib.update(cf.getDatabaseData(), sql);
  }

  private boolean exist(ColumnData[] columnData, String whereSql) {
    //ColumnData key = colLib.getPrimaryKey_ColumnData(columnData);
    String sql = "SELECT COUNT(*) AS Total FROM `" + columnData[0].getTable() + "` WHERE " + whereSql;
    boolean b = queryLib.queryBoolean(cf.getDatabaseData(), sql);
    return b;
  }
  
  public QueryLib getQueryLib() {
    return queryLib;
  }

  public TransformLib getTransLib() {
    return transLib;
  }
  
}
