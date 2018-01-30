# chimelongtest

1.先把6.6压缩包HYBRISCOMM6600P_0-70003031.ZIP文件放到/chimelongtest下
在当前目录下执行 ant install

2.在 /chimelongtest/hybris/bin/platform 下执行 ant clean all 没有报错后执行  ant initialize，都成功后可以启动服务

3.这个是基于B2C的 

访问地址：
http://chimelong.local:9001/chimelongstorefront
https://chimelong.local:9002/chimelongstorefront

配置host文件 添加
127.0.0.1       chimelong.local