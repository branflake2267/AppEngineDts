package com.gonevertical.dts;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;


import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gonevertical.dts.download.DownloadKindsManager;


public class Transfer {
  
  private static final Logger log = LoggerFactory.getLogger(Transfer.class);

  /**
   * TODO
   * -d=[downlod|upload] - download or upload data according to properties file
   * -l=[path] - path to logging properties file
   * -o=[path] - path to parameters file
   * 
   * @param args
   */
  public static void main(String[] args) {
    
    if (args == null) {
      echoArgs();
      return;
    }
    
   String log4jPath = getLog4JPath(args);
   if (log4jPath == null) {
     log.warn("Warning no log4J properties file path was provided. I will use my default. You can add it by using -l=[/path/to/log4j.properties] - log4j.properties file path.");
   }
   
   String optionsPath = getOptionsPath(args);
    
   if (optionsPath == null) {
     log.error("No options path was provided. Please add -o=[/path/to/options.properties] - options.properties options that run this program.");
     return;
   }
   
   String direction = getDirection(args);
   if (direction == null) {
     log.error("No data direction was provided. -d=[download|upload] - download or upload data using -d");
     return;
   }
   
   Transfer dt = new Transfer();
   dt.setLog4Jproperties(log4jPath);
   dt.setOptions(optionsPath);
   dt.setDirection(direction);
   dt.run();
   
  }

  private static String getDirection(String[] args) {
    String s = getOptions(args, "-d");
    return s;
  }

  private static String getOptionsPath(String[] args) {
    String s = getOptions(args, "-o");
    return s;
  }

  private static String getLog4JPath(String[] args) {
    String s = getOptions(args, "-l");
    return s;
  }
  
  private static String getOptions(String[] args, String command) {
    String s = null;
    for (int i=0; i < args.length; i++) {
      String v = args[i];
      if (v.matches(command + ".*") == true) {
        s = args[i].replaceFirst(command + "=", "");
      }
    }
    return s;
  }

  private static void echoArgs() {
    System.out.println("TransferData -d=[download|upload] -l=[/path/to/log4j.properties] -p=[/path/to/options.properties]");
    System.out.println("-d=[download|upload] - download or upload data using -d");
    System.out.println("-l=[/path/to/log4j.properties] - log4j.properties file path");
    System.out.println("-o=[/path/to/options.properties] - options.properties options that run this program");
  }

  private ClientFactory cf;
  
  private String log4jpath = "/Users/branflake2267/Documents/workspace/AppEngineDts/parameters/log4j.properties";
  
  private String optionsPath = "/Users/branflake2267/Documents/workspace/AppEngineDts/parameters/options.properties";

  private String direction;
  
  public Transfer() {
  }
  
  private void initClientFactory(String path) throws ConfigurationException, IOException {
    File configurationFile = new File(path);
    cf = new ClientFactory(configurationFile);
  }
  
  public void setLog4Jproperties(String path) {
    log4jpath = path;
  }
  
  public void setOptions(String path) {
    optionsPath = path;
  }
  
  public void setDirection(String direction) {
    this.direction = direction;
  }

  private void run() {
    
    PropertyConfigurator.configure(log4jpath);
    
    log.info("Starting Data Transfer");
    
    String configfilepath = optionsPath;
    try {
      initClientFactory(configfilepath);
    } catch (ConfigurationException e) {
      log.error("I couldn't find or read the configuration file. Please correct the confuration file.", e);
      e.printStackTrace();
      return;
      
    } catch (IOException e) {
      log.error("I couldn't log into app engine project. Please change the username and password?", e);
      e.printStackTrace();
      return;
    }
    
    if (direction == null) {
      log.error("No data direction was given. set the setDirection(direction)");
      log.error("Exiting.");
      return;
    }
    
    if (direction.contains("download") == true) {
      DownloadKindsManager dkm = new DownloadKindsManager(cf);
      dkm.run();  
      
    } else if (direction.contains("upload") == true) {
      
    }
    
  }

  public boolean test() {

    PropertyConfigurator.configure(log4jpath);
    
    log.info("Starting Test");
    
    String configfilepath = optionsPath;
    try {
      initClientFactory(configfilepath);
    } catch (ConfigurationException e) {
      log.error("I couldn't find or read the configuration file. Please correct the confuration file.", e);
      e.printStackTrace();
      return false;
      
    } catch (IOException e) {
      log.error("I couldn't log into app engine project. Please change the username and password?", e);
      e.printStackTrace();
      return false;
    }
    
    if (direction == null) {
      log.error("No data direction was given. set the setDirection(direction)");
      log.error("Exiting.");
      return false;
    }
    
    log.info("Finished Test");
    
    return true;
  }
  
  public ClientFactory getClientFactory() {
    return cf;
  }
  
  
}
