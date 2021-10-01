#!/bin/bash
set -e

sh $KAFKA_HOME/bin/connect-standalone.sh /config/connect-standalone.properties  etc/couchbase-source.properties

exec "$@"
