import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import {LoginService} from '../../../services/login.service';
import {HomeComponent} from '../../home/home.component';

@Component({
    selector: "page-signup",
    templateUrl : 'signup.html'
})

export class SignupComponent{

    
    signupF;
    emailId;
    password;
    selectedIdiom;
    constructor(public navCtrl : NavController,
                public navparams : NavParams,
                public _loginserv : LoginService){
      
      this.selectedIdiom = this.navparams.get('idiom');
    }

    public UserSignup(){
          this.signupF = {
               "emailId" : this.emailId,
               "partnerId" : "Ginger",
               "password" : this.password
           }
        console.log(this.signupF);
        this._loginserv.SignupUser(this.signupF)
            .subscribe( (res) => { console.log('signup',res); this.storeToken(res) ; this.navHome();},
                        (err) => { console.log('err',err)})
    }

    public navHome(){
         this.navCtrl.push(HomeComponent,{
             'idiom': this.selectedIdiom
         }
         );   
    }

    public storeToken(res){
        localStorage.setItem("access_token", res.token);
    }
}