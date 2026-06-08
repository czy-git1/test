// pages/order/pay.js
var app = getApp()
import utils from '../../utils/util.js'
import Dialog from '@vant/weapp/dialog/dialog';
Page({

  /**
   * 页面的初始数据
   */
  data: {
    amount: 0,
    orderId: null
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    console.log(options)
    let amt = options.amount
    let orderId = options.orderid
    this.setData({amount: amt, orderId: orderId})
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {

  },

  onCloseAuth: function(){
    this.setData({showAuth: false})
    wx.redirectTo({
      url: '/pages/order/myorders',
    })
  },

  //支付订单
  doPay: function(){
    let that = this
  
    utils.request(
      app.globalData.apiBaseUrl+'/order/pay?orderId='+this.data.orderId,
      {}
    ).then(res=>{
      Dialog.alert({
        title: '提示',
        message: '订单已支付',
      }).then(() => {
        wx.reLaunch({
          url: '/pages/order/myorders',
        })
      })
    })
  }

})