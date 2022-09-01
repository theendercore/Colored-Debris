import os
path = "./colors"
pack_name = " Ancient Debris"
colors = []

for color in os.listdir(path):
    colors.append(color)


print(colors)
