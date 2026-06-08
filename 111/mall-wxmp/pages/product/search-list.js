// pages/discover/news/news-list.js
const app = getApp()
import utils from '../../utils/util.js'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    productList: null,
    noMore: false,
    kw: '',
    noSearch: false,
    categoryId: '',
    totalrecords: null,
    page: null,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    let kw = app.globalData.kw
    let that = this
    this.setData({kw: kw}, ()=>{
      that.loadProductList()
    })
  },

  onShow(){
    // this.loadProductList()
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {
    this.setData({productList: null})
    this.loadProductList()
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {
    
  },

  //查询列表信息
  loadProductList: function(){
    let that = this
    let req = {kw: this.data.kw}
    utils.request(
      app.globalData.apiBaseUrl+'/product/search',
      req
    ).then(res=>{
      wx.stopPullDownRefresh()
      that.setData({
        productList: res.data,
        totalrecords: res.data.totalrecords,
        page: res.data.page
      })
      that.setData({noMore: true})
    })
  },

  bindKwInput: function(e){
    this.setData({ kw: e.detail.value })
  },

  toDetailPage: function(e){
    let bid = e.currentTarget.dataset.pid
    console.log('跳转到详情页',bid)
    wx.navigateTo({
      url: '/pages/product/detail?pid='+bid,
    })
  },

  onHide(){
    this.setData({kw: ''})
  },

  onClearSearch: function(e){
    this.setData({kw: ''})
  },
})