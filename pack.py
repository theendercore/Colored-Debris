from zipfile import ZipFile
import shutil
import os

pack_version = "1.4.0"
mcmeta = "./pack.mcmeta"
pack_name = " Ancient Debris"
file_path = "./assets/minecraft/textures/block/ancient_debris_"
colors_amount = 0
anim_dir = ""

os.mkdir("./expo")
os.mkdir("./expo/rainbow")

for color in os.listdir("./colors"):
    if color.endswith("Rainbow"):
        anim_dir = "rainbow/"
    with ZipFile('./expo/'+anim_dir+color+pack_name+" "+pack_version+'.zip', 'w') as resourcepack:
        resourcepack.write(mcmeta)
        resourcepack.write("./colors/"+color+"/top.png", file_path+"top.png")
        resourcepack.write("./colors/"+color+"/side.png", file_path+"side.png")
        if anim_dir == "":
            resourcepack.write("./colors/"+color+"/side.png", "./pack.png")
        else:
            resourcepack.write("./colors/"+color + "/top.png.mcmeta",
                               file_path+"top.png.mcmeta")
            resourcepack.write("./colors/"+color + "/side.png.mcmeta",
                               file_path+"side.png.mcmeta")
            resourcepack.write("./colors/Original Green/side.png",
                               "./pack.png")
    colors_amount += 1
    anim_dir = ""

print(colors_amount)
