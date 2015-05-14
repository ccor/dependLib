#!/bin/sh

DEPEND_CONF="depend.conf"
CURRENT_PATH=`pwd`

echo
echo ===========================
echo
echo     depend copy.
echo     DEPEND_LIB_HOME=$DEPEND_LIB_HOME
echo     CURRENT_PATH=$CURRENT_PATH
echo
echo ===========================

if [ -d "$DEPEND_LIB_HOME" ]
then
  if [ -f "$DEPEND_CONF" ]
  then
    while read LINE
    do
      case $LINE in
        \#*) ;;
        *)
           echo $LINE
           cmd=`echo $LINE| awk -F ">" '{print $1 $2}'`
           cp ${DEPEND_LIB_HOME}/${cmd}
           if [ $? == 0 ]; then
             echo "ok."
           fi
           ;;
      esac
    done < $DEPEND_CONF
  else
    echo "configfile miss:"
    echo "${CURRENT_PATH}/${DEPEND_CONF} is not exist."
  fi
else
  echo "DEPEND_LIB_HOME is not defined."
fi

echo finish.

