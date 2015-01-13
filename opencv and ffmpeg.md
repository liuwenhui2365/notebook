#install  

- 去网站下载即可https://www.ffmpeg.org/

- Linux可以在终端直接安装
   sudo apt-get install ffmpeg

#install  opencv

- 安装与opencv相关的包和工具：
install.sh

 apt-get install build-essential -y
apt-get install cmake -y
apt-get install pkg-config -y
apt-get install libpng12-0 libpng12-dev libpng++-dev libpng3 -y
apt-get install libpnglite-dev libpngwriter0-dev libpngwriter0c2 -y
apt-get install zlib1g-dbg zlib1g zlib1g-dev -y
apt-get install libjasper-dev libjasper-runtime libjasper1 -y
apt-get install pngtools libtiff4-dev libtiff4 libtiffxx0c2 libtiff-tools -y
apt-get install libjpeg8 libjpeg8-dev libjpeg8-dbg libjpeg-prog -y
apt-get install ffmpeg libavcodec-dev libavcodec52 libavformat52 libavformat-dev -y
apt-get install libgstreamer0.10-0-dbg libgstreamer0.10-0  libgstreamer0.10-dev -y
apt-get install libxine1-ffmpeg  libxine-dev libxine1-bin -y
apt-get install libunicap2 libunicap2-dev -y
apt-get install libdc1394-22-dev libdc1394-22 libdc1394-utils -y
apt-get install swig -y
apt-get install libv4l-0 libv4l-dev -y
apt-get install python-numpy -y
apt-get install libgtk2.0 -y
apt-get install libavformat-dev libswscale-dev -y

有几个软件包没有找到，没事不影响使用就行，安装gtk时先使用命令apt-cache search gtk2.0 或者3.0的，查找gtk开头的然后选择安装。

然后去官方网站下载自己需要安装的opencv相应的版本
http://opencv.org/downloads.html


-  make && sudo make install：

>- cmake CMakeLists.txt
>- make && sudo make install
>- sudo vim /etc/ld.so.conf
/usr/local/lib #add in the last line.
- ldconfig
第二条命令千万不能忘了，否则不能正确安装，实际并没有安装上去。
如果没有什么意外的话opencv就算安装成功了。

-  gcc -ggdb  `pkg-config --cflags opencv` -o opencv0 opencv0.c `pkg-config --libs opencv`
注意包的使用，编译时在英文输入状态下，按下数字1键前面的那个键即可。

##关于ffmpeg的使用

- 相关函数的意义
>av_register_all()：注册FFmpeg所有编解码器。
avformat_alloc_output_context2()：初始化输出码流的AVFormatContext。
avio_open()：打开输出文件。
av_new_stream()：创建输出码流的AVStream。
avcodec_find_encoder()：查找编码器。
avcodec_open2()：打开编码器。
avformat_write_header()：写文件头（对于某些没有文件头的封装格式，不需要此函数。比如说MPEG2TS）。
avcodec_encode_video2()：编码一帧视频。即将AVFrame（存储YUV像素数据）编码为AVPacket（存储H.264等格式的码流数据）。
av_write_frame()：将编码后的视频码流写入文件。
flush_encoder()：输入的像素数据读取完成后调用此函数。用于输出编码器中剩余的AVPacket。
av_write_trailer()：写文件尾（对于某些没有文件头的封装格式，不需要此函数。比如说MPEG2TS）。

##视频格式与编码

参考文档：

[H264与ogg](http://www.ruanyifeng.com/blog/2010/05/html5_codec_fight.html)

###视频格式

AVI，
	音频视频交错(Audio Video Interleaved)的英文缩写。AVI这个由微软公司发表的视频格式，在视频领域可以说是最悠久的格式之一。AVI格式调用方便、图像质量好，压缩标准可任意选择，是应用最广泛、也是应用时间最长的格式之一。
WMV
	一种独立于编码方式的在Internet上实时传播多媒体的技术标准，Microsoft公司希望用其取代QuickTime之类的技术标准以及WAV、AVI之类的文件扩展名。WMV的主要优点在于：可扩充的媒体类型、本地或网络回放、可伸缩的媒体类型、流的优先级化、多语言支持、扩展性等。
3GP是一种3G流媒体的视频编码格式，主要是为了配合3G网络的高传输速度而开发的，也是目前手机中最为常见的一种视频格式。
FLV是FLASH VIDEO的简称，FLV流媒体格式是一种新的视频格式。由于它形成的文件极小、加载速度极快，使得网络观看视频文件成为可能，它的出现有效地解决了视频文件导入Flash后，使导出的SWF文件体积庞大，不能在网络上很好的使用等缺点。
RMVB
	的前身为RM格式，它们是Real Networks公司所制定的音频视频压缩规范，根据不同的网络传输速率，而制定出不同的压缩比率，从而实现在低速率的网络上进行影像数据实时传送和播放，具有体积小，画质也还不错的优点。
WebM
[1] 由Google提出，是一个开放、免费的媒体文件格式。WebM 影片格式其实是以 Matroska（即 MKV）容器格式为基础开发的新容器格式，里面包括了 VP8 影片轨和 Ogg Vorbis 音轨，其中Google将其拥有的VP8视频编码技术以类似BSD授权开源，Ogg Vorbis 本来就是开放格式。 WebM标准的网络视频更加偏向于开源并且是基于HTML5标准的，WebM 项目旨在为对每个人都开放的网络开发高质量、开放的视频格式，其重点是解决视频服务这一核心的网络用户体验。Google 说 WebM 的格式相当有效率，应该可以在 netbook、tablet、手持式装置等上面顺畅地使用。


###编码

MPEG系列
（由ISO[国际标准组织机构]下属的MPEG[运动图象专家组]开发 ）视频编码方面主要是Mpeg1（vcd用的就是它）、Mpeg2（DVD使用）、Mpeg4（的DVDRIP使用的都是它的变种，如：divx，xvid等）、Mpeg4 AVC（正热门）；音频编码方面主要是MPEG Audio Layer 1/2、MPEG Audio Layer 3（大名鼎鼎的mp3）、MPEG-2 AAC 、MPEG-4 AAC等等。注意：DVD音频没有采用Mpeg的。
H.26X系列
（由ITU[国际电传视讯联盟]主导，侧重网络传输，注意：只是视频编码）
包括H.261、H.262、H.263、H.263+、H.263++、H.264（就是MPEG4 AVC-合作的结晶）

H.264/AVC
H.264集中了以往标准的优点，并吸收了以往标准制定中积累的经验，采用简洁设计，使它比MPEG4更容易推广。H.264创造性了多参考帧、多块类型、整数变换、帧内预测等新的压缩技术，使用了更精细的分象素运动矢量（1/4、1/8）和新一代的环路滤波器，使得压缩性能大大提高，系统更加完善。
H.264主要有以下几大优点：
－ 高效压缩：与H.263+和MPEG4 SP相比，减小50%比特率；
－ 延时约束方面有很好的柔韧性；
－ 容错能力；
－ 编/解码的复杂性可伸缩性；
－ 解码全部细节：没有不匹配；
－ 高质量应用；
－ 网络友善。

JPEG
国际标准化组织于1986年成立了JPEG(Joint Photographic Expert Group）联合图片专家小组，主要致力于制定连续色调、多级灰度、静态图像的数字图像压缩编码标准。常用的基于离散余弦变换（DCT）的编码方法，是JPEG算法的核心内容。
MPEG-1/2
MPEG-1标准用于数字存储体上活动图像及其伴音的编码，其数码率为1.5Mb/s。MPEG-1的视频原理框图和H.261的相似。
MPEG-1视频压缩技术的特点：1.随机存取；2. 快速正向/逆向搜索；3 .逆向重播；4. 视听同步；5.容错性；6. 编/解码延迟。MPEG-1视频压缩策略：为了提高压缩比，帧内/帧间图像数据压缩技术必须同时使用。帧内压缩算法与JPEG压缩算法大致相同，采用基于DCT的变换编码技术，用以减少空域冗余信息。帧间压缩算法，采用预测法和插补法。预测误差可在通过DCT变换编码处理，进一步压缩。帧间编码技术可减少时间轴方向的冗余信息。
MPEG-2被称为“21世纪的电视标准”，它在MPEG-1的基础上作了许多重要的扩展和改进，但基本算法和MPEG-1相同。
MPEG-4
MPEG-4标准并非是MPEG-2的替代品，它着眼于不同的应用领域。MPEG-4的制定初衷主要针对视频会议、可视电话超低比特率压缩（小于64Kb/s）的需求。在制定过程中，MPEG组织深深感受到人们对媒体信息，特别是对视频信息的需求由播放型转向基于内容的访问、检索和操作。
MPEG-4与前面提到的JPEG、MPEG-1/2有很大的不同，它为多媒体数据压缩编码提供了更为广阔的平台，它定义的是一种格式、一种框架，而不是具体算法，它希望建立一种更自由的通信与开发环境。于是MPEG-4新的目标就是定义为：支持多种多媒体的应用，特别是多媒体信息基于内容的检索和访问，可根据不同的应用需求，现场配置解码器。编码系统也是开放的，可随时加入新的有效的算法模块。应用范围包括实时视听通信、多媒体通信、远地监测/监视、VOD、家庭购物/娱乐等。
JVT
新一代的视频压缩标准
JVT是由ISO/IEC MPEG和ITU-T VCEG成立的联合视频工作组（Joint Video Team），致力于新一代数字视频压缩标准的制定。
JVT标准在ISO/IEC中的正式名称为：MPEG-4 AVC(part10）标准；在ITU-T中的名称：H.264（早期被称为H.26L）