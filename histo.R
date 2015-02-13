library(ggplot2)
library(xlsx)
library(plyr)
var = file.choose()
poldata = read.xlsx(var, sheetIndex=1)
cdf = ddply(poldata, "column", summarise, mean=mean(polarity), freq= length(column))
cdf2= signif(cdf, digits = 2)
p = ggplot(poldata, aes(x=polarity)) +
	geom_histogram(binwidth=.1, fill = "lightblue", color = "black", origin = -0.05) + 
	facet_grid(. ~ column) + 
	geom_vline(data=cdf, aes(xintercept=mean), linetype="dashed", size = .5, color = "red") + 
	scale_x_continuous(breaks=c(0, 0.5, 1), labels = c("0", "0.5", "1")) + scale_y_continuous(breaks=seq(0,80, (((signif(max(cdf[,3])/5, digits=1))*5)/5)), expand = c(.005, 0)) +
	geom_text(data=cdf2, aes(x=.2, y=Inf, label=mean), size = 5, vjust = 2, color = "red") + ggtitle(var) +
	theme_bw() + theme(panel.grid.minor=element_blank(), strip.background = element_rect(color = "black", fill = NA, size = 1), plot.title = element_text(size = 8, face="bold"))
print(cdf)
ggsave(file="~/Desktop/ABPhistogram.pdf", width = 6.5, height = 3, units = "in")


