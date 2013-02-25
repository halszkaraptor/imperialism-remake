# -*- coding: utf-8 -*-

#-------------------------------------------------------------------------------
# Name:     map_import
# Purpose: Import maps from Imperialism 1.
#
# Author:	  Trilarion
#
# Created:	 01/11/2012
# Licence:	 GPLv3
#-------------------------------------------------------------------------------

# TODO ord instead of strings in the display

import array, struct, os
import tools

def find_unique_elements(data, n, s):
    for x in xrange(0, 35):
        sub = data[x : n * s + x : s]
        unique = list(set(sub))
        unique.sort()
        print 'cell {0} has {1} unique values:'.format(x, len(unique))
        print ' {0}'.format(unique)

def integrity_tests(data, n, s):
    country1 = data[3 : n * s + 3 : s]
    country2 = data[4 : n * s + 4 : s]
    if country1 == country2:
        print 'cell 3 and 4 are identical'

def crop_left(data, columns, col):
    return [v for i, v in enumerate(data) if i % columns >= col]

# assumes a list of string and converts to array of int
def list_to_array(data):
    data = map(ord, data)
    return array.array('i', data)

def correlate_unique_elements(a, b):
    # make a unique and sorted
    au = list(set(a))
    au.sort()
    for e in au:
        print 'a: {0}'.format(e)
        bu = [v for i, v in enumerate(b) if a[i] == e]
        bu = list(set(bu))
        bu.sort()
        print ' goes with b: {0}'.format(bu)

# -------------------------

#parameters
map_location = 'C:/Users/jkeller1/Dropbox/remake/scenario import/europe 1814'
map_name = 's0.map'

# read file
os.chdir(map_location)
in_file = open(map_name, 'rb')
data = in_file.read()
in_file.close()

# store interesting values for each tile cell (36 bytes long)
columns = 108
cut = 8
rows = 60
n = columns * rows;
s = 36;

# unique elements for each cell
find_unique_elements(data, n, s)

# some equality tests
integrity_tests(data, n, s)

# get interesting data out and crop left columns
terrain_underlay = crop_left(data[0 : n * s + 0: s], columns, cut)
terrain_overlay = crop_left(data[19 : n * s + 19: s], columns, cut)
country = crop_left(data[3 : n * s + 3: s], columns, cut)
resources = crop_left(data[17 : n * s + 17: s], columns, cut)

rivers = crop_left(data[2 : n * s + 2: s], columns, cut)

railtrack = crop_left(data[6 : n * s + 6: s], columns, cut)

provinceA = crop_left(data[20 : n * s + 20: s], columns, cut)
provinceB = crop_left(data[21 : n * s + 21: s], columns, cut)
provinces = map(lambda a, b: ord(a) * 256 + ord(b), provinceA, provinceB)

cities = crop_left(data[29 : n * s + 29: s], columns, cut)

# get additional records
extra = data[columns * rows * s:]
provinceN = []
for x in xrange(0, 384):
    provinceN.extend(extra[x * 198 + 166: x * 198 + 176])

# even more tests
correlate_unique_elements(terrain_underlay, resources)

# conversion to arrays
size = array.array('i')
size.append(columns - cut)
size.append(rows)

terrain_underlay = list_to_array(terrain_underlay)
terrain_overlay = list_to_array(terrain_overlay)
country = list_to_array(country)
resources = list_to_array(resources)
rivers = list_to_array(rivers)
provinces = array.array('i', provinces)
names = list_to_array(provinceN)
cities = list_to_array(cities)
railtrack = list_to_array(railtrack)

# save in file
out_name = map_name[:-4] + '.imported.map'
out = open(out_name, 'wb')
out.write(size)

# 6 more chunks comin
out.write(terrain_underlay)
out.write(terrain_overlay)
out.write(country)
out.write(resources)
out.write(rivers)
out.write(provinces)
out.write(cities)
out.write(railtrack)
out.write(names)

out.close()

