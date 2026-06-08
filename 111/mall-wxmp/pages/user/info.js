// pages/user/info.js
var app = getApp()
import utils from '../../utils/util.js'
import Dialog from '@vant/weapp/dialog/dialog';
Page({

  /**
   * 页面的初始数据
   */
  data: {
    profileImg: null,
    userInfo: {},
    user: {
      "mobile": "",
      "pwd": "",
      "userId": "",
      "pic": "",
      "city": "",
    },
    genderTypes: ['男', '女', '保密'],
    gindex: 0,
    region: [],
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.initUserInfo()
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
    if(app.globalData.imgSrc!=null&&app.globalData.imgSrc!=''){
      //显示已裁剪的头像
      this.setData({profileImg: app.globalData.imgSrc, 'user.pic':app.globalData.imgSrc})
    }
  },

  //选择图片
  chooseImg: function(){
    let _this = this
    wx.chooseMedia({
      count: 1,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      mediaType: ['image'],
      success (res) {
        // tempFilePath可以作为img标签的src属性显示图片
        const picUrl = res.tempFiles[0].tempFilePath
        wx.navigateTo({
          url: '/pages/copper/cropper?picurl='+picUrl,
        })
      }
    })
  },

  initUserInfo(){
    if(app.globalData.userInfo == null){
      wx.showModal({
        title: '提示',
        content: '请先登录',
        success (res) {
          if (res.confirm) {
            wx.navigateTo({
              url: '/pages/user/login/login',
            })
          }
        }
      })
    }else{
      let appInfo = app.globalData.userInfo
      let that = this
      console.log(appInfo)
      let gindex = 0
      if(appInfo.gender=='男'){
        gindex = 0
      }else if(appInfo.gender=='女'){
        gindex = 1
      }else{
        gindex = 2
      }
      if(appInfo.city!=null&&appInfo.city!=''){
        let ary = appInfo.city.split("-")
        this.setData({region: ary})
      }
      this.setData({
        userInfo : appInfo,
        profileImg: appInfo.pic,
        gindex: gindex,
        user: appInfo
      })
    }
  },

  bindFarmNameChange: function(e){
    this.setData({ 'user.nickName': e.detail.value})
  },

  bindMobileChange: function (e) {
    this.setData({ 'user.mobile': e.detail.value })
  },

  bindShortDescChange: function (e) {
    this.setData({ 'user.shortDesc': e.detail.value })
  },

  bindGenderChange: function(e) {
    let gindex = e.detail.value
    let gtype = this.data.genderTypes[gindex]
    this.setData({'user.gender': gtype, gindex: gindex})
  },

  //提交表单
  publishUpdate: function(e){
    wx.showLoading({
      title: '正在更新',
    })
    let that = this
    let req = this.data.user
    utils.request(
      app.globalData.apiBaseUrl+'/user/updateUser',
      req
    ).then(res=>{
      wx.hideLoading()
      Dialog.alert({
        title: '提示',
        message: '个人信息已更新',
      }).then(() => {
        console.log(res)
        console.log('更新用户成功：设置全局变量', app.globalData.userInfo)
        app.globalData.userInfo = res.data
        that.setData({
          userInfo: res.data
        })
        wx.setStorageSync('LOGIN_USER', res.data)
        console.log('更新用户成功：获取全局变量', app.globalData.userInfo)
        wx.switchTab({
          url: '/pages/user/profile',
        })
      });
    })
    app.globalData.imgSrc=''
  },

  //刷新用户信息
  reloadUser: function(){
    var that = this
    wx.request({
      url: app.globalData.apiBaseUrl+'/user/updateUser',
      header: {
        'content-type': 'application/json' ,
        'Cookie': wx.getStorageSync('cookieKey')
      },
      method: 'POST',
      success(res){
        console.log(res)
        wx.hideLoading()
        if(res.data.code == "0"){
        }
      }
    })
  },
          
  bindRegionChange: function (e) {
    let add = e.detail.value.join("-")
    this.setData({
      region: e.detail.value,
      'user.city': add
    })
  }
})

// // pages/user/info.js
// var app = getApp()
// import utils from '../../utils/util.js'
// import Dialog from '@vant/weapp/dialog/dialog';

// Page({

//   /**
//    * 页面的初始数据
//    */
//   data: {
//     profileImg: null,        // 当前显示的头像（可能是临时裁剪的）
//     userInfo: {},            // 完整的用户信息（用于显示）
//     user: {                  // 用于提交的表单数据
//       "mobile": "",
//       "pwd": "",
//       "userId": "",
//       "pic": "",
//       "city": "",
//       "nickName": "",
//       "gender": "",
//       "shortDesc": ""
//     },
//     genderTypes: ['男', '女', '保密'],
//     gindex: 0,
//     region: [],
//   },

//   /**
//    * 生命周期函数--监听页面加载
//    */
//   onLoad: function (options) {
//     this.initUserInfo()
//   },

//   /**
//    * 生命周期函数--监听页面初次渲染完成
//    */
//   onReady: function () {

//   },

//   /**
//    * 生命周期函数--监听页面显示
//    */
//   onShow: function () {
//     // 从全局变量获取裁剪后的头像（兼容原有的方式）
//     if(app.globalData.imgSrc != null && app.globalData.imgSrc != ''){
//       console.log('从全局变量获取裁剪头像:', app.globalData.imgSrc);
//       this.setData({
//         profileImg: app.globalData.imgSrc, 
//         'user.pic': app.globalData.imgSrc
//       });
//       // 清空全局变量，避免重复使用
//       app.globalData.imgSrc = '';
//     }
//   },

//   /**
//    * 初始化用户信息
//    */
//   initUserInfo(){
//     if(app.globalData.userInfo == null){
//       wx.showModal({
//         title: '提示',
//         content: '请先登录',
//         success (res) {
//           if (res.confirm) {
//             wx.navigateTo({
//               url: '/pages/user/login/login',
//             })
//           }
//         }
//       })
//     }else{
//       let appInfo = app.globalData.userInfo
//       console.log('用户信息:', appInfo)
      
//       // 设置性别索引
//       let gindex = 0
//       if(appInfo.gender == '男'){
//         gindex = 0
//       }else if(appInfo.gender == '女'){
//         gindex = 1
//       }else{
//         gindex = 2
//       }
      
//       // 设置地区
//       if(appInfo.city != null && appInfo.city != ''){
//         let ary = appInfo.city.split("-")
//         this.setData({region: ary})
//       }
      
//       // 初始化数据
//       this.setData({
//         userInfo: appInfo,
//         profileImg: appInfo.pic,
//         gindex: gindex,
//         user: { ...appInfo }  // 深拷贝，避免修改原对象
//       })
//     }
//   },

//   /**
//    * 选择图片 - 优化版本，使用事件通道
//    */
//   // chooseImg: function(){
//   //   let _this = this
    
//   //   wx.chooseMedia({
//   //     count: 1,
//   //     sizeType: ['compressed'],
//   //     sourceType: ['album', 'camera'],
//   //     mediaType: ['image'],
//   //     success (res) {
//   //       const picUrl = res.tempFiles[0].tempFilePath
//   //       console.log('选择的图片路径:', picUrl)
        
//   //       // 使用事件通道方式跳转到裁剪页面
//   //       wx.navigateTo({
//   //         url: '/pages/copper/cropper?picurl=' + encodeURIComponent(picUrl),
//   //         events: {
//   //           // 接收裁剪页面返回的数据
//   //           acceptCroppedImage: function(data) {
//   //             console.log('收到裁剪图片:', data.imgPath);
              
//   //             // 更新页面显示
//   //             _this.setData({
//   //               profileImg: data.imgPath,
//   //               'user.pic': data.imgPath
//   //             });
              
//   //             // 也可以保存到全局变量，保持一致性
//   //             getApp().globalData.imgSrc = data.imgPath;
              
//   //             wx.showToast({
//   //               title: '头像已更新',
//   //               icon: 'success',
//   //               duration: 1500
//   //             });
//   //           }
//   //         },
//   //         fail: function(err) {
//   //           console.error('跳转到裁剪页面失败:', err);
//   //           wx.showToast({
//   //             title: '跳转失败',
//   //             icon: 'none'
//   //           });
//   //         }
//   //       });
//   //     },
//   //     fail: function(err) {
//   //       console.error('选择图片失败:', err);
//   //       wx.showToast({
//   //         title: '选择图片失败',
//   //         icon: 'none'
//   //       });
//   //     }
//   //   });
//   // },

//   // pages/user/info.js - 最简单的选择图片方法
// chooseImg: function(){
//   let _this = this
  
//   wx.chooseImage({
//     count: 1,
//     sizeType: ['compressed'],
//     sourceType: ['album', 'camera'],
//     success: (res) => {
//       const tempFilePath = res.tempFilePaths[0];
//       console.log('选择的图片:', tempFilePath);
      
//       // 直接询问是否使用
//       wx.showModal({
//         title: '提示',
//         content: '使用此图片作为头像？',
//         success: (modalRes) => {
//           if (modalRes.confirm) {
//             // 直接设置图片，不经过裁剪
//             _this.setData({
//               profileImg: tempFilePath,
//               'user.pic': tempFilePath
//             });
            
//             wx.showToast({
//               title: '头像已更新',
//               icon: 'success'
//             });
            
//             // 保存到全局（可选）
//             app.globalData.imgSrc = tempFilePath;
//           }
//         }
//       });
//     },
//     fail: (err) => {
//       console.error('选择图片失败:', err);
//       wx.showToast({
//         title: '选择失败',
//         icon: 'none'
//       });
//     }
//   });
// },

//   /**
//    * 备用选择图片方法 - 如果不使用裁剪组件，可以直接用这个
//    */
//   chooseImgSimple: function(){
//     wx.chooseImage({
//       count: 1,
//       sizeType: ['compressed'],
//       sourceType: ['album', 'camera'],
//       success: (res) => {
//         const tempFilePath = res.tempFilePaths[0];
        
//         // 询问是否使用此图片
//         wx.showModal({
//           title: '提示',
//           content: '是否使用此图片作为头像？',
//           success: (modalRes) => {
//             if (modalRes.confirm) {
//               this.setData({
//                 profileImg: tempFilePath,
//                 'user.pic': tempFilePath
//               });
              
//               wx.showToast({
//                 title: '头像已更新',
//                 icon: 'success'
//               });
//             }
//           }
//         });
//       }
//     });
//   },

//   /**
//    * 昵称修改
//    */
//   bindFarmNameChange: function(e){
//     this.setData({ 
//       'user.nickName': e.detail.value,
//       'userInfo.nickName': e.detail.value  // 同步更新显示
//     });
//   },

//   /**
//    * 手机号修改
//    */
//   bindMobileChange: function (e) {
//     this.setData({ 
//       'user.mobile': e.detail.value,
//       'userInfo.mobile': e.detail.value
//     });
//   },

//   /**
//    * 简介修改
//    */
//   bindShortDescChange: function (e) {
//     this.setData({ 
//       'user.shortDesc': e.detail.value,
//       'userInfo.shortDesc': e.detail.value
//     });
//   },

//   /**
//    * 性别修改
//    */
//   bindGenderChange: function(e) {
//     let gindex = parseInt(e.detail.value)
//     let gtype = this.data.genderTypes[gindex]
//     this.setData({
//       'user.gender': gtype, 
//       'userInfo.gender': gtype,
//       gindex: gindex
//     });
//   },

//   /**
//    * 地区修改
//    */
//   bindRegionChange: function (e) {
//     let add = e.detail.value.join("-")
//     this.setData({
//       region: e.detail.value,
//       'user.city': add,
//       'userInfo.city': add
//     });
//   },

//   /**
//    * 提交表单
//    */
//   publishUpdate: function(e){
//     // 表单验证
//     if(!this.data.user.nickName){
//       wx.showToast({
//         title: '请输入昵称',
//         icon: 'none'
//       });
//       return;
//     }
    
//     if(!this.data.user.mobile){
//       wx.showToast({
//         title: '请输入手机号',
//         icon: 'none'
//       });
//       return;
//     }
    
//     // 验证手机号格式（简单验证）
//     if(!/^1[3-9]\d{9}$/.test(this.data.user.mobile)){
//       wx.showToast({
//         title: '手机号格式不正确',
//         icon: 'none'
//       });
//       return;
//     }
    
//     wx.showLoading({
//       title: '正在更新...',
//       mask: true
//     });
    
//     let that = this
//     let req = { ...this.data.user }  // 深拷贝提交数据
    
//     // 确保userId存在
//     req.userId = app.globalData.userInfo ? app.globalData.userInfo.userId : '';
    
//     console.log('提交数据:', req);
    
//     utils.request(
//       app.globalData.apiBaseUrl + '/user/updateUser',
//       req,
//       'POST'
//     ).then(res => {
//       wx.hideLoading();
      
//       if(res.data && res.data.code == '0'){
//         Dialog.alert({
//           title: '提示',
//           message: '个人信息更新成功',
//           confirmButtonColor: '#2e75b6'
//         }).then(() => {
//           console.log('更新用户成功，返回数据:', res.data);
          
//           // 更新全局变量
//           app.globalData.userInfo = res.data.data || req;
          
//           // 更新本地存储
//           wx.setStorageSync('LOGIN_USER', app.globalData.userInfo);
          
//           // 清空临时头像标记
//           app.globalData.imgSrc = '';
          
//           // 跳转到个人主页
//           wx.switchTab({
//             url: '/pages/user/profile',
//           });
//         });
//       } else {
//         wx.showToast({
//           title: res.data.msg || '更新失败',
//           icon: 'none'
//         });
//       }
//     }).catch(err => {
//       wx.hideLoading();
//       console.error('更新失败:', err);
//       wx.showToast({
//         title: '网络错误，请重试',
//         icon: 'none'
//       });
//     });
//   },

//   /**
//    * 刷新用户信息（备用）
//    */
//   reloadUser: function(){
//     var that = this
//     wx.request({
//       url: app.globalData.apiBaseUrl + '/user/getUserInfo',
//       header: {
//         'content-type': 'application/json',
//         'Cookie': wx.getStorageSync('cookieKey')
//       },
//       method: 'GET',
//       success(res){
//         console.log('刷新用户信息:', res);
//         if(res.data && res.data.code == "0"){
//           let userData = res.data.data;
//           app.globalData.userInfo = userData;
//           that.initUserInfo();
//         }
//       },
//       fail(err) {
//         console.error('刷新失败:', err);
//       }
//     });
//   }
// })