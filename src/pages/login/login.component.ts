import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { HomeComponent } from '../home/home.component';
import { LoginService } from '../../services/login.service';
import { GooglePlus } from '@ionic-native/google-plus';
import { Facebook, FacebookLoginResponse } from '@ionic-native/facebook';

@Component({
  selector: 'page-login',
  templateUrl: 'login.html'
})

export class LoginPage {

  public email;
  public password;

  constructor(public navCtrl: NavController,
              public _authServ: LoginService,
              public googlePlus : GooglePlus,
              public fb : Facebook
             ) {
    
  }

  icon :string = "md-eye";
  type : string = "password";

  EyeOffClicked(param){
    console.log('clickkk eyeoff');
      if(param === "md-eye"){
       
       this.type = "text";   
       this.icon = "md-eye-off";
    }
    else{
      this.type = "password";
      this.icon = "md-eye";
    }
  }

  NavLogin(){  
    this.navCtrl.push(HomeComponent);
  }

  Loginresponse : any;
  UserLogin(){
    let datalogin = {"emailId": this.email,"password": this.password }

    console.log('login',datalogin);
    this._authServ.verifyUser(datalogin)
      .subscribe(
        (res) => {
          this.Loginresponse = res;
          console.log('res',res.token);
          this.verifySuccessfully(res);
          this.NavLogin();
      },
      (err) => {
        console.log(err);
        alert(err);
      })
  }

  public verifySuccessfully(res) {
    localStorage.setItem("access_token", res.token);
  }

  public googleLogin(){
    this.googlePlus.login({})
      .then((res) => {alert(JSON.stringify(res)); this.navCtrl.push(HomeComponent)})
      .catch(err => {console.log(err),alert(err)})
  }

  public fbresponse : any;
  public facebookLogin(){
    this.fb.login(['email'])
      .then((res: FacebookLoginResponse) => { this.fbresponse = res, alert(JSON.stringify(res)), this.navCtrl.push(HomeComponent)})
      .catch(e => alert(e));
  }

  public postacesstoken(){
     
  }

}