import { Component ,OnInit } from '@angular/core';
import { NavController } from 'ionic-angular';
import { HomeComponent } from '../home/home.component';
import { LoginService } from '../../services/login.service';
import { GooglePlus } from '@ionic-native/google-plus';
import { Facebook, FacebookLoginResponse } from '@ionic-native/facebook';
import { FormGroup,FormControl,Validators } from '@angular/forms';

@Component({
  selector: 'page-login',
  templateUrl: 'login.html'
})

export class LoginPage implements OnInit{

  public email;
  public password;
  public loginform : FormGroup;

  constructor(public navCtrl: NavController,
              public _authServ: LoginService,
              public googlePlus : GooglePlus,
              public fb : Facebook
             ) {}

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
    
    this._authServ.verifyUser(this.loginform.value)
      .subscribe(
        (res) => {
          this.Loginresponse = res;
         // alert(loginform.emailId);
          this.verifySuccessfully(res);
          this.NavLogin();
      },
      (err) => {
       alert(this.loginform.value.emailId);
       alert(this.loginform.value.password);
        console.log(err);
        alert(err);
      })
  }

  public verifySuccessfully(res) {
    localStorage.setItem("access_token", res.token);
  }

  public googleResponse;
  public googleLogin(){
    this.googlePlus.login({
      'scopes': 'profile email https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile', 
      'webClientId': '802921815833-vi6nrrotqau2c7c436j55c04r520lr8r.apps.googleusercontent.com',
      'offline': true,
    })
      .then((res) => {this.googleResponse = JSON.stringify(res); alert(this.googleResponse); alert(res.serverAuthCode); this.gauthcallBack(res.serverAuthCode); this.navCtrl.push(HomeComponent)})
      .catch(err => {console.log(err),alert(err)})
  }

  public fbresponse : any;
  public facebookLogin(){
    this.fb.login(['email'])
      .then((res: FacebookLoginResponse) => { this.fbresponse = JSON.stringify(res), this.postacesstoken() , this.navCtrl.push(HomeComponent)})
      .catch(e => alert(e));
  }


  public postacesstoken(){
  //   alert(this.fbresponse.authResponse);
     alert(this.googleResponse.access_token);
  }

  public gauthcallBack(serverauthcode){
    this._authServ.gAuthCallback(serverauthcode)
      .subscribe((res) => {alert("success"); alert(JSON.stringify(res))}, 
                 (err) => {alert(serverauthcode); alert(err)})      
  }

  ngOnInit(){
      this.loginform = new FormGroup({
        emailId : new FormControl(""),
        password : new FormControl("")
    })
  }

}