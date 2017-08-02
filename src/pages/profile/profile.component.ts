import { Component } from '@angular/core';
import { NavController,NavParams } from 'ionic-angular';
import { ProfileEditComponent } from './edit/profile-edit.component'; 
import { SettingsComponent } from './settings/settings.component';
import { ProfileService } from '../../services/profile.service';
import { CustomService } from '../../services/custom.service';
import { GifDetailComponent } from '../home/gifdetail/gifdetail.component';

@Component({
    selector : 'page-profile',
    templateUrl : 'profile.html'
})

export class ProfileComponent{

    public profiledata = {};
    EmailId;
    xyz = "contain";
    constructor(private navCtrl : NavController,
                private navparams : NavParams,
                private _proServ : ProfileService,
                private cs : CustomService){

                this.EmailId = this.navparams.get('email');
                       console.log()             
                    if(this.EmailId != null ){
                        this.getProfileDataByEmail();
                    }
                    else{
                        this.getProfileData();
                        this.GifUploadedviaUser();
                    }
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
                     () => {console.log('profiledata',this.profiledata)})
    }

    getProfileDataByEmail(){
        this._proServ.getUploaderInfo(this.EmailId)
            .subscribe( (data) => { this.Uploadedgifs = data.contents},
                (err) => { console.log(err);},
                ()    => { console.log('profiledataa',this.profiledata)})
    }

    Uploadedgifs = [];
    Uploadedgifs2 = [];
    GifUploadedviaUser(){
        this.cs.showLoader();
        this._proServ.getGifsUploadedByUrl()
        .subscribe( (data) => { this.Uploadedgifs = data; this.GifsFavorites();  this.cs.hideLoader(); this.checkUploadGifs(data);  },
                    (err) => { this.cs.hideLoader();},
                    () => { console.log('uploadgifs',this.Uploadedgifs)})
    }

    pushfavvv(data){
        let posts=  data;
                for(let post of posts){
                     console.log(post);
                    this.Uploadedgifs.push(post); 
            }
    }    
  
    // nullcheck(data){
    //     if( data = null )
    //     { this.Uploadedgifs = []
    //      }
    //      else {
    //           this.Uploadedgifs = data 
    //         };
    // }

    favoritesgifs = [];
    GifsFavorites(){
        this._proServ.GetFavoriteGifsviaUser()
            .subscribe((data) => { this.favoritesgifs = data.favouriteGifs; this.pushfavvv(this.favoritesgifs); },
                        (err) => { console.log(err)},
                        () => { console.log('favgifs',this.favoritesgifs); console.log('after push', this.Uploadedgifs);})
    }

// this.Uploadedgifs.push(this.favoritesgifs);

    noUploads = false;
    checkUploadGifs(res){
        if (res == {}){   
            this.noUploads = true;
        }
    }

}