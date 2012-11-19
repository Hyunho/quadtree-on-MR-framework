#!/usr/bin/Rscript  


argv <- commandArgs(TRUE);

#first argument is filename to save data.
#second arggument is number of dimension. 
output = argv[1]
numDim = as.numeric(argv[2])
count = 100


origin_frame = data.frame(runif(count, min=0, max=1000))
numDim = numDim - 1

while(numDim > 0) {
  ##make new frame and bind origin frame and new one
  new_frame = data.frame(runif(count, min=0, max=1000))
  origin_frame = cbind(origin_frame, new_frame)
  numDim = numDim - 1
}

write.table(origin_frame, file=output, row.names = FALSE, col.names=FALSE)