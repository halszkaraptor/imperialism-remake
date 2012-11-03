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

r = list(set(terrain))
r.sort()
print r

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

r = list(set(resources))
r.sort()
print r

# transform to our scheme
new_terrain = {'\x00': 'p1', '\x01': 'f1', '\x02': 'h1', '\x03': 'm1', '\x04': 'x1', '\x05': 's1', '\x06': 'd1', '\x07': 'p1'}

result = []
for i in range(n):
    t = new_terrain[terrain[i]]
    result.append(t)

# save output
tools.write(out_name, ''.join(result))
#out_file = open(out_name, 'wb')
#out_file.write(''.join(result))
#out_file.close()