#!/usr/bin/Rscript  

argv <- commandArgs(TRUE);
output = argv[1]
count = 100
df = data.frame(x= runif(count, min=0, max=100), y= runif(count, min =0, max = 100))
write.table(df, file=output, row.names = FALSE, col.names=FALSE)
