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

import array, struct
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

#parameters
map_name = 's0.map'

# read file
in_file = open(map_name, 'rb')
data = in_file.read()
in_file.close()

# store interesting values for each tile cell (36 bytes long)
columns = 108
rows = 60
n = columns * rows;
s = 36;

# unique elements for each cell
find_unique_elements(data, n, s)

# some equality tests
integrity_tests(data, n, s)

# get interesting data out and crop left columns
terrain_underlay = crop_left(data[0 : n * s + 0: s], columns, 8)
terrain_overlay = crop_left(data[19 : n * s + 19: s], columns, 8)
resources = crop_left(data[17 : n * s + 17: s], columns, 8)

provinceA = crop_left(data[20 : n * s + 20: s], columns, 8)
provinceB = crop_left(data[21 : n * s + 21: s], columns, 8)
provinces = map(lambda a, b: ord(a) * 256 + ord(b), provinceA, provinceB)

# even more tests
correlate_unique_elements(terrain_underlay, resources)

# conversion to arrays
size = array.array('i')
size.append(100)
size.append(60)

terrain_underlay = list_to_array(terrain_underlay)
terrain_overlay = list_to_array(terrain_overlay)
resources = list_to_array(resources)

provinces = array.array('i', provinces)

# save in file
out_name = map_name[:-4] + '.imported.map'
out = open(out_name, 'wb')
out.write(size)
out.write(terrain_underlay)
out.write(terrain_overlay)
out.write(resources)
out.write(provinces)
out.close()

