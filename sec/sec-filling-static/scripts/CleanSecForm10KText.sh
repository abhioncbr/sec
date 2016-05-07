#!/bin/bash

if [ $# != 2 ]; then
    echo "Required arguments are not provided. Two Arguments needed."
    exit 1
fi

SEC_INPUT_HTML_FILE=$1
SEC_OUTPUT_TEXT_FILE=$2

echo "Going to execute clean text utility for : $SEC_INPUT_HTML_FILE"
links2 -dump -width 300 $SEC_INPUT_HTML_FILE > $SEC_OUTPUT_TEXT_FILE
echo "Executed Successfully."
