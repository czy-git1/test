// pages/user/address.js
const app = getApp()
import utils from '../../utils/util.js'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    region: [],
    newAddress: {
      isDefault: false,
      area: '-请选择-',
      detail: '',
      contact: '',
      mobile: ''
    },
    showEdit: false,
    addressList: [],
    addOrModify: 'add'
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
    this.loadAddressList()
  },

  onUnload(){
    console.log('返回')
  },

  //读取我的地址列表
  loadAddressList: function(){
    let that = this
    utils.request(
      app.globalData.apiBaseUrl+'/address/list',
      {}
    ).then(res=>{
      wx.stopPullDownRefresh()
      that.setData({addressList: res.data})
    })
  },

  //保存或更新地址
  onSaveAddress: function(e){
    let that = this
    let req = this.data.newAddress
    utils.request(
      app.globalData.apiBaseUrl+'/address/save',
      req
    ).then(res=>{
      wx.showToast({
        title: '已保存',
        icon: 'success',
        complete: ()=>{
          let formData = {
            isDefault: false,
            area: '-请选择-',
            detail: '',
            contact: '',
            mobile: ''
          }
          that.setData({showEdit: false, newAddress: formData})
          that.loadAddressList()
        }
      })
    })
  },

  showEditForm: function(){
    this.setData({showEdit: true})
  },

  onCancleEdit: function(){
    this.setData({showEdit: false})
  },

  editAddress: function(e){
    let aid = e.currentTarget.dataset.aid
    let that = this
    utils.request(
      app.globalData.apiBaseUrl+'/address/detail/'+aid,
      {}
    ).then(res=>{
      that.setData({showEdit: true, newAddress: res.data})
    })
  },

  bindRegionChange: function (e) {
    let add = e.detail.value.join("")
    this.setData({
      region: e.detail.value,
      'newAddress.area': add
    })
  },

  //勾选默认
  onChangeSelect(e){
    let newStatus = true
    if(this.data.newAddress.isDefault){
      newStatus = false
    }
    this.setData({'newAddress.isDefault': newStatus})
  },

  bindDetailInput: function(e){
    this.setData({ 'newAddress.detail': e.detail.value})
  },

  bindContactInput: function(e){
    this.setData({ 'newAddress.contact': e.detail.value})
  },

  bindMobileInput: function(e){
    this.setData({ 'newAddress.mobile': e.detail.value})
  },

  onBeforeLeave(res) {
    const that = this;
    wx.showModal({
      title: '提示',
      content: '确认要放弃保存当前修改内容吗？',
      success: function (res) {
        console.log('点击什么？', res)
        if (res.confirm) {
          //是
          that.setData({
            showEdit: false,
          });
        } else if (res.cancel) {
          //否
          that.setData({
            showEdit: true,
          });
        }
      }
    })
  }
})