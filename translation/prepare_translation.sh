FILE=to_translate.txt
echo "Please tranlsate all text except for that inside the < and > brackets. Join http://chat.tripsit.me and ask jimmycarr or the team in #content if you have any questions. Thanks :)

<!--Store Description-->" > $FILE
cat english_store_description.txt >> $FILE
echo "
<!--Application Strings-->" >> $FILE
cat ../TripMobile/app/src/main/res/values/strings.xml | sed 's/^.*<!--.*getString.*-->.*$//g' | sed 's/.*">//g' | sed 's/<\/string>//g' | sed 's/\/string>//g' | sed 's/^ *//g' | sed 's/\\n//g' | sed 's/%1\$s/___/g' | sed 's/\\'/'/g' | sed 's/<\/item>$//g' | sed 's/<\/plurals>$//g' | sed 's/<!\[CDATA\[//g' | sed 's/\]\]>//g' | sed -n -e '/#######/,$p' | grep -B 1000000 '######' | sed 's/^.*########.*$//g' | sed 's/^.*USER FACING STRINGS.*$//g' | sed 's/^.*The following Strings are seen by the user and need translation and proper grammar.*$//g' | cat -s >> $FILE
