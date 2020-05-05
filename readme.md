### 框架
MVVM in Android

min version: 7.0.0(api=24)，否則getReplies()無法日期排序

### 分支說明
 - 強化MVVM的觀念，增強Model的功能
 - 更好的commit格式

### 學習重點
 - MVVM與物件導向實作
 - fragment, **Navigation-fragment**, AppBar新功能實戰


### 常用功能
###### 已加入
 - 預覽reply的引用內容(ex. >>12345678 (我的家庭真可愛...) )
 - 樹狀留言圖
 
###### 尚未加入
 - 偵測連結開啟App
 - Po文
 - 長按留言
 - 備份
 - 收藏貼文、閱讀歷史
 - Open Url in Browser
 - 肉餅臉產生器
 
###### 未來展望
 - 為使用者推薦K島上常用圖片(搜尋、上傳)
 - 追蹤文章
 - 個人化推薦文章(自行設定關鍵字)
 
### 目前版面
 - Komica:
   - [x] komica.org ( 
      - sora: [綜合,男性角色,短片2,寫真],
      - 2cat: [新番捏他,新番實況,漫畫,動畫,萌,車],
      - tomo: [四格],
      - luna: [女性角色,歡樂惡搞,GIF,Vtuber],
      - aqua: [蘿蔔,鋼普拉,影視,特攝,軍武,中性角色,遊戲速報,飲食,小說,遊戲王,奇幻/科幻,電腦/消費電子,塗鴉王國,新聞,布袋戲,紙牌,網路遊戲]
      )
   - [ ] vi.anacel.com (Figure/GK)
   - [ ] acgspace.wsfun.com (艦隊收藏)
   - [ ] komica.dbfoxtw.me (人外)
   - [ ] idolma.ster.tw (Idolmaster)
   - [ ] komica.yucie.net (格鬥遊戲)
   - [ ] kagaminerin.org (3D STG、動作遊戲)
   - [ ] p.komica.acg.club.tw (兄貴)
   - [ ] 2cat.org (碧藍幻想、手機遊戲、Azur Lane、網頁遊戲)
 - Komica2
   - [ ] komica2.net (二次裡Ａ,遊戲裡,三次元裡,二次裡Ｂ,3D裡,Alicesoft,足襪,YURI,惡搞裡,Figure 裡,成人音聲,改造裡,交易合購裡,玩偶裡,塗鴉裡,壁紙裡,獸裡,寫作裡,YAOI,雜談,小說裡,宣傳裡,精華裡,管理室,寫作資料庫) 
   - [ ] 2cat.org (GIF裡,動畫裡,高解析裡,成人玩具,知識裡,偽娘裡,東方裡,)
   - [ ] p.komica.acg.club.tw (觸手裡)
   - [ ] cyber.boguspix.com (機娘裡)
   - [ ] majeur.zawarudo.org (詢問裡)
 
### 下載

 
### 額外套件 (dependencies)
    implementation 'com.amitshekhar.android:android-networking:1.0.2'
    implementation 'org.jsoup:jsoup:1.12.1'
    implementation 'com.github.bumptech.glide:glide:4.9.0'
    implementation 'com.github.clans:fab:1.6.4'
    implementation 'com.google.code.gson:gson:2.8.6'
    
### Commit記錄方式
 - Fix: 修復原本的Bug
 - Update: 做了結構上的重大改變，不影響其他功能
 - Add: 新增功能
 - Del: 刪除功能

### 參考來源
 - 搜尋
    - [EhViewer](https://github.com/seven332/EhViewer)
 - 風格
    - [A島匿名板手機端](https://loyea.com/adnmb/download/latest)
 - 功能發想
    - [肉餅臉粗乃丸產生器(alpha)](https://github.com/send-tree-pay/htm170527)
    - [Pitt](https://play.google.com/store/apps/details?id=com.ihad.ptt)
 - 其他
    - [Komica20160704/homu-api](https://homu.homu-api.com/api) and [Repository](https://github.com/Komica20160704/homu-api)
    - [Komica+](https://github.com/TakumaMochizuki/Komica)
