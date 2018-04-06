#!/bin/bash
for ((q=10;q<110;q+=10))
{
for ((i=0;i<15;i++))
{
sudo docker run -t -i fred /usr/bin/j2bmp '/j'$q'/'$i'j'$q'.jpg'
echo '/j'$q'/'$i'j'$q'.jpg' 
cat /sys/fs/cgroup/cpuacct/*/cpuacct.stat
#sleep 1 
}
}
