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

import tools

#parameters
map_name = 's0.map'
out_name = 'import.map'

# read file
in_file = open(map_name, 'rb')
data = in_file.read()
in_file.close()

# store interesting values for each tile cell (36 bytes long)
n = 108 * 60;
s = 36;


terrain = data[0: n * s : s]
# 0	level grass	clearing, cotton, cattle, horse, town
# 1	forest		forest
# 2	hill		barren hills, wool
# 3	mountain	mountain
# 4	swamp		swamp
# 5	ocean		ocean
# 6	wasteland	desert, tundra
# 7	farmland	grain, orchard

resources = data[17: n * s + 17 : s]
# 0	cotton
# 1	wool
# 2	forest
# 3	coal
# 4	iron
# 5	horses
# 6	oil
# 17	grain
# 18	fruit
# 19	fish (not used)
# 20	cattle
# 21	gems
# 22	gold
# 255	no resource

# trim size (first 8 columns)
terrain = [v for i, v in enumerate(terrain) if i % 108 >= 8]
resources = [v for i, v in enumerate(resources) if i % 108 >= 8]

# some statistics
unique_terrain = list(set(terrain))
unique_terrain.sort()
for t in unique_terrain:
    print 'terrain {0}'.format(ord(t))
    rt = [vars for i, vars in enumerate(resources) if terrain[i] == t]
    unique_rt = list(set(rt))
    unique_rt.sort()
    h = [ord(v) for v in unique_rt]
    print ' contains resources {0}'.format(h)


# transform to our scheme
new_terrain = {'\x00': 'p1', '\x01': 'f1', '\x02': 'h1', '\x03': 'm1', '\x04': 'x1', '\x05': 's1', '\x06': 'd1', '\x07': 'p1'}

result = []
for i in range(len(terrain)):
    t = new_terrain[terrain[i]]
    if resources[i] == chr(18):
        t = 'p4'
    if resources[i] == chr(0):
        t = 'p3'
    if resources[i] == chr(20):
        t = 'p2'
    result.append(t)

# save output
tools.write(out_name, ''.join(result))
#out_file = open(out_name, 'wb')
#out_file.write(''.join(result))
#out_file.close()