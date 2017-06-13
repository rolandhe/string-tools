# 简介
java字符串处理工具集，目前仅支持某些中文的繁体<->简体的转换和cityhash实现。其中繁简对应关系收录在字典文件，必要时可以自行扩展；cityhash的实现结果与官方c版本保持一致,自动适配系统大小端。
# 内部实现原理
## 繁体<->简体的转换
内部使用trie实现，处理效率比较高,时间复杂度是O(n*h)，n是字符串长度，h是字典树高度。
## cityhash
+ 完全移植官方的c版本，运行结果与官方版本完全相同，自动适配系统大小端。我们针对hash64方法测试2-6k长度的数据共计1200万，与c版本相比无diff
+ 我们也发现github上已经存在[https://github.com/tamtam180/CityHash-For-Java](https://github.com/tamtam180/CityHash-For-Java/blob/master/src/main/java/at/orz/hash/CityHash.java)实现，之前我们也用这个版本，但我们发现其运行结果与官方的c版本的运行结果有很大的不同。
