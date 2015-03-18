inkscape -h512 --export-background-opacity=0 --export-png=src/main/res/drawable-xxxhdpi/icon.png src/main/svg/icon.svg
HEIGHT=80
./pngconvert.sh -h$HEIGHT icon.svg
