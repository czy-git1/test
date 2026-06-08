// pages/user/fav/fav-list.js
const app = getApp()
import utils from '../../../utils/util.js'
import Dialog from '@vant/weapp/dialog/dialog';
Page({

  /**
   * 页面的初始数据
   */
  data: {
    productList: null,
    noMore: false
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.loadProductList()
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
  
  loadProductList: function(){
    let that = this
    let req = {}
    utils.request(
      app.globalData.apiBaseUrl+'/fav/list',
      req
    ).then(res=>{
      wx.stopPullDownRefresh()
      that.setData({
        productList: res.data
      })
    })
  },

  onRemoveFav: function(e){
    let that = this
    let fid = e.currentTarget.dataset.fid
    Dialog.confirm({
      title: '提示',
      // message: '商品数量小于1，确定将该商品移出收藏夹吗？',
      message: '商品数量小于1',
    }).then(() => {
      //确认
      utils.request(
        app.globalData.apiBaseUrl+'/fav/remove/'+fid,
        {}
      ).then(res=>{
        wx.showToast({
          title: '已移除',
          icon: 'success',
          complete: ()=>{
            that.loadProductList()
          }
        })
      })
    })
  },

  toBlogDetail: function(e){
    let bid = e.currentTarget.dataset.pid
    console.log('跳转到详情页',bid)
    wx.navigateTo({
      url: '/pages/product/detail?pid='+bid,
    })
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {
    if(this.data.productList==null || this.data.productList.currentpage==this.data.productList.totalpage){
      this.setData({noMore:true})
      return
    }
    let tmp = this.data.productList
    tmp.currentpage=tmp.currentpage+1
    this.setData({productList: tmp})
    this.loadProductList()
  },

  onBuyAgain: function(e){
    let pid = e.currentTarget.dataset.pid
    wx.navigateTo({
      url: '/pages/order/comfirm?pid='+pid+"&otype=2",
    })
  },

  //下拉刷新
  onPullDownRefresh(){
    this.setData({productList: null})
    this.loadProductList()
  },
})