import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import {FormGroup,FormControl,Validators} from '@angular/forms';

@Component({
    selector: "page-signup",
    templateUrl : 'signup.html'
})

export class SignupComponent{

    signupForm : FormGroup;
    constructor(public navCtrl : NavController,
                public navparams : NavParams){

           this.signupForm = new FormGroup({
               emailId : new FormControl('vinay14@gmail.com'),
               partnerId : new FormControl('Ginger'),
               password : new FormControl('090oiuy')
           })         
    }

    public UserSignup(){
        //this.navCtrl.push();
    }
}