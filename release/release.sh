#! /bin/bash
# --- Release Script ---
# This script is used to build a release version of the app
#
# Usage:
# release.sh major (update major version)
# release.sh minor (update minor version)
# release.sh build (update build version)
# release.sh       (release but keep current version)
#

IFS='*' # Change the IFS to prevent bash removing whitespace from variables
 
GRADLE_LOCATION=../TripMobile/app/build.gradle
MANIFEST_LOCATION=../TripMobile/app/src/main/AndroidManifest.xml

version_line=$(grep versionCode $GRADLE_LOCATION)
version_name_line=$(grep versionName $GRADLE_LOCATION)
version=$(echo $version_line | awk '{print $2}')
full_version=$(echo $version_name_line | awk '{print $2}' | sed 's/"//g')

major=$(echo $full_version | sed 's/\..*//')
minor=$(echo $full_version | sed 's/^[0-9]*\.//' | sed 's/\.[0-9]*$//')
build=$(echo $full_version | sed 's/[0-9]*\.[0-9]*\.//')

if [ $1 = 'major' ]
  then
    ((++major))
    minor=0
    build=0
elif [ $1 = 'minor' ]
  then
    ((++minor))
    build=0
elif [ $1 = 'build' ]
  then
    ((++build))
fi

new_version=$((version+1))
new_version_name=$major.$minor.$build
new_version_line=$(echo $version_line | sed "s/$version/$new_version/")
new_version_name_line=$(echo $version_name_line | sed "s/$full_version/$new_version_name/")

sed -i "s/$version_line/$new_version_line/" $GRADLE_LOCATION
sed -i "s/$version_name_line/$new_version_name_line/" $GRADLE_LOCATION

manifest_version_line=$(grep android:versionCode $MANIFEST_LOCATION)
manifest_version_name_line=$(grep android:versionName $MANIFEST_LOCATION)
new_manifest_version_line=$(echo $manifest_version_line | sed "s/$version/$new_version/")
new_manifest_version_name_line=$(echo $manifest_version_name_line | sed "s/$full_version/$new_version_name/")

sed -i "s/$manifest_version_line/$new_manifest_version_line/" $MANIFEST_LOCATION
sed -i "s/$manifest_version_name_line/$new_manifest_version_name_line/" $MANIFEST_LOCATION

unset IFS # Unset the temp value we stored in IFS
