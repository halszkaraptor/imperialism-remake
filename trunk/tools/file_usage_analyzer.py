# -*- coding: utf-8 -*-

#-------------------------------------------------------------------------------
# Name:		file_usage_analyzer
# Purpose: Analyze the output of the file usage and compare with existing files.
#
# Author:	  Trilarion
#
# Created:	 29/11/2012
# Licence:	 GPLv3
#-------------------------------------------------------------------------------

import os

import tools

# parameters
lookup_dir = '..' + os.sep + 'main' + os.sep + 'dist' # relative to this files location

encoding = "utf-8"

# change to lookup directory, everything relative to lookup directory
os.chdir(lookup_dir)

# read usage file
print "load usage data"
used = open('log/file_usage.txt').read().splitlines()
used = set(used)
print ' used {0} files'.format(len(used))

# get all files in the data directory
print "locating data files"
folders, files = tools.locate(root = 'data')
existing = map(lambda folder, file: folder.replace('\\', '/') + '/' + file, folders, files) # get folders and files together again
existing = set(existing)
print ' existing {0} files'.format(len(existing))

# compare both lists
print "analyze data"
not_used = list(existing - used) # using set operations
not_used.sort()
not_existing = list(used - existing)
not_existing.sort()

# print not used files
print " {0} files not used".format(len(not_used))
for name in not_used:
    print "  {0}".format(name)

# print not existing files
print " {0} files not existing".format(len(not_existing))
for name in not_existing:
    print "  {0}".format(name)