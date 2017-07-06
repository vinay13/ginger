import { Component } from '@angular/core';
import { NavController, NavParams, ToastController } from 'ionic-angular';
import {LoginService} from '../../../services/login.service';
import {HomeComponent} from '../../home/home.component';
import {AboutPage} from '../../about/about.ts';

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
                public _loginserv : LoginService,
                public toastCtrl : ToastController){
      
      this.selectedIdiom = this.navparams.get('idiom');
    }

    emailverifyToast(){
         let toast = this.toastCtrl.create({
            message : 'Activation link is sent to your email',
            duration : 5000
        });
        toast.present();
    }

    public UserSignup(){
          this.signupF = {
               "emailId" : this.emailId,
               "partnerId" : "Ginger",
               "password" : this.password
           }
        console.log(this.signupF);
        this._loginserv.SignupUser(this.signupF)
            .subscribe( (res) => { console.log('signup',res); this.emailverifyToast();  this.storeToken(res) ; this.navHome();},
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