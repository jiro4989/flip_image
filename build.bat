@echo off

kotlinc-jvm %1.kt -include-runtime -d %1.jar
