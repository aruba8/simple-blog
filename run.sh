#!/bin/sh
export BLOG_DIR=${PWD}/static
mvn clean compile exec:java -Dexec.mainClass=blog.BlogController