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

if [[ -z $KEYSTORE_PASSWORD ]]; then
  read -s -p "Keystore password: " KEYSTORE_PASSWORD
  echo ""
fi
if [[ -z $KEY_PASSWORD ]]; then
  read -s -p "Key password: " KEY_PASSWORD
  echo ""
fi

KEYSTORE_LOCATION="../TripMobile/releasekeys.keystore"
if [[ -z $REMOTE_KEYSTORE_LOCATION && ! -f $KEYSTORE_LOCATION ]]; then
  echo "You need a keystore to run the release script, sorry."
  exit 1
fi

if [[ ! -z $REMOTE_KEYSTORE_LOCATION ]]; then
  wget --quiet -O $KEYSTORE_LOCATION $REMOTE_KEYSTORE_LOCATION
  if [[ $? -ne 0 ]]; then
    echo "Could not retrieve keystore from remote location, sorry."
    exit 1
  fi
fi

export KEYSTORE_PASSWORD=$KEYSTORE_PASSWORD
export KEY_PASSWORD=$KEY_PASSWORD

GRADLE_DIRECTORY=../TripMobile/app
GRADLE_LOCATION=$GRADLE_DIRECTORY/build.gradle
MANIFEST_LOCATION=../TripMobile/app/src/main/AndroidManifest.xml

version_line=$(grep versionCode $GRADLE_LOCATION)
version_name_line=$(grep versionName $GRADLE_LOCATION)
version=$(echo $version_line | awk '{print $2}')
full_version=$(echo $version_name_line | awk '{print $2}' | sed 's/"//g')

major=$(echo $full_version | sed 's/\..*//')
minor=$(echo $full_version | sed 's/^[0-9]*\.//' | sed 's/\.[0-9]*$//')
build=$(echo $full_version | sed 's/[0-9]*\.[0-9]*\.//')

new_version=$version
if [ $# -gt 0 ]
  then
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
  ((++new_version))
fi

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

if (cd ../TripMobile && ./gradlew assembleRelease && cd -) then
  echo $?
  cp ../TripMobile/app/build/outputs/apk/app-release.apk TripMobile-$new_version_name.apk
  echo "Apk saved as TripMobile-$new_version_name.apk"
  echo "Don't commit this. Git is not made for binary files."
else
  echo "Failed to build. Sorry!"
  exit 1
fi

unset KEYSTORE_PASSWORD
unset KEY_PASSWORD
unset IFS # Unset the temp value we stored in IFS
