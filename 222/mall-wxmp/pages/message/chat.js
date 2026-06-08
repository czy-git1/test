// pages/message/msg-list.js
const app = getApp()
import utils from '../../utils/util.js'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    receiveId: null,
    msgList: null,
    msgContent: '',
    me: {},
    other: {},
    lastLoadTime: -1,
    msgInterval: {}
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    let rid = options.rid
    this.setData({receiveId: rid})
    this.loadMessage()
    this.loadUserInfo()
    this.loadMsgInterval()
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

  bindContentInput: function(e){
    this.setData({ 'photoService.expPrice': e.detail.value })
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {
    
  },

  onUnload(){
    console.log('清除读取消息定时器')
    clearInterval(this.data.msgInterval)
  },

  loadMsgInterval: function(){
    let that = this
    //定时器，用于轮询新消息
    let timer = setInterval(() => {
      // let timestamp = (new Date()).getTime();
      // that.setData({lastLoadTime: timestamp})
      that.loadMessage()
    }, 1500)  
    this.setData({msgInterval: timer})
  },

  //读取双方的头像信息
  loadUserInfo: function(){
    let that = this
    utils.request(
      app.globalData.apiBaseUrl+'/msg/load/user?otherId='+this.data.receiveId,
      {},
      'POST'
    ).then(res=>{
      console.log(res)
      that.setData({me: res.data.me, other: res.data.other})
    })
  },
  
  //查询消息列表
  loadMessage: function(){
    let that = this
    let req = {otherUserId: this.data.receiveId, lastLoadTime: this.data.lastLoadTime}
    if(this.data.msgList!=null){
      req.total=this.data.msgList.totalrecord,
      req.page=this.data.msgList.currentpage
    }
    utils.request(
      app.globalData.apiBaseUrl+'/msg/chat/list',
      req,
      "POST",
      "加载中...", 
      "application/x-www-form-urlencoded", 
      false
    ).then(res=>{
      wx.stopPullDownRefresh()
      that.setData({lastLoadTime: (new Date()).getTime()})
      let existList = that.data.msgList
        if(existList!=null&&res.data.records.length>0){
          existList.records=existList.records.concat(res.data.records)
          that.setData({
            msgList: existList
          })
        }else if(res.data.records.length>0){
          that.setData({
            msgList: res.data,
            totalrecords: res.data.totalrecords,
            page: res.data.page
          })
        }
    })
  },

  //发送消息
  sendMessgae: function(){
    let that = this
    let req = {
      receiveId: this.data.receiveId,
      isRead: 0,
      content: this.data.msgContent
    }
    utils.request(
      app.globalData.apiBaseUrl+'/msg/send',
      req,
      'POST'
    ).then(res=>{
      console.log(res)
      that.setData({msgContent: ''})
    })
  },

  bindMsgContentInput: function(e){
    this.setData({ msgContent: e.detail.value })
  }
})