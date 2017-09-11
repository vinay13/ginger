#!/bin/sh

# Script: gif-optimize.sh
# Author: Rob Baier
# Description: Script used to optimize animated GIF files.
# Dependencies: Gifsicle (https://github.com/kohler/gifsicle)
# Environment: Tested on MacOS 10.12. Other *nix environments may require some tweaking.
# Usage: ./gif-optimize.sh input-filename.gif

# Helper function to convert bytes into a human-readable format
bytesReadable() {
    b=${1:-0}; d=''; s=0; S=(Bytes {K,M,G,T,E,P,Y,Z}iB)
    while ((b > 1024)); do
        d="$(printf ".%02d" $((b % 1024 * 100 / 1024)))"
        b=$((b / 1024))
        let s++
    done
    echo "$b$d ${S[$s]}"
}

# The output filename will have "-min.gif" appended automatically
INPUT_FILE=$1
INPUT_FILEINFO=`gifsicle -I $1`
INPUT_FILESIZE=`cat $1 | wc -c`
INPUT_FILESIZE_READABLE=`bytesReadable $INPUT_FILESIZE`
FILENAME="${INPUT_FILE%.*}"
OUTPUT_FILE=$1

# The delay factor determines how many frames will be removed and what the new frame delay will be
# in order to maintain the same animation speed as the original file. A delay factor of 2 will remove
# every other frame and double the frame delay in the new file. Increasing this number will drop more
# frames and reduce the overall filesize, but will result in a more choppy animation.
DELAY_FACTOR=3

# Set the number of colors in the output file
COLOR_DEPTH=128

# Get the number of frames from the original file
NUM_FRAMES=`echo "$INPUT_FILEINFO" | grep -E "[0-9]+ images" --only-matching | grep -E "[0-9]+" --only-matching`

echo "$INPUT_FILEINFO"
# Get the frame delay from the original file
DELAY=`echo "$INPUT_FILEINFO" | grep -E "delay [0-9].+" --only-matching | grep -E "[0-9].+" --only-matching | tail -1 | rev | cut -c 2- | rev`

# Get the color depth from the original file
INPUT_COLORS=`echo "$INPUT_FILEINFO" | grep "color" | grep -oE "[0-9]." --only-matching`

# Calculate new delay based on original delay and the new delay factor
NEW_DELAY=$(expr $DELAY*100*$DELAY_FACTOR | bc)
echo "----- $DELAY"
NEW_DELAY=${NEW_DELAY%%.*}
echo "$DELAY $NEW_DELAY"
echo $COLOR_DEPTH
echo "----- $NEW_DELAY"
FILE_SIZE=`wc -c < $INPUT_FILE`
if [ -z "$DELAY" ]; then
	echo "Delay available"
fi
if [ $INPUT_FILESIZE > 1048576 ]; then
	if [ -z "$DELAY" ]; then
		gifsicle -U $INPUT_FILE  --scale 0.70 --lossy=80 -O3 -o $OUTPUT_FILE
	else
		gifsicle -U $INPUT_FILE --delay $NEW_DELAY `seq -f "#%g" 0 $DELAY_FACTOR $NUM_FRAMES` --scale 0.70 --lossy=80 -O3 -o $OUTPUT_FILE
	fi
else
	if [ -z "$DELAY" ]; then
                gifsicle -U $INPUT_FILE   --lossy=80 -O3 -o $OUTPUT_FILE
        else

                gifsicle -U $INPUT_FILE --delay $NEW_DELAY `seq -f "#%g" 0 $DELAY_FACTOR $NUM_FRAMES`  --lossy=80 -O3 -o $OUTPUT_FILE
        fi
fi

OUTPUT_FILEINFO=`gifsicle $OUTPUT_FILE -I`
OUTPUT_FRAMES=`echo "$OUTPUT_FILEINFO" | grep -E "[0-9]+ images" --only-matching | grep -E "[0-9]+" --only-matching`
OUTPUT_COLORS=`echo "$OUTPUT_FILEINFO" | grep "color" | grep -oE "[0-9]." --only-matching`
OUTPUT_FILESIZE=`cat $OUTPUT_FILE | wc -c`
OUTPUT_FILESIZE_READABLE=`bytesReadable $OUTPUT_FILESIZE`

echo " Original file: $INPUT_FILESIZE_READABLE ($NUM_FRAMES frames, $INPUT_COLORS colors)"
echo "Optimized file: $OUTPUT_FILESIZE_READABLE ($OUTPUT_FRAMES frames, $OUTPUT_COLORS colors)"

if [ $INPUT_FILESIZE > $OUTPUT_FILESIZE ]; then
  FILESIZE_DIFF=$((INPUT_FILESIZE-OUTPUT_FILESIZE))
  FILESIZE_DIFF_READABLE=`bytesReadable $FILESIZE_DIFF`
  echo "         Saved: $FILESIZE_DIFF_READABLE"
fi