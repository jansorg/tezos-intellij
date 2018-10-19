#!/usr/bin/env bash

DIR="$PWD/src/main/resources/icons"

cd "$DIR"

rm -f stack*.png
for i in stack*.svg; do
    echo "$i"
    if [[ "$i" == *"_dark"*  ]]; then
        inkscape -w 16 -h 16 -e "$(basename -s '_dark.svg' "$i")_dark.png" "$i"
        inkscape -w 32 -h 32 -e "$(basename -s '_dark.svg' "$i")@2x_dark.png" "$i"
    else
        inkscape -w 16 -h 16 -e "$(basename -s '.svg' "$i").png" "$i"
        inkscape -w 32 -h 32 -e "$(basename -s '.svg' "$i")@2x.png" "$i"
    fi
done