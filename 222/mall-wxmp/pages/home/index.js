// pages/home/index.js
import utils from '../../utils/util.js'
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    kw: '',
    slidePics: [],
    hotProducts: [],
    recProducts: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.loadHot()
    this.loadRec()
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
    this.loadSlidePictures()
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {
    
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {
    this.loadHot()
    this.loadRec()
  },

  loadSlidePictures: function(){
    let that = this
    utils.request(
      app.globalData.apiBaseUrl+'/index/slide/pictures',
      {}
    ).then(res=>{
      wx.stopPullDownRefresh()
      that.setData({
        slidePics: res.data,
      })
    })
  },

  loadHot: function(){
    let that = this
    utils.request(
      app.globalData.apiBaseUrl+'/index/hot/products',
      {}
    ).then(res=>{
      wx.stopPullDownRefresh()
      that.setData({
        hotProducts: res.data,
      })
    })
  },

  loadRec: function(){
    let that = this
    utils.request(
      app.globalData.apiBaseUrl+'/index/rec/products',
      {}
    ).then(res=>{
      wx.stopPullDownRefresh()
      that.setData({
        recProducts: res.data,
      })
    })
  },
  
  toDetailPage: function(e){
    let bid = e.currentTarget.dataset.pid
    console.log('跳转到详情页',bid)
    wx.navigateTo({
      url: '/pages/product/detail?pid='+bid,
    })
  },

  bindKwInput: function(e){
    this.setData({ kw: e.detail.value })
  },

  onClearSearch: function(e){
    this.setData({kw: ''})
  },
  
  onSearch: function(e){
    app.globalData.kw = this.data.kw
    wx.navigateTo({
      url: '/pages/product/search-list'
    })
  },
})