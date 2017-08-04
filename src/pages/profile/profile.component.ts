import { Component } from '@angular/core';
import { NavController,NavParams,Events } from 'ionic-angular';
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

    public profiledata = {"fullName":""};
    EmailId;
    fullName;
    editBtn = true;
    xyz = "contain";
    lessdata;
    LessData;
    constructor(private navCtrl : NavController,
                private navparams : NavParams,
                private _proServ : ProfileService,
                private cs : CustomService,
                public events: Events){

                this.EmailId = this.navparams.get('email');
                events.subscribe('lessdata:created', (user) => {
                    console.log('Welcome', user);
                    this.lessdata = user;
                    this.LessData = this.lessdata;
                  
                });

                this.lessdata = localStorage.getItem('lessdata');
                  if(this.lessdata === "false"){
                    this.LessData = false
                  } 
                  else{
                    this.LessData = true;
                }  


                       console.log()             
                    if(this.EmailId != null ){  
                        this.editBtn = false;  
                        this.fullName  = this.EmailId.split('@',1)
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
        .subscribe( (data) => { this.profiledata = data ; this.fullName = this.profiledata.fullName },
                     (err) => {console.log(err);},   
                     () => {console.log('profiledata',this.profiledata)})
    }

    getProfileDataByEmail(){
        this.cs.showLoader();
        this._proServ.getUploaderInfo(this.EmailId,0)
            .subscribe( (data) => { this.Uploadedgifs = data.contents; this.cs.hideLoader();},
                (err) => { console.log(err); this.cs.hideLoader();},
                ()    => { console.log('profiledataa',this.profiledata)})
    }

    Uploadedgifs = [];
    Uploadedgifs2 = [];
    GifUploadedviaUser(){
        this.cs.showLoader();
        this._proServ.getGifsUploadedByUrl(0)
            .subscribe( (data) => { this.Uploadedgifs = data.contents; this.cs.hideLoader(); this.checkUploadGifs(data);  },
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


//  this._proServ.getUploaderInfo(this.EmailId,this.currentPage)

    currentPage = 0;
 doInfinite(infiniteScroll) {

   this.currentPage = this.currentPage + 1;
    console.log('currentpage', this.currentPage);
       this._proServ.getGifsUploadedByUrl(this.currentPage).subscribe(data =>
        {
          infiniteScroll.complete();
        //   this.hasMoreData = true;
        //   this.trendingGIFs = data;
          this.Uploadedgifs =  this.Uploadedgifs.concat(data.contents); 
      }, 
    err => {
      infiniteScroll.complete();
      this.currentPage -= 1;
   //   this.onError(err);
    },
     () => console.log('Next Page Loading completed')
     );
  }

}