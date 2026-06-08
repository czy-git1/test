// pages/user/fav/fav-list.js
const app = getApp()
import utils from '../../utils/util.js'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    messageList: null,
    noMore: false
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.loadMessageList()
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
  
  //聊天记录历史列表
  loadMessageList: function(){
    let that = this
    let req = {}
    utils.request(
      app.globalData.apiBaseUrl+'/msg/list',
      req
    ).then(res=>{
      that.setData({
        messageList: res.data
      })
    })
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {
    if(this.data.messageList==null || this.data.messageList.currentpage==this.data.messageList.totalpage){
      this.setData({noMore:true})
      return
    }
    let tmp = this.data.messageList
    tmp.currentpage=tmp.currentpage+1
    this.setData({messageList: tmp})
    this.loadmessageList()
  },

  //进入聊天页面
  toChat: function(e){
    console.log(e.currentTarget.dataset)
    let otherid = e.currentTarget.dataset.otherid
    wx.navigateTo({
      url: '/pages/message/chat?rid='+otherid,
    })
  }
})