#!/bin/sh

classpath="`dirname $0`/bin"
java -cp $classpath termfrequncy.TermFrequencyRunner "$@"
