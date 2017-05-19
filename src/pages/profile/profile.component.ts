import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { ProfileEditComponent } from './edit/profile-edit.component'; 
import { SettingsComponent } from './settings/settings.component';
import { ProfileService} from '../../services/profile.service';

@Component({
    selector : 'page-profile',
    templateUrl : 'profile.html'
})

export class ProfileComponent {

    public profiledata = {};

    constructor(private navCtrl : NavController,
                private _proServ : ProfileService ){

                    this.getProfileData();
                }

    ProfileEdit(){
        this.navCtrl.push(ProfileEditComponent,{
            'data' : this.profiledata
        });
    }

    SettingsNav(){
        this.navCtrl.push(SettingsComponent);
    }

    getProfileData(){
        this._proServ.GetUserProfile()
        .subscribe( (data) => { this.profiledata = data },
                    () => {console.log(this.profiledata)})
    }

    Uploadedgifs = [];
    GifUploadedviaUser(){
        this._proServ.getGifsUploadedByUrl()
        .subscribe( (data) => { this.Uploadedgifs = data;},
                    (err) => { console.log(err)})
    }

}