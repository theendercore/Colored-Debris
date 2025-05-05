from zipfile import ZipFile
import os
import shutil


pack_name = "AncientDebris"
pack_version = "1.3.2"
pack_format = 13
# mcmeta = "./pack.mcmeta"

file_path = "./assets/minecraft/textures/block/ancient_debris_"
anim_dir = ""

colors_amount = 0

os.mkdir("./temp")
mcmeta_file = open('./temp/pack.mcmeta', 'w')
mcmeta_file.write( '{\n   "pack":{' +\
    '\n      "pack_format": ' + str(pack_format) + ',' + \
    '\n      "description": "\u00a75by Ender",' + \
    '\n      "pack_version": "' + \
    pack_version + '"\n' + \
    '   }\n}')
mcmeta_file.close()


# mcmeta_file.write(mcmeta_cont)



os.mkdir("./expo")
os.mkdir("./expo/rainbow")

for color in os.listdir("./colors"):
    if color.endswith("Rainbow"):
        anim_dir = "rainbow/"
    x = './expo/'+anim_dir+color+pack_name+"-"+pack_version+'.zip'
    with ZipFile(x, 'w') as resourcepack:
        resourcepack.write("./temp/pack.mcmeta", "./pack.mcmeta")
        resourcepack.write("./colors/"+color+"/top.png", file_path+"top.png")
        resourcepack.write("./colors/"+color+"/side.png", file_path+"side.png")
        if anim_dir == "":
            resourcepack.write("./colors/"+color+"/side.png", "./pack.png")
        else:
            resourcepack.write("./colors/"+color + "/top.png.mcmeta",
                               file_path+"top.png.mcmeta")
            resourcepack.write("./colors/"+color + "/side.png.mcmeta",
                               file_path+"side.png.mcmeta")
            resourcepack.write("./colors/OriginalGreen/side.png",
                               "./pack.png")
    colors_amount += 1
    anim_dir = ""

print(colors_amount)


try:
    shutil.rmtree("./temp")
except OSError as e:
    print("Error: %s - %s." % (e.filename, e.strerror))