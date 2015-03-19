#abp-analysis

##Version 1.0
This version comes in two parts: the JavaScript plugin for ImageJ that measures denticle position and the R code to easily turn that data into histograms for analysis of mutant phenotypes.

###JavaScript Plugin

Designed with Dan Ly and Kynan Lawlor. A tool to determine the degree of planar polarity of denticle precursors in *Drosophila* embryos

####To install a JavaScript plugin in ImageJ (or better yet, Fiji):

Copy the .java file to the ../ImageJ/Plugins folder. (Mac: If you can't find it, right click on ImageJ and choose "Show Package Contents")

In ImageJ/Fiji, go to the "Plugins" dropdown menu and choose "Compile and Run" and select this .java file.

Restart ImageJ/Fiji. Upon restart, it should appear in the "Plugins" dropdown menu. Occasionally, this doesn't work and the solution is to add an underscore "_" to the Plugin name. Not sure why this happens or how this fixes it.

####How to use the abp analysis plugin

#####Get embryos

Embryos of the appropriate stage of analysis need to be stained with a molecular marker that will label actin (the main component of the actin-based protrusions) and a marker that will label cell outlines. For actin, phalloidin works great. Cell outlines at this stage can be visualized with an anti-E-cadherin antibody or an anti-phospho-tyrosine antibody. 

*Note: The anti-E-cadherin I've used is the one from the DHSB and it doesn't always look great with quick 37% FA, 5 min fixation ("Hard fix", Theurkauf, 1994, preserves actin). While I've had success with it, staining isn't terribly consistent from embryo to embryo. The anti-phospho-tyrosine antibody consistently stains well with 37% FA, 5min fix, but will also label a portion of the area around the ABP. Depending on how you want to specify to ImageJ where cell outlines are, one of these antibodies may work better than the other.*

Acquire images however you'd like, but take a stack of both the cell outline marker and phalloidin: the DiNardo lab uses a 40x objective. 

#####How to prepare images

Using the line tool I draw a line along the denticle columns to measure the angle.  Then use this angle to calculate the value for Transform > Rotate (and flip horizontally if necessary) so that the image is anterior to the left, with the denticle cell columns aligned vertically. This is important for proper measurement.

Crop around the area to be analyzed. For consistency, choose one segment to analyze across samples.

For better image quality, choose 3 slices around the region where the ABPs are in focus and delete the others. Best with the "Image/Duplicate" tool. Then MED project the 3 slices into one image.

At this point separate the 2 channels.

######For actin

We want a quick way to obtain coordinate data for the ABPs.

Threshold the Actin image by "Adjust/Threshold". Adjust so as to remove any "false" particles (At this point it may be necessary to run a 'watershed' function in ImageJ to separate touching particles as well). Set image to binary.

Now run "Analyze/Analyze particles". Using the default settings seems to work fine.  This gives a table with coordinates.

######For cell outlines

There are two ways that we have used to best obtain cell outline data. 

######1. Thresholding

The first is to threshold the E-cadherin/P-tyr image, make it a binary image and run the ABP plugin on that data. This works alright, but frequently thresholding these images results in cell outlines that are incomplete and have gaps. In large data sets, erroneous data caused by these cells is hard and time consuming to filter out.

Threshold the E-cadherin/P-tyr image by "Adjust/Threshold". Change the threshold level to minimize gaps in the edge. Set the image to binary (Process/Binary/Make Binary).

######2. Cell outline tracing

This way maxmimizes data from image, as it preserves all cell outlines, they are not thrown out if the staining isn't optimal. However, it takes some time up front, and requires the user to reliably trace the outlines of cells in ImageJ/Fiji.

In ImageJ, trace all of the cell outlines using the segmented line tool. At the bottom of one column, move to the right (posterior) and start tracing up. Make sure not to break the line, as you'll have to start over. Once finished, save this line under File/Save as XY coordinates.

This is undoubtedly the best method, and future versions of this plugin will attempt to integrate this step.

#####How to run the plugin/how to generate distance data

Open the coordinate data using Excel. We need to convert this data into coordinates only, in arrays that are comma deliminated. Copy the X and Y values, paste into a new file, using Paste Special with the "Transpose" box checked. Then format the coordinates so that they are rounded to the nearest integer. Save as a .csv file and open in a text editor.

For thresholded cell outline images, open in ImageJ. Go to Plugins/ABP Distance. Copy the x-coordinates and paste in the popup box. Do the same for the y-coordinates. A results table will appear. Save this as an Excel file. 

For traced cell outlines, open your cropped images in Fiji. In Fiji, we can import XY coordinates easily. Delete all the data in the cropped images, we need this file for the correct dimensions. Import the XY coordinates that represent the cell outlines and make it a line: Edit/Draw. (I've been using line width of 3px for 40x images.) Make this binary (make sure the lines have a value of 255, if not, invert the image) and run the plugin as mentioned above.
 
Open up the results in Excel. Create a new column and divide the "Distance to right border" by "Cell width". If this value is 0, the ABPs are polarized correctly. Anything that is greater than zero is incorrectly polarized.

Visually inspect the images and coordinates to determine what column the ABPs reside in.

Make a new excel file with column data and polarity data in two columns. This .xlsx file can be used in the R code to produce histograms.


##Future versions

I'd like to develop a user responsive component to the plugin, where cell columns can be drawn while the plugin is running, to speed acquisition time and produce cell column data at the same time. i.e. prompt the user to trace the column 1/2 boundary, the 2/3 boundary etc.

The polarity values can be computed by the plugin, freeing the user some time. Additionally with column data already added, this will reduce ~90% of the time required to analyze these images.





