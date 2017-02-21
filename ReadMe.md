## 这是一个使用LeanCloud对客户端进行远程控制的Demo，通过发送推送消息的形式改变UI布局和实现业务逻辑。
#### app使用leancloud实现各种操作，需要在application中填写正确的key和那个什么。。

- 可以在mainactivity中填入唯一的userid并按下下方按钮来指定设备发送消息。
- leancloud发送消息选择JSON格式。

- 格式要求：
```
{
  "alert": {
      "orderNumber":"986222",
      "totalPrice":"100",
      "payedPrice":"100",
      "changePrice":"0",
      "cashierName":"Lee",
      "cashierNumber":"007",
      "goodsList":[
      {
          "name":"羽绒服",
          "number":"123123222",
          "price":"998"
      },
      {"name":"羽绒服",
          "number":"123123222",
          "price":"997"}
      ],
      "vipInfo":{
          "name":"Lee",
          "imageUrl":"http://....."
      },
      "qrCodeInfo":{
          "alipayQrCodeUrl":"",
          "wechatQrCodeUrl":""
      }
  },
  "title": "showListOfGoods",
  "action":"com.etam.cashier"
}
```

- 以上为预定格式，如有新的信息需要传递则需要修改com.etam.cashier.bean.LeanCloudMessage 类来同步。

#### 其中title的值表示需要执行的操作，暂定有6个操作如下：
1. showListOfGoods  --展开商品列表
2. hideListOfGoods  --关闭商品列表
3. showVipCode      --开启扫码功能，识别VIP条码
4. hideVipCode      --关闭扫码功能
5. showQrCode       --显示支付的二维码
6. hideQrCode       --隐藏支付的二维码

- 备注：
- 在小米华为等设备上未实现推送功能。
- 推送格式错误可能导致app崩溃，虽然已经做过处理。
- 其他看注释吧...
