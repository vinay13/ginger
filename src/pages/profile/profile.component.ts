import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { ProfileEditComponent } from './edit/profile-edit.component'; 
import { SettingsComponent } from './settings/settings.component';
import { ProfileService} from '../../services/profile.service';
import { CustomService } from '../../services/custom.service';

@Component({
    selector : 'page-profile',
    templateUrl : 'profile.html'
})

export class ProfileComponent {

    public profiledata = {};

    constructor(private navCtrl : NavController,
                private _proServ : ProfileService,
                private cs : CustomService){

                    this.getProfileData();
                    this.GifUploadedviaUser();
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
        this.cs.showLoader();
        this._proServ.getGifsUploadedByUrl()
        .subscribe( (data) => { this.Uploadedgifs = data; this.cs.hideLoader();},
                    (err) => { this.cs.hideLoader();})
    }

}