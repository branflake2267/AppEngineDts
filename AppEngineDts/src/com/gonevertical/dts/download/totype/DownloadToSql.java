package com.gonevertical.dts.download.totype;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

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
import com.gonevertical.dts.utils.ObjectUtils;
import com.gonevertical.dts.utils.SqlUtils;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityTranslator;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.repackaged.com.google.common.util.Base64;
import com.google.appengine.tools.remoteapi.RemoteApiInstaller;
import com.google.storage.onestore.v3.OnestoreEntity.EntityProto;

public class DownloadToSql {

  private static final Logger log = LoggerFactory.getLogger(DownloadToSql.class);

  /**
   * client factory utilities
   */
  private ClientFactory cf;

  /**
   * sql table
   */
  private String table;
  
  /**
   * 
   * @param clientFactory
   */
  public DownloadToSql(ClientFactory clientFactory) {
    this.cf = clientFactory;
  }
  
  /**
   * has the tables and columns been created in this thread
   */
  private boolean createdTablesAndColumns;
  
  /**
   * what column names have been created
   */
  private HashSet<String> columnNamesCreated;

  private void saveToSql(long index, Entity entity) {
    if (createdTablesAndColumns == false) {
      createSqlTableAndColumns(entity);
      createdTablesAndColumns = true;
    }
    
    // double check and create columns if need be
    doubleCheckSqlColumns(entity);
    
    ArrayList<ColumnData> cs = new ArrayList<ColumnData>();
    cs.add(getDateColumn());
    cs.add(getKey(entity));
    cs.add(getMainAllDataColumn(entity));
    
    // iterate the properties 
    Map<String, Object> properties = entity.getProperties();
    Set<Entry<String, Object>> es = properties.entrySet();
    Iterator<Entry<String, Object>> itr = es.iterator();
    while(itr.hasNext()) {
      Entry<String, Object> entry = itr.next();
      ColumnData c = getColumnDataWithValues(entity, entry);
      if (c != null) {
        cs.add(c);
      }
    }
    
    saveColumnData(index, entity, cs);
  }
  
  private ColumnData getKey(Entity entity) {
    String value = entity.getKey().toString();
    ColumnData c = ColumnData.newInstance(table, SqlUtils.COLUMNKEY, ColumnData.Type.VARCHAR);
    c.setCharLength(255);
    c.setIdentity(true);
    c.setValue(value);
    return c;
  }

  private ColumnData getMainAllDataColumn(Entity entity) {
    ColumnData c = ColumnData.newInstance(table, SqlUtils.COLUMNALLDATA, ColumnData.Type.BLOB);
    //EntityProto ep = EntityTranslator.convertToPb(entity);
    String json = ObjectUtils.convertObjectToString(entity);
    c.setValue(json);
    return c;
  }

  private ColumnData getDateColumn() {
    ColumnData c = ColumnData.newInstance(table, SqlUtils.COLUMNDATE, ColumnData.Type.DATETIME);
    c.setValue(cf.getDateTime());
    c.setIdentity(true);
    return c;
  }

  private void saveColumnData(long index, Entity entity, ArrayList<ColumnData> cs) {
    cf.getSql().saveColumnData(index, entity, cs);
  }

  private ColumnData getColumnDataWithValues(Entity entity, Entry<String, Object> entry) {
    String propertyName = entry.getKey();
    Object o = entry.getValue();
    String columnName = cf.getSql().getColumnName(propertyName, o);
    if (columnNamesCreated.contains(columnName) == false) {
      // skip until the column has been created in mysql
      return null;
    }
    ColumnData.Type type = cf.getSql().getColumnType(o);
    ColumnData c = ColumnData.newInstance(table, columnName, type);
    c.setValue(getSqlValue(entity, propertyName, o));
    return c;
  }

  private String getSqlValue(Entity entity, String propertyName, Object o) {
    String s = EntityType.getValue(o);
    return s;
  }

  private void createSqlTableAndColumns(Entity entity) {
    table = cf.getSql().getTableName(entity);
    columnNamesCreated = cf.getSql().createTableAndColumns(entity);
  }

  private void doubleCheckSqlColumns(Entity entity) {
    Map<String, Object> properties = entity.getProperties();
    Set<Entry<String, Object>> es = properties.entrySet();
    Iterator<Entry<String, Object>> itr = es.iterator();
    while(itr.hasNext()) {
      Entry<String, Object> e = itr.next();
      String propertyName = e.getKey();
      Object o = e.getValue();
      if (exist(propertyName, o) == false) {
        createColumn(entity, propertyName, o);
      }
    }
  }
  
  private void createColumn(Entity entity, String propertyName, Object o) {
    String columnName = cf.getSql().createColumn(entity, cf.getSql().getTableName(entity), propertyName, o);
    if (columnName != null) {
      columnNamesCreated.add(columnName);
    }
  }

  private boolean exist(String propertyName, Object o) {
    if (o == null) {
      // will purge if it really doesn't exist
      return true;
    }
    String name = cf.getSql().getColumnName(propertyName, o);
    boolean exists = columnNamesCreated.contains(name); 
    return exists;
  }

  
  
  
  
}
