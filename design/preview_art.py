from pathlib import Path
from PIL import Image, ImageDraw, ImageFont
import re
base=Path(__file__).resolve().parents[1]
fontpath=base/'demo/src/main/resources/com/example/fonts/wreck-pixel.ttf'
s=(base/'demo/src/main/java/com/example/OceanArt.java').read_text()
palette={'g':'#166758','G':'#6cbe68','t':'#a65d3b','s':'#c18b49','S':'#f2d48d','d':'#263750','L':'#7eafbe','B':'#d8e9df','k':'#111b35','w':'#287da0'}
im=Image.new('RGB',(960,560),'#101a32');d=ImageDraw.Draw(im)
def text(pos,value,size,color):d.text(pos,value,font=ImageFont.truetype(str(fontpath),size),fill=color)
def sprite(name,x,y,p):
    rows=re.findall(r'"([^"\n]+)"', re.search(r'String\[\] '+name+r' = \{(.*?)\};',s,re.S).group(1))
    for r,row in enumerate(rows):
        for c,ch in enumerate(row):
            if ch in palette:d.rectangle((x+c*p,y+r*p,x+c*p+p-1,y+r*p+p-1),fill=palette[ch])
d.rectangle((20,20,940,540),outline='#496884',width=4)
text((270,39),'WORD-WRECK!',64,'#98465c');text((266,35),'WORD-WRECK!',64,'#ffe09a')
text((251,116),'8-BIT OCEAN QUEST / ART PREVIEW',20,'#b4d3d7')
d.rectangle((45,168,914,440),fill='#173d68')
for y in range(182,430,24):
    for x in range(55,891,48):
        d.rectangle((x,y,x+16,y+2),fill='#245d85');d.rectangle((x+4,y+2,x+20,y+4),fill='#3481a0')
sprite('PALM',77,200,5);sprite('SHARK',700,323,4)
for i,ch in enumerate('CARGO'):
    x=357+i*48
    d.rectangle((x,273,x+42,315),fill='#a46643',outline='#eac487',width=3)
    d.rectangle((x+4,307,x+38,309),fill='#754638')
    text((x+9,279),ch,32,'#ffecba')
for y in [225,321,369]:
    d.rectangle((453,y,495,y+42),fill='#a46643',outline='#eac487',width=3)
d.rectangle((453,273,495,315),outline='#ffe4a4',width=4)
for i,(label,col) in enumerate([('GREEN','#3b8164'),('GOLD','#977634'),('GRAY','#48536b')]):
    x=78+i*145;d.rectangle((x,468,x+117,507),fill=col);text((x+12,476),label,20,'#eaf0da')
d.rectangle((622,465,874,511),fill='#e8b95f',outline='#ffedb0',width=3);text((637,477),'START VOYAGE >',26,'#25213b')
im.save(base/'PIXEL-ART-PREVIEW.png')
