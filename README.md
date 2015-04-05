tripmobile
==========
An Android project for the Tripsit mobile app. If you have any suggestions or ideas to contribute, please add them to the [wiki](../../wiki) or raise an issue.

Build
==========
To build this project, you can either use Android Studio or the Android SDK and gradle from the command line.

## Keystore
* To sign the apk for release, you will need a copy of the releasekeys.keystore keystore.
* Inside the keystore, the key we are using is under an alias of "tripmobile"
* Please ask one of the admins for access to this keystore and its passwords if you intend to perform an official release.

## Android Studio
* Install [Android Studio IDE](https://developer.android.com/sdk/index.html#Other)
* Use the built in SDK manager to install the recommended packages below.
* The app can be built using "Build -> Generate Signed APK"

## Command line
* Install the latest version of [Gradle](https://gradle.org/downloads/). The binary only version should be sufficient.
* Add the `bin` folder to your path. For example, you might want to put this line in your `.bashrc` file: `export PATH=$PATH:<GRADLE_DIRECTORY>/bin`
* Type `gradle` to make sure gradle is installed. You should get an output similar to:
```
~$ gradle
:help

Welcome to Gradle 2.3.

To run a build, run gradle <task> ...

To see a list of available tasks, run gradle tasks

To see a list of command-line options, run gradle --help

To see more detail about a task, run gradle help --task <task>

BUILD SUCCESSFUL

Total time: 2.746 secs
```
* Download the [Android SDK Tools](https://developer.android.com/sdk/index.html#Other)
* Extract the folder somewhere and add the following environment variables:
```
export ANDROID_HOME=<ANDROID_SDK_DIRECTORY>
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
```
* Type `android list sdk --all` to see a list of available packages
* Type `android update sdk -u -a -t X` to install packages, where X is either a single package number, or a comma separated list of package numbers (i.e. 1,2,3,4)
* After installing the required packages (see below), the app can be built by running the [release.sh](release/release.sh) script. Documentation and usage can be found in comments inside the script.

## Recommended Android sdk packages
You will need the following packages to build the app:
* Android SDK Tools (latest)
* Android SDK Platform-tools (latest)
* Android SDK Build-tools, revision 21.1.2
* SDK Platform Android 5.0.1, API 21 (latest)
* Android Support Repository (latest)
* Android Support Library (latest)
