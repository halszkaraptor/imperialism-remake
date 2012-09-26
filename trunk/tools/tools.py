# -*- coding: utf-8 -*-

#-------------------------------------------------------------------------------
# Name:        tools
# Purpose:     general purpose methods for the scripts in this filder
#
# Author:      Trilarion
#
# Created:     23/07/2012
# Licence:     GPLv3
#-------------------------------------------------------------------------------

import fnmatch, os, codecs


# locate all files matching a name pattern recursively (see os.walk, fnmatch)
def locate(pattern, root = os.curdir):

    folders = list()
    names = list()

    for path, dirs, files in os.walk(root):
        for name in fnmatch.filter(files, pattern):
            names.append(name)
            folders.append(path)

    return folders, names


# read utf-8 (default as utf-8)
def read(filename, encoding = 'utf-8'):
    input_file = codecs.open(filename = filename, mode = "r", encoding = "utf-8")
    text = input_file.read()
    input_file.close()
    text = text.lstrip(u'\ufeff') # remove the byte-order mark if there

    return text


# writes unicode to file (default as utf-8)
def write(filename, text, encoding = 'utf-8'):
    text = text.lstrip(u'\ufeff') # remove the byte-order mark if there
    output_file = codecs.open(filename = filename, mode = "w", encoding = encoding, errors = "xmlcharrefreplace")
    output_file.write(text)
    output_file.close()