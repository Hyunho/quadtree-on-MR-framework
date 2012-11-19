#!/usr/bin/Rscript  

argv <- commandArgs(TRUE);

#initialze variable with arguments 
#first argument is filename to save data.second argument is number of dimension. third argument  is size of data
output = argv[1]
numDim = as.numeric(argv[2])
size = as.numeric(argv[3]);

# if given size is too large , we generate a data with smail size to maintain in memory.
count = 1000000

gnorm <- function(count, mean, sd)  {

  v <- c()

  while(count > 0) {
    record = rnorm(1, mean, sd)    
    if(record < 1000 && record > 0) {
      v <- c(v, record)
      count = count - 1
    }
  }
  return(v)
}

# define function to generate data frame with given dimension and count
gen_frame <- function(dimension, count) {
  
  origin_frame = data.frame(gnorm(count, mean=300, sd=300))
  numDim = numDim - 1
  
  while(numDim > 0) {
    
    ##make new frame and bind origin frame and new one    
    new_frame = data.frame(data.frame(gnorm(count, mean=300, sd=300)))
    
    
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


