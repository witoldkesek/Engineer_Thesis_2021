#!/bin/bash
#
# ClassVisualizer start script.
#
DIR=`dirname "$0"`
APP_PATH="$DIR/clsvis.jar"

#
# Choose java - use JAVA_HOME, if set.
#
if [ "$JAVA_HOME" != "" ]; then
	JAVA="$JAVA_HOME/bin/java"
else
	JAVA="java"
fi

#
# Choose Look and Feel.
#
# modern one - Nimbus
LAF="javax.swing.plaf.nimbus.NimbusLookAndFeel"
#LAF="com.sun.java.swing.plaf.gtk.GTKLookAndFeel"
# No explicit settings - java default (Ocean)
# OS X - java default = system default, use screen menu bar
if [ `uname` == "Darwin" ]; then
    LAF=""
    OPTS="$OPTS -Dapple.laf.useScreenMenuBar=true"
fi

#
# Final settings.
#
# Look and Feel
if [ "$LAF" != "" ]; then
	OPTS="$OPTS -Dswing.defaultlaf=$LAF"
fi
# Font settings
OPTS="$OPTS -Dawt.useSystemAAFontSettings=on"
# Memory
OPTS="$OPTS -Xms16m -Xmx1g -XX:+UseSerialGC"
# Additional parameters
#OPTS="$OPTS -verbose:gc"
# Logging
OPTS="$OPTS -Djava.util.logging.config.file=logging.properties"

#
# Run.
#
CMD="$JAVA $OPTS -jar $APP_PATH $@"
echo "$CMD"
$CMD
