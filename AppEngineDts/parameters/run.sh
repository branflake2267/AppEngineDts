#!/bin/sh
# created by Brandon Donnelson http://c.gwt-examples.com
# you can run this from cron
# 0 0   * * *   root   sh /path/to/config/run.sh # will run nightly at midnight

# make sure you edit the paths!!!
BASEPATH="/Users/branflake2267/Documents/workspace/AppEngineDts/parameters"
LOGGING_PATH="$BASEPATH/log4j.properties"
OPTIONS_PATH="$BASEPATH/options.properties"

# move to path with TransferAppEngineData.jar
cd $BASEPATH

echo "java  -classpath TransferAppEngineData.jar com.gonevertical.dts.Transfer -d=download -l=$LOGGING_PATH -o=$OPTIONS_PATH"
java  -jar TransferAppEngineData.jar com.gonevertical.dts.Transfer -d=download -l=$LOGGING_PATH -o=$OPTIONS_PATH