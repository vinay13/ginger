import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { HomeComponent } from '../home/home.component';
import { LoginService } from '../../services/login.service';

@Component({
  selector: 'page-login',
  templateUrl: 'login.html'
  
})

export class LoginPage {

  constructor(public navCtrl: NavController) {
    
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

  UserLogin(){  
    this.navCtrl.push(HomeComponent);
  }




}