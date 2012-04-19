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
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityTranslator;
import com.google.appengine.repackaged.com.google.common.util.Base64;
import com.google.storage.onestore.v3.OnestoreEntity.EntityProto;

public class DownloadToFile {

  private static final Logger log = LoggerFactory.getLogger(DownloadToFile.class);

  /**
   * client factory utilities
   */
  private ClientFactory cf;

  /**
   * writer
   */
  private BufferedWriter out;
  
  /**
   * 
   * @param clientFactory
   */
  public DownloadToFile(ClientFactory clientFactory) {
    this.cf = clientFactory;
  }
  
  public boolean openFile(String kind) {
    String name = getName(kind);
    String path = cf.getFilePath() + name;
    File file = new File(path);
    try {
      out = new BufferedWriter(new FileWriter(file, false));
      log.info("creating file=" + file.toString());
    } catch (IOException e) {
      e.printStackTrace();
      log.error("Is the file writable? I could not setup file writer for file=" + file.toString(), e);
      return false;
    }    
    return true;
  }

  private String getName(String kind) {
    return cf.getFileName(kind);
  }

  public void closeFile() {
    try {
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void saveToFile(Entity entity) {
    if (entity == null) {
      return;
    }
    String json = ObjectUtils.convertObjectToString(entity);
    writeToFile(json);
  }
  
  private void writeToFile(String s) {
    try {
      log.info("writeToFile=" + s);
      out.write(s + "\n");
    } catch (IOException e) {
      e.printStackTrace();
      log.error("Oops, I can't write to the file anymore.", e);
    }
  }
  
}
