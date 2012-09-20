count = 10000
df = data.frame(x= runif(count, min=0, max=100), y= runif(count, min =0, max = 100))
write.table(df, file="data2D-quad.txt", row.names = FALSE, col.names=FALSE)
