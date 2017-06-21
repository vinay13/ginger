import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import {FormGroup,FormControl,Validators} from '@angular/forms';
import {LoginService} from '../../../services/login.service';
import {HomeComponent} from '../../home/home.component';

@Component({
    selector: "page-signup",
    templateUrl : 'signup.html'
})

export class SignupComponent{

    signupForm : FormGroup;
    constructor(public navCtrl : NavController,
                public navparams : NavParams,
                public _loginserv : LoginService){

           this.signupForm = new FormGroup({
               emailId : new FormControl(""),
               partnerId : new FormControl("Ginger"),
               password : new FormControl("")
           })         
    }

    public UserSignup(){
        
        this._loginserv.SignupUser(this.signupForm)
            .subscribe( (res) => { console.log('signup',res); this.storeToken(res) ; this.navHome();},
                        (err) => { console.log(err)})
    }

    public navHome(){
         this.navCtrl.push(HomeComponent);   
    }

    public storeToken(res){
        localStorage.setItem("access_token", res.token);
    }
}