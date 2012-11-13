@echo off
protoc --version
protoc -I../../main/src --java_out=../../main/src ../../main/src/org/iremake/common/network/messages/messages.proto
pause