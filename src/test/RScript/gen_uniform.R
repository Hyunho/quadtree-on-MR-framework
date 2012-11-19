#!/usr/bin/Rscript  

argv <- commandArgs(TRUE);

#initialze variable with arguments 
#first argument is filename to save data.second argument is number of dimension. third argument  is size of data
output = argv[1]
numDim = as.numeric(argv[2])
size = as.numeric(argv[3]);

# if given size is too large , we generate a data with smail size to maintain in memory.
count = 100000

# define function to generate data frame with given dimension and count
gen_frame <- function(dimension, count) {
  
  origin_frame = data.frame(runif(count, min=0, max=1000))
  numDim = numDim - 1
  
  while(numDim > 0) {
    ##make new frame and bind origin frame and new one
    new_frame = data.frame(runif(count, min=0, max=1000))
    origin_frame = cbind(origin_frame, new_frame)
    numDim = numDim - 1
  }
  
  return(origin_frame)
}


while(size > count) {
  df = gen_frame(dimension, count) 
  write.table(df, file=output, append = TRUE, row.names = FALSE, col.names=FALSE)
  size = size - count  
}

df = gen_frame(dimension, size) 
write.table(df, file=output, append = TRUE, row.names = FALSE, col.names=FALSE)