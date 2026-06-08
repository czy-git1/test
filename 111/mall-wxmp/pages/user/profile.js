var app = getApp()
import utils from '../../utils/util.js'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userInfo: {},
    newMsg: 0,
    notLogin: true
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // let isLogin = util.checkLogin()
    // if(!isLogin){
    //   //进入授权登录页面
    //   wx.redirectTo({
    //     url: '/pages/user/login/login',
    //   })
    // }
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
    let isLogin = utils.checkLogin()
    if(!isLogin){
      //进入授权登录页面
      wx.redirectTo({
        url: '/pages/user/login/login',
      })
    }
    if(wx.getStorageSync('LOGIN_USER')!=''){
      this.initUserInfo()
    }
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

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  onLogout: function(){
    wx.showModal({
      title: '提示',
      content: '确定要退出登录吗？',
      success(res){
        if(res.confirm){
          app.globalData.userInfo = null
          wx.removeStorage({
            key: 'LOGIN_USER'
          })
          wx.removeStorage({
            key: 'TOKEN'
          })
          wx.switchTab({
            url: '/pages/home/index',
          })
        }
      }
    })
  },

  toEditPage: function(){
    wx.navigateTo({
      url: '/pages/user/info',
    })
  }
})