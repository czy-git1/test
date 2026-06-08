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
    let cid = options.cid
    let that = this
    this.setData({categoryId: cid}, ()=>{
      that.loadProductList()
    })
    this.loadCategory()
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
    if(this.data.productList==null || this.data.productList.currentpage==this.data.productList.totalpage){
      this.setData({noMore:true})
      return
    }
    let tmp = this.data.productList
    tmp.currentpage=tmp.currentpage+1
    this.setData({productList: tmp})
    this.loadProductList()
  },

  loadCategory: function(){
    let that = this
    utils.request(
      app.globalData.apiBaseUrl+'/product/category/'+that.data.categoryId,
      {}
    ).then(res=>{
      that.setData({category: res.data})
      wx.setNavigationBarTitle({
        title: res.data.name
      })
    })
  },

  //查询列表信息
  loadProductList: function(){
    let that = this
    let req = {category: this.data.categoryId}
    if(this.data.productList!=null){
      req.total=this.data.productList.totalrecord,
      req.page=this.data.productList.currentpage
    }
    utils.request(
      app.globalData.apiBaseUrl+'/product/list',
      req
    ).then(res=>{
      wx.stopPullDownRefresh()
      // console.log('查询结果：', res)
      let existList = that.data.productList
      if(existList!=null){
        existList.records=existList.records.concat(res.data.records)
        that.setData({
          productList: existList
        })
      }else{
        that.setData({
          productList: res.data,
          totalrecords: res.data.totalrecords,
          page: res.data.page
        })
      }
      let noMore = false
      if(res.data.page==res.data.totalpage||res.data.records.length==0){
        noMore=true 
      }
      that.setData({noMore: noMore})
    })
  },

  //搜索
  onSearch: function(){
    let that = this
    let req = {productType: 2, kw: this.data.kw, currCity: this.data.currCity}
    utils.request(
      app.globalData.apiBaseUrl+'/photo/service/search',
      req
    ).then(res=>{
      let noResult = false
      if(res.data.records.length==0){
        noResult = true
      }
      wx.stopPullDownRefresh()
        that.setData({
          productList: res.data,
          noSearch: noResult
        })
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

  toAddPage: function(e){
    wx.navigateTo({
      url: '/pages/product/publish?stype=1',
    })
  },

  onClearSearch: function(e){
    this.setData({kw: ''})
  },

  onHide(){
    this.setData({kw: ''})
  },

  changeCity: function(e){
    wx.navigateTo({
      url: '/pages/product/city?servicetype=2',
    })
  },

  // 获取当前定位
  getCurrentPositioning: function() {
    const that = this
    wx.getLocation({
      type: 'wgs84',
      altitude: true,
      isHighAccuracy: true,
      highAccuracyExpireTime: 2000,
      success: function(res) {
        wx.hideLoading()
        console.log(res)
        that.setData({
          latitude: res.latitude,
          longitude: res.longitude,
          'photoService.posLat': res.latitude,
          'photoService.posLng': res.longitude
        })
        // 构建请求地址
        // 逆解析接口 /ws/geocoder/v1
        var qqMapApi = 'http://apis.map.qq.com/ws/geocoder/v1/' + "?location=" + that.data.latitude + ',' +
          that.data.longitude + "&key=" + 'NXDBZ-PWQRD-3RK4F-HHB34-C2DPH-KBFBM' + "&get_poi=1";
        that.sendRequest(qqMapApi);
      },
      fail: function(res) {
        console.log(res)
        wx.showToast({
          title: '获取位置信息失败',
          icon: 'none'
        })
      }
    })
  },

  sendRequest:function(qqMapApi) {
    const that = this
    wx.request({
      url: qqMapApi,
      header: {
        'Content-Type': 'application/json'
      },
      data: {},
      method:'GET',
      success: (res) => {
        console.log('定位成功：',res)
        if (res.statusCode == 200 && res.data.status == 0) {
          // 从返回值中提取需要的业务地理信息数据 国家、省、市、县区、街道
          // that.setData({ 'address.province': res.data.resul
          let cityStr = res.data.result.address_component.city
          // that.setData({ 'photoService.posLng': res.data.result.location.lat });
          // that.setData({ 'photoService.posLat': res.data.result.location.lng });
          that.setData({
            currCity: cityStr
          })
          app.globalData.currCity=cityStr
          wx.setStorageSync('CURR_CITY', cityStr)
        }
      }
    })
  },

  //筛选类型
  bindCategoryFilter: function(e){
    this.setData({productList: null})
    let category = e.currentTarget.dataset.tag
    console.log(category)
    this.setData({tag: category})
    this.loadProductList()
  },

})