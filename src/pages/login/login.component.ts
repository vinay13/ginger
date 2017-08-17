import { Component ,OnInit } from '@angular/core';
import {  IonicPage, NavController,NavParams,ToastController } from 'ionic-angular';
import { HomeComponent } from '../home/home.component';
import { LoginService } from '../../services/login.service';
import { GooglePlus } from '@ionic-native/google-plus';
import { Facebook, FacebookLoginResponse } from '@ionic-native/facebook';
import { FormGroup,FormControl,Validators } from '@angular/forms';
import { SignupComponent } from './signup/signup.component';
import { AboutPage } from '../about/about.ts';
import { ProfileService } from '../../services/profile.service';

@Component({
  selector : 'page-login',
  templateUrl: 'login.html'
})

export class LoginPage implements OnInit{

  public email;
  public password;
  public loginform : FormGroup;
  public selectedIdiom; 
  constructor(public navCtrl: NavController,
              public navparams : NavParams,
              public _authServ: LoginService,
              public googlePlus : GooglePlus,
              public toastCtrl : ToastController,
              public fb : Facebook,
              public _proserv : ProfileService
             ) {
                this.selectedIdiom = this.navparams.get('idiom');
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
    this.navCtrl.push(AboutPage,{
        'idiom': this.selectedIdiom
    });
  }

  toastLogin(){
        let toast = this.toastCtrl.create({
            message : 'username/password incorrect',
            duration : 4000
        });
        toast.present();
  }

  Loginresponse : any;
  UserLogin(){

    this._authServ.verifyUser(this.loginform.value)
      .subscribe(
        (res) => {
          this.Loginresponse = res;
         // alert(loginform.emailId);
          this.verifySuccessfully(res);
          this.getprofile();
          this.NavLogin();
      },
      (err) => {
        console.log(err);
        this.toastLogin();
      })
  }

  public verifySuccessfully(res) {
    localStorage.setItem("access_token", res.token);
  }

  getprofile(){
    this._proserv.GetUserProfile()
        .subscribe( (data) => { localStorage.setItem('username',data.userName); },
                     (err) => {console.log(err);})
  }
// clientId = "802025194036-nk9ebs1d9sc4em80ore73oavctb75esk.apps.googleusercontent.com"
// clientSecret = "Ia_HMIjBvZGbrbQpxNzcmmXp"
  public googleResponse;
  public googleLogin(){
      this.googlePlus.login({
         'scopes': 'profile email https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile', 
         'webClientId': '802921815833-vi6nrrotqau2c7c436j55c04r520lr8r.apps.googleusercontent.com',
         'offline': true,
      })
         .then((res) => { this.googleResponse = JSON.stringify(res); this.gauthcallBack(res.serverAuthCode);  })
         .catch(err => { console.log(err),alert(err)})
  }

  public fbresponse : any;
  public facebookLogin(){
    this.fb.login(['email'])
      .then((res: FacebookLoginResponse) => { this.fbresponse = res; this.fbauthcallBack(this.fbresponse.authResponse.accessToken);})
      .catch(e => alert(e));
  }

  public postacesstoken(){
     //alert(this.fbresponse.authResponse);
  //   alert(this.fbresponse.authResponse['accessToken']);
    // alert(this.googleResponse.access_token);
  }

  public gauthcallBack(serverauthcode){
    this._authServ.gAuthCallback(serverauthcode)
      .subscribe((res) => {this.verifySuccessfully(res);this.NavLogin(); this.getprofile(); }, 
                 (err) => {alert(err)})      
  }

  public fbauthcallBack(serverauthcode){
   
    this._authServ.fbAuthCallback(serverauthcode)
      .subscribe((res) => {this.verifySuccessfully(res);this.NavLogin(); this.getprofile(); }, 
                 (err) => {alert(err)})      
  }

  public navSignup(){
    this.navCtrl.push(SignupComponent,{
      'idiom': this.selectedIdiom
    });
  }

  ngOnInit(){
      this.loginform = new FormGroup({
        emailId : new FormControl(""),
        password : new FormControl("")
    })
  }

}