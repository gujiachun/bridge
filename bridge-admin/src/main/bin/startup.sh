#!/bin/bash

current_path=`pwd`
case "`uname`" in
    Linux)
		bin_abs_path=$(readlink -f $(dirname $0))
		;;
	*)
		bin_abs_path=`cd $(dirname $0); pwd`
		;;
esac
base=${bin_abs_path}/..
echo ${base}

export LANG=en_US.UTF-8
export BASE=$base

if [ ! -d $base/logs ] ; then
	mkdir -p $base/logs
fi

## set java path
if [ -z "$JAVA" ] ; then
  JAVA=$(which java)
fi

#custom java sdk path
CUSTOM_JAVA="/usr/bridge/java/bin/java"
if [ -z "$JAVA" ]; then
  if [ -f $CUSTOM_JAVA ] ; then
  	JAVA=$CUSTOM_JAVA
  else
  	echo "Cannot find a Java JDK. Please set either set JAVA or put java (>=1.5) in your PATH." 2>&2
    exit 1
  fi
fi

#default env params
ENV_OPTS="-Dspring.profiles.active=dev"

#default java params
JAVA_OPTS="-server -Xms2048m -Xmx2048m"
JAVA_OPTS="$JAVA_OPTS -XX:+UseG1GC -XX:MaxGCPauseMillis=250 -XX:+UseGCOverheadLimit -XX:+ExplicitGCInvokesConcurrent"
JAVA_OPTS="$JAVA_OPTS -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Dfile.encoding=UTF-8"
echo $JAVA_OPTS
echo "----------"

case "$#"
in
0 )
  ;;
1 )
  if [ -n "$1" ]; then
   ENV_OPTS="-Dspring.profiles.active="$1
  fi
  ;;
2 )
  echo "启动参数1:$1"
  echo "启动参数2:$2"
  if [ -n "$1" ]; then
   ENV_OPTS="-Dspring.profiles.active="$1
  fi

  if [ -n "$2" ];then
    if [ "$2" != "default" ];then
     JAVA_OPTS="$2"
    fi
  fi
  ;;
* )
  echo "THE PARAMETERS MUST BE TWO OR LESS.PLEASE CHECK AGAIN."
  exit;;
esac

#bridge admin params
BRIDGE_OPTS="-DappName=bridge-admin -Ddruid.mysql.usePingMethod=false"
echo "================================"

echo "env params:"${ENV_OPTS}
echo "java params:"${JAVA_OPTS}

for i in $base/lib/*;
    do CLASSPATH=$i:"$CLASSPATH";
done

CLASSPATH="$base/conf:$CLASSPATH";

echo "cd to $bin_abs_path for workaround relative path"
cd $bin_abs_path

echo CLASSPATH :$CLASSPATH

$JAVA $JAVA_OPTS $BRIDGE_OPTS ${ENV_OPTS} -classpath .:$CLASSPATH com.rainbow.bridge.admin.BridgeAdminApplication


