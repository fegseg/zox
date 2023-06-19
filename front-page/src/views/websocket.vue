<template>
  <div class="login-page">
    <div class="login">
      <div class="front-login">
        <h2 class="title">
          <p  style="color: #000000a6">聊天框</p>
        </h2>
        
        <div class="chat-content">
          <div class="word" >
               <img src="../assets/img/2.jpg" width="30px" height="30px" >
               <div class="info">
                    <p class="time">dzc  2023-06-01 14:12:11</p>
                    <div class="info-content">你好，请问需要什么帮助</div>
               </div>
          </div>
          <div class="word-my">
               <div class="info">
                    <p class="time">111  2023-06-01 14:13:01</p>
                    <div class="info-content">你好</div>
               </div>
               <img src="../assets/img/3.jpg" width="30px" height="30px">
           </div>
          </div>
        <a-form
          id="components-form-demo-normal-login"
          class="login-form"
        >
        
          <a-form-item style="margin-top: 150px;">
            <a-input>
              <a-icon
                slot="prefix"
                type="user"
                style="color: rgba(0, 0, 0, 0.25)"
              />
            </a-input>
            <div align="right">
              <a-button type="primary" style="margin-bottom: 5px" >发送</a-button>
            </div>
          </a-form-item>

         
          
          <a-form-item>
            
            
            
          </a-form-item>
        </a-form>
      </div>
    </div>
  </div>
</template>


<style>
#components-form-demo-normal-login .login-form {
  max-width: 300px;
}
#components-form-demo-normal-login .login-form-forgot {
  float: right;
}
#components-form-demo-normal-login .login-form-button {
  width: 100%;
}
.login-page {
  background-image: url(../assets/img/bg_register.png);
  background-repeat: no-repeat;
  height: 100%;
  padding: 58px 0;
  margin-top: 2px;
}

.login-page .front-login {
  background-color: #ffffff;
  width: 700px;
  height: 523px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  margin: auto;
  position: relative;
  padding: 28px 56px 0;
  text-align: left;
}

.login-page .front-login .title {
  text-align: center;
  margin-bottom: 18px;
  height: 38px;
  line-height: 38px;
}
.login-page .front-login .ant-input {
  height: 38px;
}
.login-page .front-login .login-form-button {
  height: 35px;
}

.chat-content{
  width: 600px;
  padding: 20px;
  .word{
     display: flex;
     margin-bottom: 20px;
     img{
       width: 40px;
       height: 40px;
       border-radius: 50%;
     }
     .info{
       margin-left: 10px;
       .time{
         font-size: 12px;
         color: rgba(51,51,51,0.8);
         margin: 0;
         height: 20px;
         line-height: 20px;
         margin-top: -5px;
       }
       .info-content{
          padding: 10px;
          font-size: 14px;
          background: #e5dede;
          position: relative;
          margin-top: 8px;
          border: solid;
        }
        .info-content::before{
            position: absolute;
            left: -8px;
            top: 8px;
            content: '';
            border-right: 10px solid #dfd6d6;
            border-top: 8px solid transparent;
            border-bottom: 8px solid transparent;
        }
     }
  }
 
.word-my{
    display: flex;
    justify-content:flex-end;
    margin-bottom: 20px;
    img{
       width: 40px;
       height: 40px;
       border-radius: 50%;
     }
     .info{
       width: 90%;
       margin-left: 10px;
       text-align: right;
       .time{
         font-size: 12px;
         color: rgba(51,51,51,0.8);
         margin: 0;
         height: 20px;
         line-height: 20px;
         margin-top: -5px;
         margin-right: 10px;
       }
       .info-content{
          max-width: 70%;
          padding: 10px;
          font-size: 14px;
          float: right;
          margin-right: 10px;
          position: relative;
          margin-top: 8px;
          background: #A3C3F6;
          text-align: left;
        }
        .info-content::after{
            position: absolute;
            right: -8px;
            top: 8px;
            content: '';
            border-left: 10px solid #A3C3F6;
            border-top: 8px solid transparent;
            border-bottom: 8px solid transparent;
        }
     }
  }
}
</style>