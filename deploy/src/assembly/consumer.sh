#!/bin/sh

cd $(dirname $0)
java -Dfile.encoding=UTF8 -cp ./lib/*: io.joynr.demo.MyRadioConsumerApplication test
