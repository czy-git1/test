// app.js
App({
  onLaunch() {
    //全局定时器，用于轮询新消息
    // let timer = setInterval(() => {
    //   let timestamp = Date.parse(new Date());
    //   //TODO   
    // }, 3000)  
    // wx.setTabBarBadge({index: 1, text: '3'});
  },
  
  globalData: {
    apiBaseUrl: 'http://localhost:8030/mall-server/api',
    // apiBaseUrl: 'http://192.168.1.17:8030/mall-server/api',
    // apiBaseUrl: 'https://a1b2c3d4.ngrok-free.app/mall-server/api',
    userInfo: null,
    imgSrc: '',
    timerLock:false,//是否关闭合成定时器
    uid: 'f775b389-2d99-f280-cff6-032ab33991ef'
  },
})
