import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { ProfileEditComponent } from './edit/profile-edit.component'; 
import { SettingsComponent } from './settings/settings.component';
import { ProfileService} from '../../services/profile.service';
import { CustomService } from '../../services/custom.service';
import { GifDetailComponent } from '../home/gifdetail/gifdetail.component';

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
                    this.GifsFavorites();

                }

    ProfileEdit(){
        this.navCtrl.push(ProfileEditComponent,{
            'data' : this.profiledata
        });
    }

    gifViewer(url){
        this.navCtrl.push(GifDetailComponent,{
            "url" : url
        })
    }

    SettingsNav(){
        this.navCtrl.push(SettingsComponent);
    }

    getProfileData(){
        this._proServ.GetUserProfile()
        .subscribe( (data) => { this.profiledata = data },
                     (err) => {console.log(err);},   
                     () => {console.log(this.profiledata)})
    }

    Uploadedgifs = [];
    GifUploadedviaUser(){
        this.cs.showLoader();
        this._proServ.getGifsUploadedByUrl()
        .subscribe( (data) => { this.Uploadedgifs = data;this.cs.hideLoader(); this.checkUploadGifs(data);  },
                    (err) => { this.cs.hideLoader();},
                    () => { console.log('uploadgifs',this.Uploadedgifs)})
    }

    favoritesgifs = [];
    GifsFavorites(){
        this._proServ.GetFavoriteGifsviaUser()
            .subscribe((data) => { this.Uploadedgifs = data.favouriteGifs; },
                        (err) => { console.log(err)},
                        () => { console.log('favgifs',this.Uploadedgifs);})
    }

// this.Uploadedgifs.push(this.favoritesgifs);

    noUploads = false;
    checkUploadGifs(res){
        if (res == {}){   
            this.noUploads = true;
        }
    }

}