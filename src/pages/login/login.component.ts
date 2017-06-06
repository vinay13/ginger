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
          console.log('res',res.token);
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

  public googleLogin(){
    this.googlePlus.login({})
      .then((res) => {alert(JSON.stringify(res)); this.navCtrl.push(HomeComponent)})
      .catch(err => {console.log(err),alert(err)})
  }

  public fbresponse : any;
  public facebookLogin(){
    this.fb.login(['email'])
      .then((res: FacebookLoginResponse) => { this.fbresponse = JSON.stringify(res), this.postacesstoken(), alert(JSON.stringify(res)), this.navCtrl.push(HomeComponent)})
      .catch(e => alert(e));
  }

  public postacesstoken(){
     alert(this.fbresponse.authResponse);
  }

  ngOnInit(){
      this.loginform = new FormGroup({
        emailId : new FormControl(""),
        password : new FormControl("")
    })
  }

}