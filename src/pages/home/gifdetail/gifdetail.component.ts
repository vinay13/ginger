import { Component } from '@angular/core';
import { PopoverController } from 'ionic-angular';
import { PopOverComponent } from './popover';
import { SearchResultComponent } from '../../search/searchResult/search-result.component';
import { NavController, ToastController , NavParams } from 'ionic-angular';
import { SocialSharing } from '@ionic-native/social-sharing';
import { CustomService } from '../../../services/custom.service';
import { HomeService } from '../../../services/home.service';

@Component({
    selector : 'page-gifdetail',
    templateUrl : 'gifdetail.html'
})

export class GifDetailComponent {

    public gifurl;
    public gifobject;
    public recomns : any;
    constructor(public popoverCtrl : PopoverController,
                public navCtrl : NavController,
                public toastCtrl : ToastController,
                private socialSharing: SocialSharing,
                private _homeserv: HomeService,
                private cs : CustomService,
                public navparams : NavParams){
                    this.gifobject = this.navparams.get('url');
                    this.gifurl = this.gifobject.images.downsized_still.url;
                    this.RecommendedGifs();
                }

    loadProgress: number = 0;	            
    ionViewDidLoad(){

		setInterval(() => {

			if(this.loadProgress < 100){
				this.loadProgress++;
                
                if(this.loadProgress == 40){
                    this.gifurl = this.gifobject.images.downsized.url;
                }

                // if(this.loadProgress == 50){
                //      console.log(this.gifurl);
                //     this.gifurl = this.gifobject.images.downsized_medium.url;
                //     console.log(this.gifurl);
                // }

                if(this.loadProgress == 80)
                {
                    console.log(this.gifurl);
                    this.gifurl = this.gifobject.images.original.url;
                    console.log(this.gifurl);
                }
			}

		}, 100);

	}            

    
    RecommendedGifs(){
        this._homeserv.getTrendingGifs('hindi')
            .subscribe( (res) => {this.recomns = res.data},
                        (err) => console.log(err),
                        () => console.log(this.recomns))
    }

    presentPopover(myEvent){
        let popover = this.popoverCtrl.create(PopOverComponent);
        popover.present({
            ev: myEvent
        });
    }

    TagClicked(tag){
        this.navCtrl.push(SearchResultComponent,{
            'tag' : tag
        });
    }

    GIFviewer(url){
        this.navCtrl.push(GifDetailComponent,{
            'url': url
        });
        
    }

    Addfavorites(){
       this.presentToast();
    }

    presentToast(){
        let toast = this.toastCtrl.create({
            message: 'Added to favorites list',
            duration: 3000
        });
    toast.present();
    }

   shareGif(){
    this.cs.showLoader();
    this.socialSharing.shareViaWhatsApp("Message via WhatsApp",this.gifurl, "https://giphy.com")
      .then(()=>{
        this.cs.hideLoader();
      },
      ()=>{
         alert("failed")
      })
  }

  shareGifTwitter(){
      this.cs.showLoader();
    this.socialSharing.shareViaTwitter("message",this.gifurl,"https://giphy.com")
        .then(()=>{
            this.cs.hideLoader();
        },
        ()=>{
         alert("failed")
         this.cs.hideLoader();
      })
  }

  shareGifFacebook(){
      this.cs.showLoader();
      this.socialSharing.shareViaFacebook("message",this.gifurl,"https://giphy.com")
        .then(() => {
            this.cs.hideLoader();
        },
        () => { alert("U don't have facebook app"); this.cs.hideLoader(); } )
  }


  shareGifInstagram(){
      this.cs.showLoader();
      this.socialSharing.shareViaInstagram("message", this.gifurl)
            .then(() => {
                this.cs.hideLoader();
            },
            ()=> { alert("U don't have Instagram app"); this.cs.hideLoader(); })
  }
  
}