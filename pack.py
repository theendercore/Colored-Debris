from zipfile import ZipFile
import os

pack_version = "1.4.0"
mcmeta = "./pack.mcmeta"
pack_name = " Ancient Debris"
file_path = "./assets/minecraft/textures/block/ancient_debris_"
colors_amount = 0


for color in os.listdir("./colors"):
    if color.endswith("Rainbow"):

    with ZipFile('./expo/'+color+pack_name+" "+pack_version+'.zip', 'w') as resourcepack:
        resourcepack.write(mcmeta)
        resourcepack.write("./colors/"+color+"/top.png", file_path+"top.png")
        resourcepack.write("./colors/"+color+"/side.png", file_path+"side.png")
        resourcepack.write("./colors/"+color+"/side.png", "./pack.png")
    colors_amount += 1

print(colors_amount)
