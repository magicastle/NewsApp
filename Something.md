## 我们的关关关关键指导：

**1. <https://github.com/haerulmuttaqin/PopularNews> ** 

**2.<https://github.com/iMeiji/Toutiao>**





## Done: UI

### MainActivity

* Navigation drawer Activity (**Now Chosen**))
* Tabbed Activity
* 或者组合

### NewsDetailActivity

* .......



## Done : Network

- [x] Retrofit : 网络请求，返回数据处理



## TODO: Data List

- [x] RecycleView: ListView 升级版
- [x] CardView:  beautify
- [x] SwipeRefreshLayout: 下拉刷新
- [ ] SwipeRefreshLayout: 上拉加载更多
- [x] Click jump to news detail page



## TODO: Main Activity

- [x] Search:  words
- [ ] Settings:



## TODO: NewsDetail Activity

- [x] 由MainActivity页面向 NewsDetail 页面传递信息：选择了传递自定义对象SingleNews,要不然一项一项传入过于麻烦了， 需要 SingleNews 类 实现 Serializable接口（不需要做什么）
- [x] Glide : 图片加载框架：用于新闻具体页面中图片(其他选择：picasso, fresco)
- [x] 左上角返回上一级
- [ ] 分享
- [ ] 收藏
- [ ] 保存本地



## TODO: 本地存储

- [ ] 每次请求得到的json文件
- [ ] 数据库 or json格式？
- [ ] 看过的新闻



## 遇到的(大)坑/问题 + 解决

* <https://blog.csdn.net/qq_32623363/article/details/54895817> 神他么巨坑





|          | 功能     | 子功能                               | 分   | 完成情况  | 子性能         | 分   | 完成情况  |
| -------- | -------- | ------------------------------------ | ---- | --------- | -------------- | ---- | --------- |
| 基础功能 | 系统支持 | 正常运行不崩溃                       | 8    | Done      | 不卡顿         | 2    | Done      |
|          | 页面布局 | 合理正确                             | 8    |           | 美观、图片     | 2    |           |
|          | 分类列表 | 删除添加                             | 4    | Done      | 修改动态       | 1    | Done      |
|          | 新闻列表 | 正确显示,进入                        | 8    | Done      | 图片、视频     | 2    | Half Done |
|          |          | 本地存储<br />离线浏览<br />页面标灰 | 8    |           | 存储大量新闻   | 2    |           |
|          |          | 上拉更多<br />下拉刷新               | 4    | Half Done | 上拉、下拉特效 | 1    |           |
|          |          | 显示来源、时间                       | 4    | Done      | 合理美观       | 1    |           |
|          |          | 新闻关键词<br />历史记录             | 4    | Half Done | 合理美观       | 1    |           |
|          | 分享     | 分享                                 | 4    |           | 合理美观       | 1    |           |
|          |          | 收藏添加删除                         | 4    |           | 合理美观       | 1    |           |
|          |          | 所有收藏列表                         | 4    |           | 合理美观       | 1    |           |
|          | 推荐     | 看过新闻推荐                         | 8    |           | 推荐准确       | 2    |           |
| 附加     | 夜间模式 |                                      |      |           |                |      |           |
|          |          |                                      |      |           |                |      |           |
|          |          |                                      |      |           |                |      |           |
|          |          |                                      |      |           |                |      |           |
|          |          |                                      |      |           |                |      |           |




