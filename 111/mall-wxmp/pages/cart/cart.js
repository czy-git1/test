const app = getApp()
import utils from '../../utils/util.js'
import Notify from '@vant/weapp/notify/notify'
import Dialog from '@vant/weapp/dialog/dialog';
Page({

  /**
   * 页面的初始数据
   */
  data: {
    selectList: [],
    cartList: [],
    isAllSelected: false,
    totalAmount: 0
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
    this.loadCartList()
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {
    this.loadCartList()
  },

  loadCartList: function(){
    let that = this
    utils.request(
      app.globalData.apiBaseUrl+'/cart/list',
      {}
    ).then(res=>{
      wx.stopPullDownRefresh()
      let length = res.data.length
      let nselectList = []
      let selectAllOrNot = true
      for(let idx=0; idx<length; idx++){
        if(res.data.isSelect==0){
          nselectList.push(false)
          selectAllOrNot=false
        }else{
          nselectList.push(true)
        }
      }
      that.setData({
        cartList: res.data,
        selectList: nselectList,
        isAllSelected: selectAllOrNot
      })
      that.calcTotalAmount()
    })
  },

  //刷新购物车
  refreshCart: function(){
    let that = this
    utils.request(
      app.globalData.apiBaseUrl+'/cart/list',
      {}
    ).then(res=>{
      that.setData({
        cartList: res.data,
      })
      that.calcTotalAmount()
    })
  },

  //计算已勾选的总价
  calcTotalAmount: function(){
    let newCartList = this.data.cartList
    let total = 0
    for(let idx=0; idx<newCartList.length; idx++){
      if(newCartList[idx].isSelect){
        let amount = newCartList[idx].product.price
        total += amount*newCartList[idx].quantity
      }
    }
    total = total * 100 //单位为分，自动格式化
    this.setData({totalAmount: total})
  },

  //增加数量
  addQuantity: function(e){
    let cid = e.currentTarget.dataset.cid
    let that = this
    utils.request(
      app.globalData.apiBaseUrl+'/cart/add/quantity',
      {cartId: cid}
    ).then(res=>{
      that.refreshCart()
    })
  },

  //减少数量
  minusQuantity: function(e){
    let cid = e.currentTarget.dataset.cid
    let that = this
    utils.request(
      app.globalData.apiBaseUrl+'/cart/minus/quantity',
      {cartId: cid}
    ).then(res=>{
      that.refreshCart()
    })
  },

  minusQuantity2: function(e){
    this.removeCart(e)
  },

  //收藏商品
  onFav: function(e){
    let cid = e.currentTarget.dataset.cid
    let that = this
    utils.request(
      app.globalData.apiBaseUrl+'/cart/fav',
      {productId: cid}
    ).then(res=>{
      wx.showToast({
        title: '已收藏',
        icon: 'success'
      })
    })
  },

  //移出购物车
  removeCart: function(e){
    let that = this
    let cid = e.currentTarget.dataset.cid
    Dialog.confirm({
      title: '提示',
      message: '确认将该商品从购物车移除吗？',
    }).then(() => {
      utils.request(
        app.globalData.apiBaseUrl+'/cart/remove',
        {cartId: cid}
      ).then(res=>{
        wx.showToast({
          title: '已移除',
          icon: 'success'
        })
        that.refreshCart()
      })
    }).catch(() => {
      //取消
    });
  },

  //勾选商品响应事件
  onChangeSelect(event) {
    console.log(event.currentTarget.dataset)
    let idx = event.currentTarget.dataset.idx
    let cartid = event.currentTarget.dataset.cartid
    let newSelectList = this.data.selectList
    let newCartList = this.data.cartList
    let that = this
    if(this.data.selectList[idx]){
      //取消勾选
      newSelectList[idx] = false
      newCartList[idx].isSelect = false
      utils.request(
        app.globalData.apiBaseUrl+'/cart/select/dis',
        {cartId: cartid, isSelect: 0}
      ).then(res=>{
        that.setData({isAllSelected: false})
      })
    }else{
      //勾选
      newSelectList[idx] = true
      newCartList[idx].isSelect = true
      utils.request(
        app.globalData.apiBaseUrl+'/cart/select',
        {cartId: cartid, isSelect: 1}
      ).then(res=>{})
      console.log('勾选',newSelectList)
    }
    let selectAllOrNot = true
    console.log(newSelectList)
    for(let idx2=0; idx2<newSelectList.length; idx2++){
      if(!newSelectList[idx2]){
        selectAllOrNot=false
      }
    }
    this.setData({
      selectList: newSelectList,
      cartList: newCartList,
      isAllSelected: selectAllOrNot
    });
    this.calcTotalAmount()
  },

  //全选与取消全选
  onChangeAllSelect(e){
    let length = this.data.selectList.length
    console.log()
    let nselectList = []
    let nAllSelected = false
    let newCartList = this.data.cartList
    if(this.data.isAllSelected){
      //当前是全选状态，要变为全不选
      for(let idx=0; idx<length; idx++){
        nselectList.push(false)
      }
      utils.request(
        app.globalData.apiBaseUrl+'/cart/select/dis/all',
        {}
      ).then(res=>{})
      for(let idx=0; idx<newCartList.length; idx++){
        newCartList[idx].isSelect=false
      }
    }else{
      //全选
      for(let idx=0; idx<length; idx++){
        nselectList.push(true)
      }
      nAllSelected=true
      utils.request(
        app.globalData.apiBaseUrl+'/cart/select/all',
        {}
      ).then(res=>{})
      for(let idx=0; idx<newCartList.length; idx++){
        newCartList[idx].isSelect=true
      }
    }
    this.setData({
      selectList: nselectList,
      cartList: newCartList,
      isAllSelected: nAllSelected
    })
    this.calcTotalAmount()
  },

  toDetailPage: function(e){
    let bid = e.currentTarget.dataset.pid
    wx.navigateTo({
      url: '/pages/product/detail?pid='+bid,
    })
  },

  //进入预览订单页面
  toOrderConfirm: function(e){
    wx.navigateTo({
      url: '/pages/order/comfirm?pid=-1&otype=1'
    })
  },
})