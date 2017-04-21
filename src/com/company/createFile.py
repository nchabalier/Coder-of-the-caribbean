import sys
import os
import glob
import time


javafiles = glob.glob("*.java")

final_file= open("Final.txt","w")

import_lines = []

for file_name in javafiles:
    file = open(file_name,"r")
    lines = file.readlines()
    for line in lines:
        if not "package " in line:
            if "import " in line:
                if line not in import_lines:
                    import_lines.append(line)
                    final_file.write(line)

    file.close()


for file_name in javafiles:
    file = open(file_name,"r")
    lines = file.readlines()
    for line in lines:
        if not "package " in line:
            if "import " not in line:
                final_file.write(line)

    file.close()

final_file.close()
time.sleep(5.5)