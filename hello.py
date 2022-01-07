import sys

inputPath = str(sys.argv[1])
rulerGap = str(sys.argv[2])

with open(inputPath, 'a') as f:
    f.write(rulerGap)