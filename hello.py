import sys

inputPath = str(sys.argv[1])
rulerGap = str(sys.argv[2])
rulerWidth = str(sys.argv[3])

with open(inputPath, 'a') as f:
    f.write("Ruler Gap: " + rulerGap + " Ruler Width: " + rulerWidth)