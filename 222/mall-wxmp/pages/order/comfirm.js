// pages/user/info.js
var app = getApp()
import utils from '../../utils/util.js'
import Dialog from '@vant/weapp/dialog/dialog';
Page({

  /**
   * 页面的初始数据
   */
  data: {
    order: {
      remark: "",
      productId: "",
      addressId: "",
      orderCode: ''
    },
    product: {},
    addressList: [],
    pindex: 0,
    productId: '',
    orderType: 1,
    loading: true,
    previewOrder: {}
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log('订单页面参数：', options)
    let pid = options.pid
    let orderType = options.otype
    console.log(options)
    this.setData({
        productId: pid,
        orderType: orderType
      })
    this.previewOrder()
    this.loadAddressList()
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

  //生成预览订单
  previewOrder: function(){
    let that = this
    utils.request(
      app.globalData.apiBaseUrl+'/order/preview',
      {orderType: that.data.orderType, productId: that.data.productId}
    ).then(res=>{
      that.setData({
        previewOrder: res.data,
        'order.orderCode': res.data.orderCode
      })
      // console.log(res.data)
    })
  },

  //读取我的地址列表
  loadAddressList: function(){
    let that = this
    utils.request(
      app.globalData.apiBaseUrl+'/address/list',
      {}
    ).then(res=>{
      that.setData({addressList: res.data, 'order.addressId': res.data[0].addressId})
    })
  },

  bindRealNameChange: function(e){
    this.setData({ 'order.contact': e.detail.value})
  },

  bindMobileChange: function (e) {
    this.setData({ 'order.mobile': e.detail.value })
  },

  bindAddressInput: function (e) {
    this.setData({ 'order.address': e.detail.value })
  },

  bindRemarkChange: function (e) {
    this.setData({ 'order.remark': e.detail.value })
  },

  bindAddressChange: function(e) {
    let idx = e.detail.value
    this.setData({'order.addressId': this.data.addressList[idx].addressId, pindex: idx})
  },

  //提交表单
  onSubmitOrder: function(e){
    if(this.data.order.addressId==null||this.data.order.addressId==''){
      Dialog.alert({
        title: '提示',
        message: '请添加收货地址',
      })
      return
    }
    wx.showLoading({
      title: '正在提交',
    })
    let that = this
    let req = this.data.order
    utils.request(
      app.globalData.apiBaseUrl+'/order/save',
      req
    ).then(res=>{
      wx.hideLoading()
      let msg = '订单已提交，请支付！'
      Dialog.alert({
        title: '提示',
        message: msg,
      }).then(() => {
        console.log(res)
        wx.redirectTo({
          url: '/pages/order/pay?amount='+res.data.totalPrice+'&orderid='+res.data.orderId,
        })
      });
    })
  },

  addNewAddress: function(e){
    wx.navigateTo({
      url: '/pages/user/address',
    })
  }
})