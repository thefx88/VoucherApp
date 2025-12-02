#!/usr/bin/env sh

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS="-Xmx64m -Xms64m"

APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD="maximum"

warn () {
    echo "$*"
}

die () {
    echo
    echo "$*"
    echo
    exit 1
}

# OS specific support.
# Add other OSes here.
cygwin=false
msys=false
darwin=false
nonstop=false
case "`uname`" in
  CYGWIN* )
    cygwin=true
    ;;
  Darwin* )
    darwin=true
    ;;
  MSYS* | MINGW* )
    msys=true
    ;;
  NONSTOP* )
    nonstop=true
    ;;
  * )
    ;;
esac

CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

if $cygwin ; then
    APP_HOME=`cygpath --path --mixed "$APP_HOME"`
    CLASSPATH=`cygpath --path --mixed "$CLASSPATH"`
    JAVACMD=`cygpath --unix "$JAVACMD"`
fi

# Escape application args
a_argv=()
for arg in "$@"; do
  a_argv+=("$(printf '%s\n' "$arg" | sed "s/'/'\\''/g")")
done
APP_ARGS=("${a_argv[@]}")

# Collect all arguments for the java command, following the shell quoting and substitution rules
JVM_OPTS=()
for opt in ${DEFAULT_JVM_OPTS}; do
  JVM_OPTS+=("$opt")
done

JAVA_OPTS_ARRAY=()
for opt in ${JAVA_OPTS}; do
  JAVA_OPTS_ARRAY+=("$opt")
done

GRADLE_OPTS_ARRAY=()
for opt in ${GRADLE_OPTS}; do
  GRADLE_OPTS_ARRAY+=("$opt")
done

CMD_ARGS=()
for arg in "${APP_ARGS[@]}"; do
  CMD_ARGS+=("$arg")
done

# Add default JVM options if not set by environment
for opt in "${JAVA_OPTS_ARRAY[@]}" "${GRADLE_OPTS_ARRAY[@]}"; do
  JVM_OPTS+=("$opt")
done

# Locate java binary
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        # IBM's JDK on AIX uses strange locations for the executables
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME\n\nPlease set the JAVA_HOME variable in your environment to match the location of your Java installation."
    fi
else
    JAVACMD="java"
fi

if [ ! -x "$JAVACMD" ] ; then
    die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.\n\nPlease set the JAVA_HOME variable in your environment to match the location of your Java installation."
fi

# Increase the maximum file descriptors if we can.
if ! $cygwin && ! $darwin && ! $nonstop ; then
    MAX_FD_LIMIT=`ulimit -H -n`
    if [ $? -eq 0 ] ; then
        if [ "$MAX_FD" = "maximum" ] || [ "$MAX_FD" = "max" ] ; then
            MAX_FD="$MAX_FD_LIMIT"
        fi
        ulimit -n $MAX_FD
        if [ $? -ne 0 ] ; then
            warn "Could not set maximum file descriptor limit: $MAX_FD"
        fi
    fi
fi

# For Darwin, add options to specify how the application appears in the dock
if $darwin ; then
    GRADLE_OPTS_ARRAY+=("-Xdock:name=$APP_NAME")
    GRADLE_OPTS_ARRAY+=("-Xdock:icon=$APP_HOME/media/gradle.icns")
fi

# Avoid recursive xargs call on Linux
if [ -x /usr/bin/cygpath ] ; then
    CLASSPATH=`cygpath --path --mixed "$CLASSPATH"`
fi

exec "$JAVACMD" "${JVM_OPTS[@]}" "${GRADLE_OPTS_ARRAY[@]}" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "${CMD_ARGS[@]}"
