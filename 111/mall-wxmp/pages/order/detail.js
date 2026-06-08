// pages/order/detail.js
const app = getApp()
import utils from '../../utils/util.js'
import Dialog from '@vant/weapp/dialog/dialog';
Page({

  /**
   * 页面的初始数据
   */
  data: {
    orderInfo: {},
    orderId: '',
    userInfo: {}
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    let orderId = options.orderid
    this.setData({
      orderId: orderId,
      userInfo: app.globalData.userInfo
    })
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
    this.loadOrder()
  },

  //读取订单
  loadOrder: function(){
    let that = this
    utils.request(
      app.globalData.apiBaseUrl+'/order/detail/'+this.data.orderId,
      {}, 'GET'
    ).then(res=>{
      wx.stopPullDownRefresh()
      let oi = res.data
      oi.totalPrice = oi.totalPrice.toFixed(2)
      that.setData({
        orderInfo: oi,
      })
    })
  },

  //取消订单
  onCancelOrder: function(){
    let that = this
    Dialog.confirm({
      title: '提示',
      message: '确定要取消该订单吗？',
    }).then(() => {
      //确认
      utils.request(
        app.globalData.apiBaseUrl+'/order/cancel/'+this.data.orderId,
        {}
      ).then(res=>{
        Dialog.alert({
          title: '提示',
          message: '订单已取消',
        }).then(() => {
          that.loadOrder()
        })
      })
    }).catch(() => {
      //取消
    });
  },

  //确认订单
  onConfirmOrder: function(){
    let that = this
    Dialog.confirm({
      title: '提示',
      message: '需要确认该订单吗？',
    }).then(() => {
      //确认
      utils.request(
        app.globalData.apiBaseUrl+'/order/confirm?orderId='+this.data.orderId,
        {}
      ).then(res=>{
        Dialog.alert({
          title: '提示',
          message: '订单已确认',
        }).then(() => {
          that.loadOrder()
        })
      })
    }).catch(() => {
      //取消
    });
  },

  //完成订单
  onCompleteOrder: function(){
    let that = this
    Dialog.confirm({
      title: '提示',
      message: '确定已收货了吗？',
    }).then(() => {
      //确认
      utils.request(
        app.globalData.apiBaseUrl+'/order/complete/'+this.data.orderId,
        {}
      ).then(res=>{
        Dialog.alert({
          title: '提示',
          message: '订单已完成',
        }).then(() => {
          that.loadOrder()
        })
      })
    }).catch(() => {
      //取消
    });
  },

  //去评论
  toFeedback: function(e){
    let iid = e.currentTarget.dataset.iid
    let pid = e.currentTarget.dataset.pid
    wx.navigateTo({
      url: '/pages/order/feedback?oid='+this.data.orderId+"&pid="+pid+"&iid="+iid,
    })
  },

  onBuyAgain: function(e){
    let pid = e.currentTarget.dataset.pid
    wx.navigateTo({
      url: '/pages/order/comfirm?pid='+pid+"&otype=2",
    })
  },

  toPay: function(){
    wx.navigateTo({
      url: '/pages/order/pay?amount='+this.data.orderInfo.totalPrice+'&orderid='+this.data.orderInfo.orderId,
    })
  },

  //下拉刷新
  onPullDownRefresh(){
    this.setData({orderInfo: null})
    this.loadOrder()
  },
})