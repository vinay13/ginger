import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { HomeComponent } from '../home/home.component';

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
      if(param === "md-eye"){
       
       this.type = "password";   
       this.icon = "md-eye-off";
    }
    else{
     
      this.type = "text";
      this.icon = "md-eye";
    }
  }

  UserLogin(){  
    this.navCtrl.push(HomeComponent);
  }




}