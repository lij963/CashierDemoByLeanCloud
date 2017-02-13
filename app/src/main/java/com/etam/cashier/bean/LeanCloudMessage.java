package com.etam.cashier.bean;

import java.util.List;

/**
 * Author:  admin
 * Date:    2017/2/9.
 * Description:
 */

public class LeanCloudMessage {


    /*预定JSON格式
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
  "title": "showQrCode",
  "action":"com.etam.cashier"
}
 */

    /**
     * alert : {"goodsList":[{"name":"羽绒服","number":"123123222","price":"998"},{"name":"羽绒服","number":"123123222","price":"998"}],"vipInfo":{"name":"Lee","imageUrl":"http://....."},"qrCodeInfo":{"alipayQrCodeUrl":"","wechatQrCodeUrl":""}}
     * title : showListOfGoods
     * action : com.etam.cashier
     */

    private AlertBean alert;
    private String title;
    private String action;


    public AlertBean getAlert() {
        return alert;
    }

    public void setAlert(AlertBean alert) {
        this.alert = alert;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public static class AlertBean {
        /**
         * goodsList : [{"name":"羽绒服","number":"123123222","price":"998"},{"name":"羽绒服","number":"123123222","price":"998"}]
         * vipInfo : {"name":"Lee","imageUrl":"http://....."}
         * qrCodeInfo : {"alipayQrCodeUrl":"","wechatQrCodeUrl":""}
         */
        private String orderNumber;
        private String totalPrice;
        private String payedPrice;
        private String changePrice;
        private String cashierName;
        private String cashierNumber;
        private VipInfoBean vipInfo;
        private QrCodeInfoBean qrCodeInfo;
        private List<GoodsListBean> goodsList;

        public String getCashierName() {
            return cashierName;
        }

        public void setCashierName(String cashierName) {
            this.cashierName = cashierName;
        }

        public String getCashierNumber() {
            return cashierNumber;
        }

        public void setCashierNumber(String cashierNumber) {
            this.cashierNumber = cashierNumber;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getPayedPrice() {
            return payedPrice;
        }

        public void setPayedPrice(String payedPrice) {
            this.payedPrice = payedPrice;
        }

        public String getChangePrice() {
            return changePrice;
        }

        public void setChangePrice(String changePrice) {
            this.changePrice = changePrice;
        }

        public VipInfoBean getVipInfo() {
            return vipInfo;
        }

        public void setVipInfo(VipInfoBean vipInfo) {
            this.vipInfo = vipInfo;
        }

        public QrCodeInfoBean getQrCodeInfo() {
            return qrCodeInfo;
        }

        public void setQrCodeInfo(QrCodeInfoBean qrCodeInfo) {
            this.qrCodeInfo = qrCodeInfo;
        }

        public List<GoodsListBean> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(List<GoodsListBean> goodsList) {
            this.goodsList = goodsList;
        }

        public static class VipInfoBean {
            /**
             * name : Lee
             * imageUrl : http://.....
             */

            private String name;
            private String imageUrl;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }
        }

        public static class QrCodeInfoBean {
            /**
             * alipayQrCodeUrl :
             * wechatQrCodeUrl :
             */

            private String alipayQrCodeUrl;
            private String wechatQrCodeUrl;

            public String getAlipayQrCodeUrl() {
                return alipayQrCodeUrl;
            }

            public void setAlipayQrCodeUrl(String alipayQrCodeUrl) {
                this.alipayQrCodeUrl = alipayQrCodeUrl;
            }

            public String getWechatQrCodeUrl() {
                return wechatQrCodeUrl;
            }

            public void setWechatQrCodeUrl(String wechatQrCodeUrl) {
                this.wechatQrCodeUrl = wechatQrCodeUrl;
            }
        }

        public static class GoodsListBean {
            /**
             * name : 羽绒服
             * number : 123123222
             * price : 998
             */

            private String name;
            private String number;
            private String price;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }
        }
    }


}
