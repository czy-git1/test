// pages/discover/news/news-detail.js
const app = getApp()
import utils from '../../utils/util.js'
import Notify from '@vant/weapp/notify/notify'
import Dialog from '@vant/weapp/dialog/dialog';

Page({

  /**
   * 页面的初始数据
   */
  data: {
    productId: '',
    product: {},
    comments: [],
    commentContent: '',
    userInfo: {},
    cartCount: 0,
    favCount: 0
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    let bid = options.pid
    this.setData({productId: bid})
    this.loadProductDetail()
    // this.initUserInfo()
    this.loadCartCount()
    this.checkFav()
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

  //读取商品信息
  loadProductDetail: function(){
    let that = this
    utils.request(
      app.globalData.apiBaseUrl+'/product/detail/'+this.data.productId,
      {}
    ).then(res=>{
      wx.stopPullDownRefresh()
      that.setData({
        product: res.data,
      })
    })
  },

  //读取当前购物车商品数量
  loadCartCount: function(){
    let that = this
    utils.request(
      app.globalData.apiBaseUrl+'/cart/count',
      {}
    ).then(res=>{
      that.setData({
        cartCount: res.data,
      })
    })
  },

  //检查是否已经收藏该商品
  checkFav: function(){
    let that = this
    utils.request(
      app.globalData.apiBaseUrl+'/fav/check/'+this.data.productId,
      {},
      'GET'
    ).then(res=>{
      that.setData({
        favCount: res.data,
      })
    })
  },

  //收藏商品
  onFav: function(e){
    let cid = this.data.productId
    let that = this
    utils.request(
      app.globalData.apiBaseUrl+'/cart/fav',
      {productId: cid}
    ).then(res=>{
      wx.showToast({
        title: '已收藏',
        icon: 'success'
      })
      that.setData({
        favCount: 1,
      })
    })
  },

  onRemoveFav: function(e){
    let that = this
    let fid = this.data.productId
    utils.request(
      app.globalData.apiBaseUrl+'/fav/remove2/'+fid,
      {}
    ).then(res=>{
      wx.showToast({
        title: '已取消收藏',
        icon: 'success',
      })
      that.setData({
        favCount: 0,
      })
    })
  },

  //浏览大图
  showLargeImage(e) {
    let currIdx = e.currentTarget.dataset.idx //被点击的图片在该笔记中的下标，比如有五张图片，被点的是第3张，currIdx则为2
    let curr = ''
    let imgList = []
    for(var idx=0; idx<this.data.product.picList.length; idx++){
      imgList.push(this.data.product.picList[idx])
      if(currIdx==idx){
          curr = this.data.product.picList[idx]
      }
    }
    wx.previewImage({
        urls: imgList, //需要预览的图片http链接列表，注意是数组
        current: curr, // 当前显示图片的http链接，默认是第一个
        success: function (res) { },
        fail: function (res) { },
        complete: function (res) { },
      })
  },

  //进入预览订单页面
  toOrderConfirm: function(e){
    wx.navigateTo({
      url: '/pages/order/comfirm?pid='+this.data.productId+"&otype=2",
    })
  },

  //加入购物车
  addToCart: function(e){
    let that = this
    let req = {
      productId: this.data.productId
    }
    utils.request(
      app.globalData.apiBaseUrl+'/cart/add',
      req
    ).then(res=>{
      wx.showToast({
        title: '已加入购物车',
        icon: 'success',
        complete: that.loadCartCount()
      })
    })
  },

  switchToCart: function(){
    wx.switchTab({
      url: '/pages/cart/cart',
    })
  },
  
  //加载当前登录用户的信息
  initUserInfo: function(){
    let that = this
    //到服务端实时load一下用户信息
    utils.request(
      app.globalData.apiBaseUrl+'/product/load/user',
      {},
      'POST'
    ).then(res=>{
      console.log(res)
      that.setData({userInfo: res.data, notLogin: false})
      wx.setStorageSync("LOGIN_USER", res.data)
      app.globalData.userInfo = res.data 
      wx.hideLoading()
    })
  },

})