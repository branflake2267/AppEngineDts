package com.gonevertical.dts;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.naming.Context;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.gonevertical.dts.data.DatabaseData;
import org.gonevertical.dts.lib.FileUtil;
import org.gonevertical.dts.lib.StringUtil;
import org.gonevertical.dts.lib.pooling.ManualSetupOfInitialPoolingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gonevertical.dts.utils.AppEngineUtils;
import com.gonevertical.dts.utils.SqlUtils;

public class ClientFactory {
  
  private static final Logger log = LoggerFactory.getLogger(ClientFactory.class);

  private PropertiesConfiguration config;
  
  private AppEngineUtils appEngineUtils;
  
  private DatabaseData databaseData;

  private String tableBase;
  
  private SqlUtils sql;

  private String appIdFull;
  private String appIdLeft;
  
  private Calendar calendar;

  public ClientFactory(File configurationFile) throws ConfigurationException, IOException {
   
    config = new PropertiesConfiguration(configurationFile);
    
    loginAppEngine();
    
    loginSqlServer();
    
    setTime();
  }
  
  private void setTime() {
    calendar = Calendar.getInstance();
  }
  
  public void setTime(Calendar calendar) {
    this.calendar = calendar;
  }

  public DatabaseData getDatabaseData() {
    return databaseData;
  }
  
  public AppEngineUtils getAppEngineUtils() {
    return appEngineUtils;
  }

  /**
   * login to app engine
   * @throws IOException
   */
  private void loginAppEngine() throws IOException {
    this.appIdFull = config.getString("appengine.applicationid");
    if (appIdFull == null) {
      log.error("appengine.applicationid=null please set an applicationid project.appspot.com");
    }
    String username = config.getString("appengine.username");
    if (username == null) {
      log.error("appengine.username=null please add a username");
    }
    String password = config.getString("appengine.password");
    if (password == null) {
      log.error("appengine.password=null please add a password");
    }
    appEngineUtils = AppEngineUtils.newInstance(username, password, appIdFull);
  }
  
  /**
   * login to sql server - setup pool
   * TODO put sql server type in properties
   */
  private void loginSqlServer() {
    if (downloadToSql() == false) {
      return;
    }
    String[] arr = config.getStringArray("sqlserver.param");
    HashMap<String, String> properties = StringUtil.createHashMap(arr, "=");
    databaseData = new DatabaseData(DatabaseData.TYPE_MYSQL, properties); 
    String tmpPath = config.getString("sqlserver.pooling.tmppath");
    ManualSetupOfInitialPoolingContext.registerContext(tmpPath, databaseData);
    sql = new SqlUtils(this);
  }
  
  public String getTableBase() {
    if (tableBase == null) {
      tableBase = config.getString("sqlserver.tablebase");
    }
    return tableBase;
  }
  
  public SqlUtils getSql() {
    return sql;
  }
  
  public String getAppId() {
    if (appIdLeft == null) {
      String[] a = appIdFull.split("\\.");
      appIdLeft = a[0];
    }
    return appIdLeft;
  }
   
  public boolean downloadToSql() {
    boolean b = false;
    b = config.getBoolean("download.to.sql");
    return b;
  }
  
  public boolean downloadToFile() {
    boolean b = false;
    b = config.getBoolean("download.to.file");
    return b;
  }

  public String getFilePath() {
    String s = config.getString("download.directory");
    if (s.matches(".*/") == false) {
      s = s + "/";
    }
    boolean b = FileUtil.createDirectory(s);
    if (b == true) {
      log.error("Could not created directories for file directory " + s);
    }
    return s;
  }

  /**
   * force finish
   * @return
   */
  public Long getFinish() {
    String s = config.getString("appengine.download.numberof.entities");
    if (s == null || s.trim().length() == 0) {
      return null;
    }
    Long l = null;;
    try {
      l = Long.parseLong(s);
    } catch (NumberFormatException e) {
      e.printStackTrace();
      log.error("appengine.download.numberof.entities as a number value");
    }
    return l;
  }

  public ArrayList<String> getDownloadEntities() {
    String[] a = config.getStringArray("appengine.download.entities");
    ArrayList<String> r = null;
    for (int i=0; i < a.length; i++) {
     if (r == null) {
       r = new ArrayList<String>();
     }
     if (a[i] != null) {
       r.add(a[i].trim());
     }
    }
    return r;
  }

  public int getShardLimit() { 
    int limit = 10;
    try {
      limit = config.getInt("appengine.download.shard.limit", 10);
    } catch (Exception e) {
      e.printStackTrace();
      log.warn("I couldn't figure out the shard limit from config file ", e);
    }
    return limit;
  }
  
  
  
  public String getDateTime() {
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String s = df.format(calendar.getTime());
    return s;
  }
  
  private String getDateTime(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String s = df.format(calendar.getTime());
    return s;
  }
  
  public String getFileName(String kind) {
    String sdate = getDateTime();
    String name = cleanDate(kind, sdate);
    return name;
  }
  
  public String getFileName(String kind, Date date) {
    String sdate = getDateTime(date);
    String name = cleanDate(kind, sdate);
    return name;
  }
  
  private String cleanDate(String kind, String sdate) {
    sdate = sdate.replaceAll(" ", "__");
    sdate = sdate.replaceAll(":", "-");
    String name = sdate + "___" + kind;
    return name;
  }
  
  public File getFile(String kind, Date date) {
    String name = getFileName(kind);
    String path = getFilePath() + name;
    File file = new File(path);
    return file;
  }
   
  
}
