// miniprogram/pages/login/login.js
var app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    mobile: '18912341234',
    pwd: '123456',
    showAuth: false
  },

  onLoad: function(options){
   //弹出登录授权框
    this.setData({showAuth: true})
  },

  //向服务端发起请求，获取openid，关联到数据库用户表的记录，如果没有则创建新用户
  userLogin(){
    let that = this
    wx.showLoading({
      title: '正在在加载',
    })
    wx.login({
      success (res) {
        if (res.code) {
          console.log(res)
          //发起网络请求
          wx.request({
            url: app.globalData.apiBaseUrl+'/user/getopenid',
            method: 'GET',
            data: {
              code: res.code
            },
            success(res){
              //授权登录成功
              console.log("得到openid响应:",res)
              wx.hideLoading()
              if(res.data.code==200){
                wx.setStorageSync("TOKEN", res.data.data.token)
                //data: res.data.data.openid
                wx.setStorageSync("LOGIN_USER", res.data.data.user)
                let currTime = (new Date()).getTime()
                wx.setStorageSync('LAST_LOGIN_TIME', currTime)
                wx.switchTab({
                  url: '/pages/user/profile',
                })
              }
            },
            fail(res){
              console.log("获取openid失败，重试！")
              wx.hideLoading()
            }
          })
        } else {
          console.log('登录失败！' + res.errMsg)
        }
        that.setData({showAuth: false})
      }
    })
  },

  onCloseAuth: function(){
    this.setData({showAuth: false})
    wx.switchTab({
      url: '/pages/index/index',
    })
  },

  toReg: function(){
    wx.navigateTo({
      url: '/pages/user/regist/regist'
    })
  }, 
  toValidate: function(){
    wx.navigateTo({
      url: '/pages/user/validate/validate'
    })
  }, 

  inputMobile: function(e){
    this.setData({ mobile: e.detail.value })
  },

  inputPwd: function (e) {
    this.setData({ pwd: e.detail.value })
  },
  
  toValidate: function(e){
    wx.navigateTo({
      url: '/pages/user/profile'
    })
  },



})