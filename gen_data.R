for ( i in 1:100) write.table(data.frame(x = sample(1:100000, 5000000, replace=TRUE), y = sample(1:100000, 5000000, replace=TRUE)), as.character(i), row.names=FALSE, col.names=FALSE)
q()
