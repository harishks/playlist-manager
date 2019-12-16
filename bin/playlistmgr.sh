#!/usr/bin/env bash

if [ $# -gt 3 ];
then
    echo 'USAGE: bin/playlistmgr.sh inp.json changes.json out.json'
	exit 1
fi
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
cd $DIR/..
java -jar out/artifacts/playlistconversions_jar/playlistconversions.jar $@
echo "Changes have been successfully applied - please checkout \"out.json\" for the transformed mixtape file"