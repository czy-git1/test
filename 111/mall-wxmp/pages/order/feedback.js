// pages/publish/publish.js
var app = getApp()
import utils from '../../utils/util.js'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    productId: null,
    userInfo: {},
    fb: {
      stars: 0,
      orderItemId: null,
      productId: null,
      tOrderId: null,
      content: '',
    },
    product: {}
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let isLogin = utils.checkLogin()
    if(!isLogin){
      //进入授权登录页面
      wx.redirectTo({
        url: '/pages/user/login/login',
      })
    }
    let productId = options.pid
    let oid = options.oid
    let iid = options.iid
    this.setData({
      productId: productId,
      'fb.tOrderId': oid,
      'fb.orderItemId': iid,
      userInfo: app.globalData.userInfo
    })
    this.loadProductInfo()
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    
  },

  bindFieldRemarkInput: function(e){
    this.setData({ 'fb.content': e.detail.value })
  },

  orderId(event) {
    console.log('当前值：' + event.detail)
    this.setData({'fb.stars': event.detail})
  },

  /**
   * 提交表单
   */
  publishBlog: function(){
    if(this.data.fb.content==''){
      wx.showToast({title:'请输入内容', icon: 'error'})
      return
    }
    wx.showLoading({
      title: '加载中',
    })
    let req = this.data.fb
    utils.request(
      app.globalData.apiBaseUrl+'/order/feedback/save',
      req,
      'POST'
    ).then(res=>{
      console.log(res)
      if(res.code=='500'){
        wx.showModal({
          title: '提示',
          content: res.msg,
          showCancel: false,
          complete: (res) => {
            wx.navigateBack()
          }
        })
      }else{
        wx.showModal({
          title: '提示',
          content: '评价已发布',
          showCancel: false,
          complete: (res) => {
            wx.navigateBack()
          }
        })
      }
    })
  },
  
  //读取订单商品
  loadProductInfo: function(){
    let that = this
    utils.request(
      app.globalData.apiBaseUrl+'/product/detail/'+this.data.productId,
      {}
    ).then(res=>{
      wx.stopPullDownRefresh()
      let oi = res.data
      that.setData({
        product: oi,
        'fb.productId': oi.productId,
      })
      if(oi.orderStatus==4){
        that.loadFeedback()
      }
    })
  },

  loadFeedback: function(){
    let that = this
    utils.request(
      app.globalData.apiBaseUrl+'/order/feedback/detail?orderId='+this.data.orderId,
      {}
    ).then(res=>{
      wx.stopPullDownRefresh()
      that.setData({
        fb: res.data
      })
    })
  }
})