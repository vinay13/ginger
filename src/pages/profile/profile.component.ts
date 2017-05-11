import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { ProfileEditComponent } from './edit/profile-edit.component'; 

@Component({
    selector : 'page-profile',
    templateUrl : 'profile.html'
})

export class ProfileComponent {

    constructor(private navCtrl : NavController ){}

    ProfileEdit(){
        this.navCtrl.push(ProfileEditComponent);
    }
    

}