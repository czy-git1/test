// pages/product/category.js
import utils from '../../utils/util.js'
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    categoryList: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {

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

  loadSlidePictures: function(){
    let that = this
    utils.request(
      app.globalData.apiBaseUrl+'/product/category/list',
      {}
    ).then(res=>{
      wx.stopPullDownRefresh()
      that.setData({
        categoryList: res.data,
      })
    })
  },

  //进入该分类的商品列表页面
  toCategoryProducts: function(e){
    let cid = e.currentTarget.dataset.cid
    wx.navigateTo({
      url: '/pages/product/product-list?cid='+cid,
    })
  }

})